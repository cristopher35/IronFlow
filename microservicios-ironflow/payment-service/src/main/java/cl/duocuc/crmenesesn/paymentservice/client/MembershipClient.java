package cl.duocuc.crmenesesn.paymentservice.client;

import cl.duocuc.crmenesesn.paymentservice.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "membership-service", configuration = FeignClientConfig.class)
public interface MembershipClient {

    @GetMapping("/api/planes-miembros/miembro/{miembroId}")
    List<Object> getPlanMiembroByMiembroId(@PathVariable("miembroId") Long miembroId);
}
