package cl.duocuc.crmenesesn.bookingservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClassClient {

    private final RestTemplate restTemplate;
    private static final String CLASS_SERVICE_URL = "http://localhost:8084/class-app";

    public ClassClient() {
        this.restTemplate = new RestTemplate();
    }

    public Object getHorarioById(Long id) {
        String url = CLASS_SERVICE_URL + "/api/schedules/" + id;
        return restTemplate.getForObject(url, Object.class);
    }
}