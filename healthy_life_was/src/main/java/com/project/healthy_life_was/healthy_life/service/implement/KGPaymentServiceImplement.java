package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.client.IamPortClient;
import com.project.healthy_life_was.healthy_life.dto.payment.ApiResponseDto;
import com.project.healthy_life_was.healthy_life.dto.payment.CancelRequestDto;
import com.project.healthy_life_was.healthy_life.dto.payment.VerifyRequestDto;
import com.project.healthy_life_was.healthy_life.entity.order.Order;
import com.project.healthy_life_was.healthy_life.entity.payment.Payment;
import com.project.healthy_life_was.healthy_life.entity.payment.PaymentMethod;
import com.project.healthy_life_was.healthy_life.repository.OrderRepository;
import com.project.healthy_life_was.healthy_life.repository.PaymentRepository;
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

    @Transactional
    public ApiResponseDto verify(VerifyRequestDto req) {

        String token = iamPortClient.getAccessToken().block();
        Map<String, Object> pay = iamPortClient.getPayment(token, req.getImpUid()).block();
        Map<String, Object> easyPay = (Map<String, Object>) pay.get("easy_pay");
        String provider = String.valueOf(easyPay.get("provider"));

        if (pay == null) return ApiResponseDto.fail("NO_PAYMENT");

        String status = String.valueOf(pay.get("status"));
        if (!"paid".equalsIgnoreCase(status)) {
            return ApiResponseDto.fail("PG_NOT_PAID");
        }

        String merchantUidFromPG = String.valueOf(pay.get("merchant_uid"));
        if (!req.getMerchantUid().equals(merchantUidFromPG)) {
            return ApiResponseDto.fail("MERCHANT_UID_MISMATCH");
        }

        String payMethod = String.valueOf(pay.get("pay_method"));
        PaymentMethod method;

        Map<String, Object> data = new HashMap<>();

        switch (payMethod) {
            case "card":
                method = PaymentMethod.CREDIT_CARD;
                break;

            case "easy_pay":

                if ("kakaopay".equals(provider)) {
                    method = PaymentMethod.KAKAO_PAY;
                } else if ("naverpay".equals(provider)) {
                    method = PaymentMethod.NAVER_PAY;
                } else {
                    method = PaymentMethod.EASY_PAY;
                }
                break;

            default:
                throw new RuntimeException("Unknown pay method: " + payMethod);
        }

        data.put("imp_uid", req.getImpUid());
        data.put("merchant_uid", req.getMerchantUid());
        data.put("amount", ((Number) pay.get("amount")).intValue());
        data.put("pay_method", payMethod);
        data.put("status", status);
        data.put("payment_method_enum", method);

        return ApiResponseDto.ok(data);
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