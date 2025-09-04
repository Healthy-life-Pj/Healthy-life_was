package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.client.IamPortClient;
import com.project.healthy_life_was.healthy_life.dto.payment.ApiResponseDto;
import com.project.healthy_life_was.healthy_life.dto.payment.CancelRequestDto;
import com.project.healthy_life_was.healthy_life.dto.payment.VerifyRequestDto;
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

        if (pay == null) {
            return ApiResponseDto.fail("NO_PAYMENT");
        }

        String status = String.valueOf(pay.get("status"));
        long pgAmount = ((Number) pay.get("amount")).longValue();

        if (!"paid".equalsIgnoreCase(status)) {
            return ApiResponseDto.fail("PG_NOT_PAID");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("imp_uid", req.getImpUid());
        data.put("merchant_uid", req.getMerchantUid());
        data.put("pg_tid", String.valueOf(pay.get("pg_tid")));
        data.put("approvalNo", String.valueOf(pay.get("apply_num")));
        data.put("receipt", String.valueOf(pay.get("receipt_url")));
        data.put("amount", pgAmount);

        return ApiResponseDto.ok(data);
    }

    @Transactional
    public ApiResponseDto cancel(CancelRequestDto req) {
        String token = iamPortClient.getAccessToken().block();

        Map<String, Object> body = new HashMap<>();
        if (req.getImpUid() != null && !req.getImpUid().isBlank()) body.put("imp_uid", req.getImpUid());
        if (req.getMerchantUid() != null && !req.getMerchantUid().isBlank()) body.put("merchant_uid", req.getMerchantUid());
        if (req.getAmount() != null) body.put("amount", req.getAmount());
        if (req.getReason() != null && !req.getReason().isBlank()) body.put("reason", req.getReason());

        Map<String, Object> res = iamPortClient.cancel(token, body).block();
        if (res == null) {
            return ApiResponseDto.fail("CANCEL_FAILED");
        }

        return ApiResponseDto.ok(res);
    }
}
