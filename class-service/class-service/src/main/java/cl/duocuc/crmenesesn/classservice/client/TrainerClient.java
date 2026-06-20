package cl.duocuc.crmenesesn.classservice.client;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

@Component
public class TrainerClient {

    private final RestTemplate restTemplate;
    private final String trainerServiceUrl;

    public TrainerClient(@Value("${services.trainer.base-url:http://localhost:8086/trainer-app}") String trainerServiceUrl) {
        this.restTemplate = new RestTemplate();
        this.trainerServiceUrl = trainerServiceUrl;
    }

    public Object getTrainerById(Long id) {
        String url = trainerServiceUrl + "/api/entrenadores/" + id;
        return restTemplate.getForObject(url, Object.class);
    }
}
