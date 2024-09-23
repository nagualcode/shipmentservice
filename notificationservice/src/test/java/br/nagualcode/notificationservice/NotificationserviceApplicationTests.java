package br.nagualcode.notificationservice;

import br.nagualcode.notificationservice.client.TrackingClient;
import br.nagualcode.notificationservice.model.Package;
import br.nagualcode.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotificationserviceApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TrackingClient trackingClient;

    @Test
    public void testSendNotification() {
        // Simula a criação de um pacote no TrackingService (mock or actual)
        Package pkg = new Package("12345", "Delivered");

        // Envia uma notificação para o pacote
        ResponseEntity<String> response = restTemplate.postForEntity(
            "http://localhost:" + port + "/api/notifications/12345", null, String.class);

        // Verifica a resposta
        assertThat(response.getBody()).contains("Notification sent for package");
    }
}
