package cl.duocuc.crmenesesn.classservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI classOpenApi() {
        return new OpenAPI().info(new Info().title("IronFlow - Class Service").version("1.0")
                .description("Catálogo de clases y programación de horarios"));
    }
}
