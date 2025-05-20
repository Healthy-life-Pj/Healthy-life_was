package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.CartOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.DirectOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.OrderGetRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.response.CartOrderResponseDto;
import com.project.healthy_life_was.healthy_life.dto.order.response.DirectOrderResponseDto;
import com.project.healthy_life_was.healthy_life.dto.order.response.OrderCancelResponseDto;
import com.project.healthy_life_was.healthy_life.dto.order.response.OrderDetailResponseDto;
import com.project.healthy_life_was.healthy_life.entity.cart.Cart;
import com.project.healthy_life_was.healthy_life.entity.cart.CartItem;
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

import java.time.LocalDate;
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

    @Override
    public ResponseDto<CartOrderResponseDto> cartOrder(String username, CartOrderRequestDto dto) {
        CartOrderResponseDto data = null;
        List<Long> cartItemIds = dto.getCartItemIds();
        String shippingRequest = dto.getShippingRequest();

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "user"));
            Cart cart = cartRepository.findByUser(user)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "cart"));
            List<CartItem> cartItems = cartItemRepository.findAllById(cartItemIds);
            if (cartItems.isEmpty()) {
                throw new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "cart items");
            }

            int totalAmount = cartItems.stream()
                    .mapToInt(cartItem -> cartItem.getProductQuantity() * cartItem.getProduct().getPPrice())
                    .sum() + 3000;

            Order order = Order.builder()
                    .cart(cart)
                    .user(user)
                    .orderStatus(OrderStatus.PENDING)
                    .orderTotalAmount(totalAmount)
                    .shippingRequest(shippingRequest)
                    .orderDate(LocalDate.now())
                    .build();
            orderRepository.save(order);

            List<OrderDetail> orderDetails = cartItems.stream()
                    .map(cartItem -> {
                        OrderDetail orderDetail = OrderDetail.builder()
                                .order(order)
                                .product(cartItem.getProduct())
                                .quantity(cartItem.getProductQuantity())
                                .price(cartItem.getProduct().getPPrice())
                                .totalPrice(cartItem.getProductQuantity() * cartItem.getProduct().getPPrice())
                                .build();
                        orderDetailRepository.save(orderDetail);
                        return orderDetail;
                    })
                    .collect(Collectors.toList());


            data = new CartOrderResponseDto(order, orderDetails);
            cartRepository.delete(cart);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<DirectOrderResponseDto> directOrder(String username, Long pId, DirectOrderRequestDto dto) {
        DirectOrderResponseDto data = null;
        int quantity = dto.getQuantity();
        String shippingRequest = dto.getShippingRequest();
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "user"));

            Product product = productRepository.findById(pId)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "product"));

            int totalAmount = (product.getPPrice() * quantity) + 3000;

            Order order = Order.builder()
                    .user(user)
                    .orderStatus(OrderStatus.PENDING)
                    .orderTotalAmount(totalAmount)
                    .shippingRequest(shippingRequest)
                    .orderDate(LocalDate.now())
                    .build();
            orderRepository.save(order);

            List<OrderDetail> orderDetails = new ArrayList<>();
                OrderDetail orderDetail = OrderDetail.builder()
                        .order(order)
                        .product(product)
                        .quantity(quantity)
                        .price(product.getPPrice())
                        .totalPrice(quantity * product.getPPrice())
                        .build();
                orderDetailRepository.save(orderDetail);
                orderDetails.add(orderDetail);

            data = new DirectOrderResponseDto(order, orderDetails);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<OrderDetailResponseDto> getOrder(String username, OrderGetRequestDto dto) {
        OrderDetailResponseDto data = null;

        LocalDate startOrderDate = dto.getStartOrderDate();
        LocalDate endOrderDate = dto.getEndOrderDate();
        try {
                List<OrderDetail> orderDetails;

                if (startOrderDate == null && endOrderDate == null) {
                    orderDetails = orderDetailRepository.findAllByOrder_User_Username(username);
                } else {
                    orderDetails = orderDetailRepository.findAllByUser_usernameAndStartAndEnd(username, startOrderDate, endOrderDate);
                }

            data = new OrderDetailResponseDto(orderDetails);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<OrderCancelResponseDto> cancelOrder(String username, Long orderDetailId) {
        OrderCancelResponseDto data = null;
        try {
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "orderDetail"));
            if (orderDetail.getOrder().getOrderStatus().equals(OrderStatus.CANCELLED)) {
                return ResponseDto.setFailed(ResponseMessage.EXIST_DATA + "cancel");
            }
            orderDetail.getOrder().setOrderStatus(OrderStatus.CANCELLED);

            orderRepository.save(orderDetail.getOrder());
            data = new OrderCancelResponseDto(orderDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }
}
