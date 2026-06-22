package cl.duocuc.crmenesesn.membershipservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MemberClient {

    private final RestTemplate restTemplate;
    private static final String MEMBER_SERVICE_URL = "http://localhost:8081";
    public MemberClient() {
        this.restTemplate = new RestTemplate();
    }

    public Object getMemberById(Long id) {
        String url = MEMBER_SERVICE_URL + "/api/members/" + id;
        return restTemplate.getForObject(url, Object.class);
    }
}