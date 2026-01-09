package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.client.IamPortClient;
import com.project.healthy_life_was.healthy_life.dto.payment.ApiResponseDto;
import com.project.healthy_life_was.healthy_life.dto.payment.CancelRequestDto;
import com.project.healthy_life_was.healthy_life.dto.payment.VerifyRequestDto;
import com.project.healthy_life_was.healthy_life.entity.order.Order;
import com.project.healthy_life_was.healthy_life.repository.OrderRepository;
import com.project.healthy_life_was.healthy_life.service.KGPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class KGPaymentServiceImplement implements KGPaymentService {

    private final IamPortClient iamPortClient;
    private final OrderRepository orderRepository;

    @Transactional
    public ApiResponseDto verify(VerifyRequestDto req) {

        String token = iamPortClient.getAccessToken().block();
        Map<String, Object> pay = iamPortClient.getPayment(token, req.getImpUid()).block();

        if (pay == null) return ApiResponseDto.fail("NO_PAYMENT");

        if (!"paid".equalsIgnoreCase(String.valueOf(pay.get("status")))) {
            return ApiResponseDto.fail("PG_NOT_PAID");
        }

        String merchantUidFromPG = String.valueOf(pay.get("merchant_uid"));
        if (!req.getMerchantUid().equals(merchantUidFromPG)) {
            return ApiResponseDto.fail("MERCHANT_UID_MISMATCH");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("imp_uid", req.getImpUid());
        data.put("merchant_uid", req.getMerchantUid());
        data.put("amount", ((Number) pay.get("amount")).intValue());

        return ApiResponseDto.ok(data);
    }

    private static String str(Object o) { return o == null ? null : String.valueOf(o); }
    private static long num(Object o) {
        if (o instanceof Number n) return n.longValue();
        return (o == null) ? 0L : Long.parseLong(String.valueOf(o));
    }


    @Transactional
    public ApiResponseDto cancel(CancelRequestDto req) {
        String token = iamPortClient.getAccessToken().block();

        Map<String, Object> body = new HashMap<>();
        body.put("imp_uid", req.getImpUid());
        body.put("reason", "주문 취소에 따른 결제 취소");

        Map<String, Object> res = iamPortClient.cancel(token, body).block();

        if (res == null) {
            return ApiResponseDto.fail("CANCEL_FAILED");
        }

        return ApiResponseDto.ok(res);
    }
}
