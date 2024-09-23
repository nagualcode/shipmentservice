package br.nagualcode.notificationservice.client;

import br.nagualcode.notificationservice.model.Package;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "trackingservice", url = "${TRACKING_SERVICE_URL}")
public interface TrackingClient {

    @GetMapping("/api/packages/{trackingNumber}")
    Package getPackageByTrackingNumber(@PathVariable("trackingNumber") String trackingNumber);
}
