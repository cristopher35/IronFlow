package com.ironflow.accesservice.client;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class MembershipClient {

    private final WebClient webClient;

    public MembershipClient(@Value("${MEMBERSHIP_SERVICE_URL:http://localhost:8082/membership-app}") String membershipServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(membershipServiceUrl).build();
    }

    public Object getPlanMiembroByMiembroId(Long miembroId) {
        return webClient.get()
                .uri("/api/planes-miembros/miembro/{miembroId}", miembroId)
                .retrieve()
                .bodyToMono(Object.class)
                .timeout(Duration.ofSeconds(5))
                .block();
    }
}
