package cl.duocuc.crmenesesn.paymentservice.config;

import feign.Request;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

public class FeignClientConfig {

    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(2, TimeUnit.SECONDS, 5, TimeUnit.SECONDS, true);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        ErrorDecoder defaultDecoder = new ErrorDecoder.Default();
        return (methodKey, response) -> {
            if (response.status() == 404) {
                return new IllegalArgumentException("Recurso remoto no encontrado en membership-service");
            }
            return defaultDecoder.decode(methodKey, response);
        };
    }
}
