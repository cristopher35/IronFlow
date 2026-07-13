package cl.duocuc.crmenesesn.paymentservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PaymentServiceApplicationTests {

    @Test
    @DisplayName("Debe cargar el contexto de payment-service")
    void contextLoads() {
    }

}
