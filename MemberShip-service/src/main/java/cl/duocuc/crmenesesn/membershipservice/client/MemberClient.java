package cl.duocuc.crmenesesn.membershipservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MemberClient {

    private final RestTemplate restTemplate;

    @Value("${member.service.url:http://localhost:8081}")
    private String memberServiceUrl;

    public MemberClient() {
        this.restTemplate = new RestTemplate();
    }

    public Object getMemberById(Long id) {
        String url = memberServiceUrl + "/api/members/" + id;
        return restTemplate.getForObject(url, Object.class);
    }
}