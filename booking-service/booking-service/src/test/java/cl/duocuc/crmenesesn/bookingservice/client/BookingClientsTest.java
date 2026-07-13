package cl.duocuc.crmenesesn.bookingservice.client;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BookingClientsTest {

    private HttpServer server;
    private String baseUrl;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/", exchange -> {
            byte[] body = "{\"id\":1}".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
        baseUrl = "http://localhost:" + server.getAddress().getPort();
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    void classClientCallsRemoteScheduleEndpoints() {
        // Given
        ClassClient client = new ClassClient(baseUrl);

        // When / Then
        assertNotNull(client.getHorarioById(10L));
        assertNotNull(client.reservarCupo(10L));
        assertNotNull(client.liberarCupo(10L));
    }

    @Test
    void memberClientCallsRemoteMemberEndpoint() {
        // Given
        MemberClient client = new MemberClient(baseUrl);

        // When / Then
        assertNotNull(client.getMemberById(1L));
    }
}
