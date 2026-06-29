package cl.duocuc.crmenesesn.bookingservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI bookingOpenApi() {
        return new OpenAPI().info(new Info()
                .title("IronFlow Booking Service")
                .version("1.0.0")
                .description("API para reservas de clases en IronFlow"));
    }
}
