package br.nagualcode.userservice.service;

import br.nagualcode.userservice.client.TrackingServiceClient;
import br.nagualcode.userservice.model.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackageStatusService {

    @Autowired
    private TrackingServiceClient trackingServiceClient;

    public String getPackageStatus(Package pack) {
        try {
            return trackingServiceClient.getStatus(pack.getTrackingNumber());
        } catch (Exception e) {
            return "Status unavailable"; // Graceful handling
        }
    }
}
