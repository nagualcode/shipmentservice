package br.nagualcode.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tracking-service", url = "http://trackingservice:8082")
public interface TrackingServiceClient {

    @GetMapping("/packages/{trackingNumber}/status")
    String getStatus(@PathVariable("trackingNumber") String trackingNumber);
}
