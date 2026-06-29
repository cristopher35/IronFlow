package cl.duocuc.crmenesesn.paymentservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MembershipClient {

    private final RestTemplate restTemplate;

    @Value("${membership.service.url:http://localhost:8082}")
    private String membershipServiceUrl;

    public MembershipClient() {
        this.restTemplate = new RestTemplate();
    }

    public Object getPlanMiembroByMiembroId(Long miembroId) {
        String url = membershipServiceUrl + "/api/planes-miembros/miembro/" + miembroId;
        return restTemplate.getForObject(url, Object.class);
    }
}