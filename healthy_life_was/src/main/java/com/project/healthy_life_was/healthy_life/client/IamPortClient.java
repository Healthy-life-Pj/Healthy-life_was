package com.project.healthy_life_was.healthy_life.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class IamPortClient {

    private final WebClient webClient;
    private final IamPortProperties props;

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    public Mono<String> getAccessToken() {
        return webClient.post()
                .uri("/users/getToken")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "imp_key", props.getRestKey(),
                        "imp_secret", props.getRestSecret()
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(TIMEOUT)
                .map(res -> {
                    Map<?, ?> response = (Map<?, ?>) res.get("response");
                    return response.get("access_token").toString();
                });
    }

    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> getPayment(String token, String impUid) {
        return webClient.get()
                .uri("/payments/{imp_uid}", impUid)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(TIMEOUT)
                .map(root -> (Map<String, Object>) root.get("response"));
    }

    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> cancel(String token, Map<String, Object> body) {
        return webClient.post()
                .uri("/payments/cancel")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(TIMEOUT)
                .map(root -> (Map<String, Object>) root.get("response"));
    }

    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> prepare(String token, String merchantUid, long amount) {
        return webClient.post()
                .uri("/payments/prepare")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("merchant_uid", merchantUid, "amount", amount))
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(TIMEOUT)
                .map(root -> (Map<String, Object>) root.get("response"));
    }
}