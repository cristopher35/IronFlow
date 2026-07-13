package cl.duocuc.crmenesesn.bookingservice.client;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

@Component
public class MemberClient {

    private final RestClient restClient;

    public MemberClient(@Value("${MEMBER_SERVICE_URL:http://localhost:8081}") String memberServiceUrl) {
        this.restClient = RestClient.builder().baseUrl(memberServiceUrl).build();
    }

    public Object getMemberById(Long id) {
        return restClient.get()
                .uri("/api/members/{id}", id)
                .retrieve()
                .body(Object.class);
    }
}
