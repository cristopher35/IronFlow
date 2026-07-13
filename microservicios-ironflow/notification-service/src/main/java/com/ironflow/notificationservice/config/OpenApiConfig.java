package com.ironflow.notificationservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI notificationOpenApi() {
        return new OpenAPI().info(new Info().title("IronFlow - Notification Service").version("1.0")
                .description("Registro y seguimiento de notificaciones"));
    }
}
