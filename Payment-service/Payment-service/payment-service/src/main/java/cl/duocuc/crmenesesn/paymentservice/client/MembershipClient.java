package cl.duocuc.crmenesesn.paymentservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MembershipClient {

    private final RestTemplate restTemplate;
    private static final String MEMBERSHIP_SERVICE_URL = "http://localhost:8082";

    public MembershipClient() {
        this.restTemplate = new RestTemplate();
    }

    public Object getPlanMiembroByMiembroId(Long miembroId) {
        String url = MEMBERSHIP_SERVICE_URL + "/api/planes-miembros/miembro/" + miembroId;
        return restTemplate.getForObject(url, Object.class);
    }
}