package cl.duocuc.crmenesesn.classservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TrainerClient {

    private final RestTemplate restTemplate;
    private static final String TRAINER_SERVICE_URL = "http://localhost:8086/trainer-app";

    public TrainerClient() {
        this.restTemplate = new RestTemplate();
    }

    public Object getTrainerById(Long id) {
        String url = TRAINER_SERVICE_URL + "/api/entrenadores/" + id;
        return restTemplate.getForObject(url, Object.class);
    }
}