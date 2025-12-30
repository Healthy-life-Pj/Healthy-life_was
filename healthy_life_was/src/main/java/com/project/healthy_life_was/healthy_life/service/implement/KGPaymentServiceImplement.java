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

        String status = String.valueOf(pay.get("status"));
        if (!"paid".equalsIgnoreCase(status)) return ApiResponseDto.fail("PG_NOT_PAID");

        String pgProvider = str(pay.get("pg_provider"));
        String payMethod  = str(pay.get("pay_method"));
        String merchantUidFromPG = str(pay.get("merchant_uid"));
        String pgTid     = str(pay.get("pg_tid"));
        String approval  = str(pay.get("apply_num"));
        String receipt   = str(pay.get("receipt_url"));

        long pgAmount = num(pay.get("amount"));

        if (pgProvider == null || !pgProvider.toLowerCase().contains("inicis")) {
            return ApiResponseDto.fail("NOT_INICIS");
        }
        if (!"card".equalsIgnoreCase(payMethod)) {
            return ApiResponseDto.fail("INVALID_PAY_METHOD");
        }

//        Order order = orderRepository.findByOrderCode(req.getMerchantUid())
//                .orElse(null);
//        if (order == null) return ApiResponseDto.fail("ORDER_NOT_FOUND");

        if (!req.getMerchantUid().equals(merchantUidFromPG)) {
            return ApiResponseDto.fail("MERCHANT_UID_MISMATCH");
        }

        // 프론트에서 보낸 expectedAmount와 실제 결제 금액 비교만
        long expectedAmount = num(pay.get("custom_data.expectedAmount"));
        if (pgAmount != expectedAmount) {
            return ApiResponseDto.fail("AMOUNT_MISMATCH");
        }


        Map<String, Object> data = new HashMap<>();
        data.put("imp_uid", req.getImpUid());
        data.put("merchant_uid", req.getMerchantUid());
        data.put("pg_tid", pgTid);
        data.put("approvalNo", approval);
        data.put("receipt", receipt);
        data.put("amount", pgAmount);
        data.put("pg_provider", pgProvider);
        data.put("pay_method", payMethod);

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
