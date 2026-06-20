package com.ironflow.trainerservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI trainerOpenApi() {
        return new OpenAPI().info(new Info().title("IronFlow - Trainer Service").version("1.0")
                .description("Gestión de entrenadores del gimnasio"));
    }
}
