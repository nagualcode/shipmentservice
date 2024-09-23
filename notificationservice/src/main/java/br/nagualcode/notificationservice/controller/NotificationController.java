package br.nagualcode.notificationservice.controller;

import br.nagualcode.notificationservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/{trackingNumber}")
    public ResponseEntity<String> sendNotification(@PathVariable String trackingNumber) {
        String message = notificationService.sendNotification(trackingNumber);
        return ResponseEntity.ok(message);
    }
}
