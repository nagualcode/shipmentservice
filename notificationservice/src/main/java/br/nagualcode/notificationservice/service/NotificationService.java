package br.nagualcode.notificationservice.service;

import br.nagualcode.notificationservice.client.TrackingClient;
import br.nagualcode.notificationservice.model.Package;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final TrackingClient trackingClient;

    public NotificationService(TrackingClient trackingClient) {
        this.trackingClient = trackingClient;
    }

    public String sendNotification(String trackingNumber) {
        Package pkg = trackingClient.getPackageByTrackingNumber(trackingNumber);
        if (pkg != null) {
            return "Notification sent for package: " + pkg.getTrackingNumber() + " with status: " + pkg.getStatus();
        }
        return "Package not found!";
    }
}
