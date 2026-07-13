package cl.duocuc.crmenesesn.bookingservice.client;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class ClassClient {

    private final WebClient webClient;

    public ClassClient(@Value("${CLASS_SERVICE_URL:http://localhost:8084/class-app}") String classServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(classServiceUrl).build();
    }

    public Object getHorarioById(Long id) {
        return webClient.get()
                .uri("/api/schedules/{id}", id)
                .retrieve()
                .bodyToMono(Object.class)
                .timeout(Duration.ofSeconds(5))
                .block();
    }

    public Object reservarCupo(Long id) {
        return webClient.patch()
                .uri("/api/schedules/{id}/reservar", id)
                .retrieve()
                .bodyToMono(Object.class)
                .timeout(Duration.ofSeconds(5))
                .block();
    }

    public Object liberarCupo(Long id) {
        return webClient.patch()
                .uri("/api/schedules/{id}/liberar", id)
                .retrieve()
                .bodyToMono(Object.class)
                .timeout(Duration.ofSeconds(5))
                .block();
    }
}
