package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.order.OrderDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.CartOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.DirectOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.OrderDetailIdListRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.response.*;
import com.project.healthy_life_was.healthy_life.entity.cart.Cart;
import com.project.healthy_life_was.healthy_life.entity.cart.CartItem;
import com.project.healthy_life_was.healthy_life.entity.deliverAddress.DeliverAddress;
import com.project.healthy_life_was.healthy_life.entity.order.Order;
import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import com.project.healthy_life_was.healthy_life.entity.order.OrderStatus;
import com.project.healthy_life_was.healthy_life.entity.product.Product;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.repository.*;
import com.project.healthy_life_was.healthy_life.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Printable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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
    public ResponseDto<PostOrderResponseDto> directOrder(String username, Long pId, DirectOrderRequestDto dto) {
        PostOrderResponseDto data = null;
        int quantity = dto.getQuantity();
        String shippingRequest = dto.getShippingRequest();
        String recipientName = dto.getOrderRecipientName();
        String recipientPhone = dto.getOrderRecipientPhone();
        Long deliverAddressId = dto.getDeliverAddressId();
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "user"));

            Product product = productRepository.findById(pId)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "product"));
            DeliverAddress deliver = deliverAddressRepository.findByDeliverAddressId(deliverAddressId);

            int totalAmount = (product.getPPrice() * quantity) + 3000;

            Order order = Order.builder()
                    .user(user)
                    .orderRecipientName(recipientName)
                    .orderRecipientPhone(recipientPhone)
                    .orderTotalAmount(totalAmount)
                    .shippingRequest(shippingRequest)
                    .deliverAddress(deliver)
                    .orderDate(LocalDate.now())
                    .build();
            orderRepository.save(order);

            List<OrderDetail> orderDetails = new ArrayList<>();
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .orderStatus(OrderStatus.PENDING)
                    .product(product)
                    .quantity(quantity)
                    .price(product.getPPrice())
                    .totalPrice(quantity * product.getPPrice())
                    .build();
            orderDetailRepository.save(orderDetail);
            orderDetails.add(orderDetail);

            data = new PostOrderResponseDto(order, orderDetails);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
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
                if (orderDetail.getOrderStatus().equals(OrderStatus.RETURN)
                        && !orderStatus.equals(OrderStatus.DELIVERED.name())) {
                    return ResponseDto.setFailed(ResponseMessage.EXIST_DATA + "RETURN");
                }
                if (orderDetail.getOrderStatus().equals(OrderStatus.EXCHANGE)
                        && !orderStatus.equals(OrderStatus.DELIVERED.name())) {
                    return ResponseDto.setFailed(ResponseMessage.EXIST_DATA + "EXCHANGE");
                }
                if (orderDetail.getOrderStatus().equals(OrderStatus.SHIPPED) && orderStatus.equals(OrderStatus.EXCHANGE.name())) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_EXCHANGE);
                }
                if (orderDetail.getOrderStatus().equals(OrderStatus.SHIPPED) && orderStatus.equals(OrderStatus.RETURN.name())) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_RETURN);
                }

                if (ChronoUnit.DAYS.between(orderDetail.getOrder().getOrderDate(), LocalDate.now()) >= 8) {
                    return ResponseDto.setFailed(ResponseMessage.CAN_NOT_CHANGE_STATUS_DATE);   
                }

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

            if (orderDetail.getOrderStatus().equals(OrderStatus.RETURN) || orderDetail.getOrderStatus().equals(OrderStatus.EXCHANGE)) {
                orderDetail.setOrderStatus(OrderStatus.DELIVERED);
            } else {
                return ResponseDto.setFailed(ResponseMessage.NOT_RETURN_EXCHANGE);
            }

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

}
