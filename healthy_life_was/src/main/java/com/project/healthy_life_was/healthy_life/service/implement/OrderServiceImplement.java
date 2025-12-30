package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.order.OrderDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.CartOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.DirectOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.OrderDetailIdListRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.response.*;
import com.project.healthy_life_was.healthy_life.dto.payment.ApiResponseDto;
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

import java.awt.print.Printable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private String makeOrderCode(Long id) {
        return LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                + "-" + String.format("%06d", id);
    }

    @Override
    public ResponseDto<PostOrderResponseDto> cartOrder(String username, CartOrderRequestDto dto) {
        PostOrderResponseDto data = null;
        List<Long> cartItemIds = dto.getCartItemIds();
        String shippingRequest = dto.getShippingRequest();
        String recipientName = dto.getOrderRecipientName();
        String recipientPhone = dto.getOrderRecipientPhone();
        Long deliverAddressId = dto.getDeliverAddressId();

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "user"));

            Cart cart = cartRepository.findByUser(user)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "cart"));

            DeliverAddress deliver = deliverAddressRepository.findByDeliverAddressId(deliverAddressId);
            if (deliver == null || !deliver.getUser().equals(user)){
                throw new IllegalArgumentException(ResponseMessage.NO_PERMISSION + "deliverAddress");
            }

            List<CartItem> cartItems = cartItemRepository.findAllById(cartItemIds);
            if (cartItems.isEmpty() || cartItems.stream().anyMatch(c -> !c.getCart().getUser().equals(user))) {
                throw new IllegalArgumentException(ResponseMessage.NO_PERMISSION + "cartItems");
            }

            int totalAmount = cartItems.stream()
                    .mapToInt(cartItem -> cartItem.getProductQuantity() * cartItem.getProduct().getPPrice())
                    .sum() + 3000;
            verifyPaymentOrThrow(dto.getKgPayment(), totalAmount);

            Order order = Order.builder()
                    .cart(cart)
                    .user(user)
                    .orderRecipientName(recipientName)
                    .orderRecipientPhone(recipientPhone)
                    .orderTotalAmount(totalAmount)
                    .shippingRequest(shippingRequest)
                    .deliverAddress(deliver)
                    .orderDate(LocalDate.now())
                    .build();

            order.setCart(null);
            order.setOrderCode(makeOrderCode(order.getOrderId()));
            orderRepository.save(order);

            cartRepository.deleteByCartItemIds(cartItemIds);

            List<OrderDetail> orderDetails = cartItems.stream()
                    .map(cartItem -> {
                        OrderDetail orderDetail = OrderDetail.builder()
                                .order(order)
                                .product(cartItem.getProduct())
                                .orderStatus(OrderStatus.PENDING)
                                .quantity(cartItem.getProductQuantity())
                                .price(cartItem.getProduct().getPPrice())
                                .totalPrice(cartItem.getProductQuantity() * cartItem.getProduct().getPPrice())
                                .build();
                        orderDetailRepository.save(orderDetail);
                        return orderDetail;
                    })
                    .collect(Collectors.toList());


            data = new PostOrderResponseDto(order, orderDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
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

            System.out.println("Logged-in user ID: " + user.getUserId());
            System.out.println("Deliver user ID: " + deliver.getUser().getUserId());

            final int shippingCost = 3000;
            final int totalAmount = product.getPPrice() * quantity + shippingCost;

            Order order = Order.builder()
                    .user(user)
                    .deliverAddress(deliver)
                    .orderRecipientName(dto.getOrderRecipientName())
                    .orderRecipientPhone(dto.getOrderRecipientPhone())
                    .orderTotalAmount(totalAmount)
                    .shippingRequest(shippingRequest)
                    .shippingCost(shippingCost)
                    .orderDate(LocalDate.now())
                    .build();
            order = orderRepository.save(order);

            String orderCode = dto.getKgPayment().getMerchantUid();
            order.setOrderCode(orderCode);
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

            if ((startOrderDate == null || startOrderDate.equals("")) &&
                    (endOrderDate == null || endOrderDate.equals(""))) {
                orders = orderRepository.findAllByUser_Username(username);
            } else {
                orders = orderRepository.findAllByUser_usernameAndStartAndEnd(username, startOrderDate, endOrderDate);
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
    public ResponseDto<OrderListResponseDto> orderGetReview(String username) {

        try {
            List<Order> orders = orderRepository.findDeliveredOrdersWithoutReview(username);

            List<OrderDto> orderList = orders.stream()
                    .map(OrderDto::new)
                    .collect(Collectors.toList());

            OrderListResponseDto responseDto = new OrderListResponseDto(orderList);

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    private void verifyPaymentOrThrow(KGPaymentDto kg, int expectedAmount) {
        if (kg == null) throw new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "kgPayment");

        VerifyRequestDto req = new VerifyRequestDto();
        req.setImpUid(kg.getImpUid());
        req.setMerchantUid(kg.getMerchantUid());

        ApiResponseDto res = kgPaymentService.verify(req);

        // 1) API 호출 성공 여부: status == "OK"
        if (res == null || res.getStatus() == null || !"OK".equalsIgnoreCase(res.getStatus()) || res.getData() == null) {
            throw new IllegalArgumentException(ResponseMessage.NO_PERMISSION); // 키 없으면 임시로 NO_PERMISSION 등 사용
        }

        Map<String, Object> map = res.getData();

        // 2) 결제 금액
        Object amountObj = map.get("amount"); // KGPaymentService에서 넣어준 키 이름과 동일해야 함
        if (amountObj == null) throw new IllegalArgumentException(ResponseMessage.NO_PERMISSION);

        int paidAmount;
        if (amountObj instanceof Number n) paidAmount = n.intValue();
        else paidAmount = Integer.parseInt(String.valueOf(amountObj));

        // 3) 결제 상태(예: "paid", "ready", "cancelled"...)
        String payStatus = String.valueOf(map.get("status"));
        if (!"paid".equalsIgnoreCase(payStatus)) {
            throw new IllegalArgumentException(ResponseMessage.NO_PERMISSION);
        }
        if (paidAmount != expectedAmount) {
            throw new IllegalArgumentException(ResponseMessage.NO_PERMISSION);
        }

        System.out.println(">>> imp_uid: " + kg.getImpUid());
        System.out.println(">>> merchant_uid: " + kg.getMerchantUid());
        System.out.println(">>> expected amount: " + expectedAmount);
        System.out.println(">>> actual status: " + payStatus);
        System.out.println(">>> actual amount: " + paidAmount);

    }

}
