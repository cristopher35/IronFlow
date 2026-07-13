package cl.duocuc.crmenesesn.membershipservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MembershipServiceApplicationTests {

    @Test
    @DisplayName("Debe cargar el contexto de membership-service")
    void contextLoads() {
    }

}
