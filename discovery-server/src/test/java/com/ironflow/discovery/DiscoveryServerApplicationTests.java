package com.ironflow.discovery;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mockStatic;

@SpringBootTest(properties = "server.port=0")
class DiscoveryServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mainDelegatesToSpringApplication() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            String[] args = {"--server.port=0"};

            DiscoveryServerApplication.main(args);

            springApplication.verify(() -> SpringApplication.run(DiscoveryServerApplication.class, args));
        }
    }
}
