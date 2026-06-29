package com.ironflow.notificationservice.client;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.NoSuchElementException;

@Component
public class MemberClient {

    private final WebClient webClient;

    public MemberClient(@Value("${services.member.base-url:http://localhost:8081/member-app}") String memberServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(memberServiceUrl).build();
    }

    public Object getMemberById(Long id) {
        return webClient.get()
                .uri("/api/members/{id}", id)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        response -> Mono.error(new NoSuchElementException("Miembro no encontrado: " + id)))
                .onStatus(status -> status.is5xxServerError(),
                        response -> Mono.error(new IllegalStateException("member-service no está disponible")))
                .bodyToMono(Object.class)
                .block(Duration.ofSeconds(5));
    }
}
