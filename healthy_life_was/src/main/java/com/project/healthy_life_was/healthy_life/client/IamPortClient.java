package com.project.healthy_life_was.healthy_life.client;

import lombok.RequiredArgsConstructor;
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
                .map(res -> {
                    Map<?, ?> response = (Map<?, ?>) res.get("response");
                    return response.get("access_token").toString();
                });
    }

    public Mono<Map<String, Object>> getPayment(String token, String impUid) {
        return
    }
}
