package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.shipping.response.ShippingResponseDto;
import com.project.healthy_life_was.healthy_life.entity.order.Order;
import com.project.healthy_life_was.healthy_life.entity.shipping.Shipping;
import com.project.healthy_life_was.healthy_life.entity.shipping.ShippingStatus;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.repository.OrderRepository;
import com.project.healthy_life_was.healthy_life.repository.ShippingRepository;
import com.project.healthy_life_was.healthy_life.repository.UserRepository;
import com.project.healthy_life_was.healthy_life.service.ShippingService;
import com.sun.jdi.InternalException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShippingServiceImplement implements ShippingService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ShippingRepository shippingRepository;

    @Override
    @Transactional
    public ResponseDto<ShippingResponseDto> createShipping(Long orderId) {
        ShippingResponseDto data = null;

        if (orderId == null || orderId <= 0) {
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "orderId");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_ORDER));

        if (shippingRepository.findByOrderOrderId(orderId).isPresent()) {
            return ResponseDto.setFailed(ResponseMessage.ALREADY_EXIST_SHIPPING);
        }

        // 임시로 만든 tracking Num _ 8자리 숫자
        int trackingNum = (int) (10000000 + Math.random() * 90000000);

        if (shippingRepository.findByTrackingNum(trackingNum).isPresent()) {
            trackingNum = (int) (10000000 + Math.random() * 90000000);
        }

        Shipping shipping = Shipping.builder()
                .order(order)
                .shippingShippedAt(LocalDateTime.now())
                .shippingStatus(ShippingStatus.PENDING)
                .shippingTrackingNum(trackingNum)
                .build();

        shipping = shippingRepository.save(shipping);

        data = new ShippingResponseDto(shipping);

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<ShippingResponseDto> findShippingByTrackingNumber(int trackingNum) {
        ShippingResponseDto data = null;

        Shipping shipping = shippingRepository.findByTrackingNum(trackingNum)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_SHIPPING));

        data = new ShippingResponseDto(shipping);

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<List<ShippingResponseDto>> getUserShipping(String username) {
        List<ShippingResponseDto> data = null;

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_USER));

        data = shippingRepository.findByUserId(user.getUserId())
                .stream()
                .map(ShippingResponseDto::new)
                .toList();

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<ShippingResponseDto> findShippingByOrderId(String username, Long orderId) {
        ShippingResponseDto data = null;

        Optional<Shipping> optionalShipping = shippingRepository.findByOrderOrderId(orderId);

        if (optionalShipping.isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_ORDER);
        }

        data = new ShippingResponseDto(optionalShipping.get());

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }
}