package com.ironflow.equipmentservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI equipmentOpenApi() {
        return new OpenAPI().info(new Info().title("IronFlow - Equipment Service").version("1.0")
                .description("Inventario y mantenimiento de equipamiento"));
    }
}
