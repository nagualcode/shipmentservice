package br.nagualcode.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tracking-service", url = "http://localhost:8082") // URL do TrackingService
public interface TrackingServiceClient {

    @GetMapping("/tracking/{trackingNumber}/status")
    String getStatus(@PathVariable("trackingNumber") String trackingNumber);
}
