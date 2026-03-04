package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.order.OrderDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.CartOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.DirectOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.OrderDetailIdListRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.response.*;
import com.project.healthy_life_was.healthy_life.dto.payment.ApiResponseDto;
import com.project.healthy_life_was.healthy_life.dto.payment.CancelRequestDto;
import com.project.healthy_life_was.healthy_life.dto.payment.KGPaymentDto;
import com.project.healthy_life_was.healthy_life.dto.payment.VerifyRequestDto;
import com.project.healthy_life_was.healthy_life.entity.cart.Cart;
import com.project.healthy_life_was.healthy_life.entity.cart.CartItem;
import com.project.healthy_life_was.healthy_life.entity.deliverAddress.DeliverAddress;
import com.project.healthy_life_was.healthy_life.entity.order.Order;
import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import com.project.healthy_life_was.healthy_life.entity.order.OrderStatus;
import com.project.healthy_life_was.healthy_life.entity.product.Product;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.repository.*;
import com.project.healthy_life_was.healthy_life.service.KGPaymentService;
import com.project.healthy_life_was.healthy_life.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImplement implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final DeliverAddressRepository deliverAddressRepository;
    private final KGPaymentService kgPaymentService;

    @Override
    @Transactional
    public ResponseDto<PostOrderResponseDto> cartOrder(String username, CartOrderRequestDto dto) {
        PostOrderResponseDto data;

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "user"));

        DeliverAddress deliver = deliverAddressRepository.findByDeliverAddressId(dto.getDeliverAddressId());
        if (deliver == null || !deliver.getUser().equals(user)) {
            throw new IllegalArgumentException(ResponseMessage.NO_PERMISSION + "deliverAddress");
        }

        List<CartItem> cartItems = cartItemRepository.findAllById(dto.getCartItemIds());
        if (cartItems.isEmpty() || cartItems.stream().anyMatch(ci -> !ci.getCart().getUser().equals(user))) {
            throw new IllegalArgumentException(ResponseMessage.NO_PERMISSION + "cartItems");
        }

        int totalAmount = calculateCartTotalAmount(cartItems, dto.getShippingCost());

        Map<String, Object> paymentData = verifyPaymentExistOrThrow(dto.getKgPayment());
        verifyAmountOrThrow(paymentData, totalAmount);

        try {
            Order order = Order.builder()
                    .user(user)
                    .deliverAddress(deliver)
                    .orderRecipientName(dto.getOrderRecipientName())
                    .orderRecipientPhone(dto.getOrderRecipientPhone())
                    .orderTotalAmount(totalAmount)
                    .shippingRequest(dto.getShippingRequest())
                    .orderDate(LocalDateTime.now())
                    .orderCode(dto.getKgPayment().getMerchantUid())
                    .impUid(dto.getKgPayment().getImpUid())
                    .build();

            orderRepository.save(order);

            List<OrderDetail> orderDetails = cartItems.stream()
                    .map(ci -> orderDetailRepository.save(
                            OrderDetail.builder()
                                    .order(order)
                                    .product(ci.getProduct())
                                    .quantity(ci.getProductQuantity())
                                    .price(ci.getProduct().getPPrice())
                                    .totalPrice(ci.getProductQuantity() * ci.getProduct().getPPrice())
                                    .orderStatus(OrderStatus.PENDING)
                                    .build()
                    ))
                    .toList();

            cartRepository.deleteByCartItemIds(dto.getCartItemIds());

            data = new PostOrderResponseDto(order, orderDetails);

        } catch (Exception e) {
            kgPaymentService.cancel(
                    new CancelRequestDto(dto.getKgPayment().getImpUid())
            );
            throw e;
        }

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }


    @Override
    @Transactional
    public ResponseDto<PostOrderResponseDto> directOrder(String username, Long pId, DirectOrderRequestDto dto) {
        try {
            int quantity = Math.max(dto.getQuantity(), 1);
            String shippingRequest = (dto.getShippingRequest() == null || dto.getShippingRequest().isBlank())
                    ? "요청사항 없음" : dto.getShippingRequest();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "user"));

            Product product = productRepository.findById(pId)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "product"));

            DeliverAddress deliver = deliverAddressRepository.findByDeliverAddressId(dto.getDeliverAddressId());
            if (deliver == null || !deliver.getUser().getUserId().equals(user.getUserId())) {
                throw new IllegalArgumentException("No permission: deliverAddress");
            }

            final int totalAmount = product.getPPrice() * dto.getQuantity() + dto.getShippingCost();

            Order order = Order.builder()
                    .user(user)
                    .deliverAddress(deliver)
                    .orderRecipientName(dto.getOrderRecipientName())
                    .orderRecipientPhone(dto.getOrderRecipientPhone())
                    .orderTotalAmount(totalAmount)
                    .shippingRequest(shippingRequest)
                    .shippingCost(dto.getShippingCost())
                    .orderDate(LocalDateTime.now())
                    .build();
            order = orderRepository.save(order);

            String orderCode = dto.getKgPayment().getMerchantUid();
            String impUid = dto.getKgPayment().getImpUid();
            order.setOrderCode(orderCode);
            order.setImpUid(impUid);
            orderRepository.save(order);

            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .price(product.getPPrice())
                    .totalPrice(product.getPPrice() * quantity)
                    .orderStatus(OrderStatus.PENDING)
                    .preDeliveryStatus(null)
                    .build();
            orderDetailRepository.save(orderDetail);

            List<OrderDetail> orderDetails = List.of(orderDetail);
            PostOrderResponseDto data = new PostOrderResponseDto(order, orderDetails);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

        } catch (IllegalArgumentException e) {
            return ResponseDto.setFailed(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }



    @Override
    public ResponseDto<OrderListResponseDto> getOrder(String username, LocalDate startOrderDate, LocalDate endOrderDate) {
        OrderListResponseDto data = null;

        try {
            List<Order> orders;
            LocalDateTime start = startOrderDate == null
                    ? null
                    : startOrderDate.atStartOfDay();

            LocalDateTime end = endOrderDate == null
                    ? null
                    : endOrderDate.plusDays(1).atStartOfDay();

            if ((startOrderDate == null || startOrderDate.equals("")) &&
                    (endOrderDate == null || endOrderDate.equals(""))) {
                orders = orderRepository.findAllByUser_Username(username);
            } else {
                orders = orderRepository.findAllByUser_usernameAndStartAndEnd(username, start, end);
            }

            List<OrderDto> dtos = orders.stream()
                    .map(OrderDto::new)
                    .collect(Collectors.toList());



            data = new OrderListResponseDto(dtos);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<OrderListResponseDto> changeOrderStatus(String username, OrderDetailIdListRequestDto dto, String orderStatus) {
        OrderListResponseDto data = null;
        try {
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderDetailIds(dto.getOrderDetailIds());
            if (orderDetails.isEmpty()) {
                throw new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "orderDetail");
            }
            for (OrderDetail orderDetail : orderDetails) {
                if (orderDetail.getOrderStatus().equals(OrderStatus.CANCELLED)) {
                    return ResponseDto.setFailed(ResponseMessage.EXIST_DATA + "CANCELLED");
                }
                if (orderDetail.getOrderStatus().equals(OrderStatus.SHIPPED) && orderStatus.equals(OrderStatus.CANCELLED.name())) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_CANCEL);
                }
                if (orderDetail.getOrderStatus().equals(OrderStatus.RETURNED)) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_CHANGE_ORDER_STATUS + "RETURNED");
                }
                if (orderDetail.getOrderStatus().equals(OrderStatus.EXCHANGED)) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_CHANGE_ORDER_STATUS + "EXCHANGED");
                }
                if (orderDetail.getOrderStatus().equals(OrderStatus.CONFIRMED) && orderStatus.equals(OrderStatus.CANCELLED.name())) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_EXCHANGE);
                }
                if (orderDetail.getOrderStatus().equals(OrderStatus.SHIPPED) && orderStatus.equals(OrderStatus.RETURN_REQUEST.name())) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_RETURN);
                }
                if (orderDetail.getOrderStatus().equals(OrderStatus.RETURN_REQUEST)) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_CHANGE_ORDER_STATUS + "RETURN_REQUEST");
                }
                if (orderDetail.getOrderStatus().equals(OrderStatus.EXCHANGE_REQUEST)) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_CHANGE_ORDER_STATUS + "EXCHANGE_REQUEST");
                }
                if (orderDetail.getOrderStatus().equals(OrderStatus.EXCHANGE_IN_PROGRESS)) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_CHANGE_ORDER_STATUS + "EXCHANGE_IN_PROGRESS");
                }
                if (orderDetail.getOrderStatus().equals(OrderStatus.RETURN_IN_PROGRESS)) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_CHANGE_ORDER_STATUS + "RETURN_IN_PROGRESS");
                }
                if (orderDetail.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_CHANGE_ORDER_STATUS + "CONFIRMED");
                }
                if (ChronoUnit.DAYS.between(orderDetail.getOrder().getOrderDate(), LocalDate.now()) >= 8) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_CHANGE_STATUS_DATE);
                }

                orderDetail.setPreDeliveryStatus(String.valueOf(orderDetail.getOrderStatus()));
                orderDetail.setOrderStatus(OrderStatus.valueOf(orderStatus));
            }

            orderDetails.stream()
                    .map(OrderDetail::getOrder)
                    .distinct()
                    .forEach(orderRepository::save);

            List<OrderDto> orders = orderDetails.stream()
                    .map(OrderDetail::getOrder)
                    .map(OrderDto::new)
                    .distinct()
                    .toList();
            data = new OrderListResponseDto(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<OrderCancelResponseDto> cancelReturnOrExchange(String username, Long orderDetailId) {
        OrderCancelResponseDto data = null;
        try {
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "orderDetail"));

            if (orderDetail.getOrderStatus().equals(OrderStatus.RETURN_REQUEST) || orderDetail.getOrderStatus().equals(OrderStatus.EXCHANGE_REQUEST)) {
                orderDetail.setOrderStatus(OrderStatus.valueOf(orderDetail.getPreDeliveryStatus()));
            } else {
                return ResponseDto.setFailed(ResponseMessage.NOT_RETURN_EXCHANGE);
            }

            orderDetail.setPreDeliveryStatus(null);
            orderRepository.save(orderDetail.getOrder());

            data = new OrderCancelResponseDto(orderDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<List<OrderReviewResponseDto>> orderGetReview(String username) {

        try {
            LocalDateTime limitDate = LocalDateTime.now().minusDays(30);
            List<Order> orders = orderDetailRepository.findCanCreateReviewOrders(username, limitDate);

            List<OrderReviewResponseDto> data = orders.stream()
                    .flatMap(order -> order.getOrderDetails().stream())
                    .map(od -> new OrderReviewResponseDto(
                            od.getOrderDetailId(),
                            od.getProduct().getPId(),
                            od.getProduct().getPName(),
                            od.getProduct().getPImgUrl(),
                            od.getOrderStatus(),
                            od.getOrder().getOrderDate()
                    ))
                    .toList();

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseDto<List<OrderCancelResponseDto>> orderCancel(String username, CancelRequestDto dto) {
        System.out.println("OrderDto impUid = " + dto.getImpUid());
        List<OrderCancelResponseDto> data = null;
        List<Order> orders = orderRepository.findAllByImpUid(dto.getImpUid());
        orders.stream()
                .map(Order::getImpUid)
                .forEach(impUid ->
                        System.out.println("OrderDto impUid = " + impUid)
                );

        if (orders.isEmpty()) {
            return ResponseDto.setFailed("주문 없음");
        }

        boolean hasNoPermission = orders.stream()
                .anyMatch(order -> !order.getUser().getUsername().equals(username));

        if (hasNoPermission) {
            return ResponseDto.setFailed(ResponseMessage.NO_PERMISSION);
        }

        boolean cancelable = orders.stream()
                .allMatch(order -> order.getOrderDetails().stream().allMatch(detail -> detail.getOrderStatus() == OrderStatus.PENDING));

        if (!cancelable) {
            return ResponseDto.setFailed("취소 가능한 상태가 아닙니다.");
        }

        ApiResponseDto payCancelResult = kgPaymentService.cancel(dto);

        if (!"OK".equals(payCancelResult.getStatus())) {
            return ResponseDto.setFailed("결제 취소 실패");
        }

        orders.forEach(order ->
                order.getOrderDetails().forEach(d -> d.setOrderStatus(OrderStatus.CANCELLED)));

        data = orders.stream()
                .flatMap(order -> order.getOrderDetails().stream())
                .map(OrderCancelResponseDto::new)
                .toList();

        return ResponseDto.setSuccess(
                ResponseMessage.SUCCESS, data
        );
    }

    private Map<String, Object> verifyPaymentExistOrThrow(KGPaymentDto kg) {
        if (kg == null) throw new IllegalArgumentException("결제 정보 없음");

        VerifyRequestDto req = new VerifyRequestDto();
        req.setImpUid(kg.getImpUid());
        req.setMerchantUid(kg.getMerchantUid());

        ApiResponseDto res = kgPaymentService.verify(req);

        if (res == null || !"OK".equalsIgnoreCase(res.getStatus()) || res.getData() == null) {
            throw new IllegalArgumentException("결제 검증 실패");
        }

        return res.getData();
    }

    private void verifyAmountOrThrow(Map<String, Object> data, int expectedAmount) {
        int paidAmount = (int) data.get("amount");
        if (paidAmount != expectedAmount) {
            throw new IllegalArgumentException("결제 금액 불일치");
        }
    }


    private int calculateCartTotalAmount(List<CartItem> cartItems, int shippingCost) {
        int productTotal = cartItems.stream()
                .mapToInt(ci -> ci.getProductQuantity() * ci.getProduct().getPPrice())
                .sum();
        return productTotal + shippingCost;
    }


}