package com.ironflow.gateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "app.security.enabled=true",
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
class SecurityConfigTest {

    @LocalServerPort
    int port;

    @Test
    @DisplayName("Debe responder 401 cuando no hay credenciales")
    void rechazaSolicitudSinCredenciales() throws Exception {
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request("/branch-app/api/sucursales").build(), HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    @DisplayName("Debe responder 403 cuando el rol no tiene permiso")
    void rechazaSolicitudConRolInsuficiente() throws Exception {
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request("/branch-app/api/sucursales")
                                .header("Authorization", basicAuth("socio", "socio123"))
                                .build(),
                        HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(403);
    }

    @Test
    @DisplayName("Debe conservar X-Request-Id si el cliente lo envia")
    void conservaRequestIdEntrante() throws Exception {
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request("/actuator/health")
                                .header("X-Request-Id", "defensa-ironflow-001")
                                .build(),
                        HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("X-Request-Id")).contains("defensa-ironflow-001");
    }

    @Test
    @DisplayName("Debe generar X-Request-Id cuando el cliente no lo envia")
    void generaRequestIdCuandoNoExiste() throws Exception {
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request("/actuator/health").build(), HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("X-Request-Id")).isPresent();
        assertThat(response.headers().firstValue("X-Request-Id").orElse("")).isNotBlank();
    }

    private HttpRequest.Builder request(String path) {
        return HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET();
    }

    private String basicAuth(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
}
