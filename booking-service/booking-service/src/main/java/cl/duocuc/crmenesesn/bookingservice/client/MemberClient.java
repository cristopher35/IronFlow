package cl.duocuc.crmenesesn.bookingservice.client;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class MemberClient {

    private final WebClient webClient;

    public MemberClient(@Value("${MEMBER_SERVICE_URL:http://localhost:8081/member-app}") String memberServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(memberServiceUrl).build();
    }

    public Object getMemberById(Long id) {
        return webClient.get()
                .uri("/api/members/{id}", id)
                .retrieve()
                .bodyToMono(Object.class)
                .timeout(Duration.ofSeconds(5))
                .block();
    }
}
