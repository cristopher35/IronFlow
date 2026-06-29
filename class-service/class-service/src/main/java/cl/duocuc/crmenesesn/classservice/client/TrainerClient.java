package cl.duocuc.crmenesesn.classservice.client;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.NoSuchElementException;

@Component
public class TrainerClient {

    private final WebClient webClient;

    public TrainerClient(@Value("${services.trainer.base-url:http://localhost:8086/trainer-app}") String trainerServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(trainerServiceUrl).build();
    }

    public Object getTrainerById(Long id) {
        return webClient.get()
                .uri("/api/entrenadores/{id}", id)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        response -> Mono.error(new NoSuchElementException("Entrenador no encontrado: " + id)))
                .onStatus(status -> status.is5xxServerError(),
                        response -> Mono.error(new IllegalStateException("trainer-service no está disponible")))
                .bodyToMono(Object.class)
                .block(Duration.ofSeconds(5));
    }
}
