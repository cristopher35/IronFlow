package cl.duocuc.crmenesesn.membershipservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Membership Service API")
                        .version("1.0")
                        .description("Microservicio encargado de la gestión de planes de membresía y su asignación a los miembros del gimnasio IronFlow")
                        .contact(new Contact()
                                .name("Cristopher Meneses")
                                .email("cristopher.meneses@duocuc.cl")));
    }
}