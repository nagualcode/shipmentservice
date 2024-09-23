package br.nagualcode.trackingservice;

import br.nagualcode.trackingservice.model.Package;
import br.nagualcode.trackingservice.repository.PackageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TrackingserviceApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PackageRepository packageRepository;

    @Test
    public void testGetPackageStatus() {
        Package pkg = new Package("12345", "In Transit");
        packageRepository.save(pkg);

        ResponseEntity<Package> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/api/packages/12345", Package.class);

        assertThat(response.getBody().getStatus()).isEqualTo("In Transit");
    }

    @Test
    public void testUpdatePackageStatus() {
        Package pkg = new Package("12345", "In Transit");
        packageRepository.save(pkg);

        HttpEntity<String> requestEntity = new HttpEntity<>("Delivered");
        restTemplate.exchange("http://localhost:" + port + "/api/packages/12345/status",
                HttpMethod.PUT, requestEntity, Package.class);

        Package updatedPackage = packageRepository.findByTrackingNumber("12345");
        assertThat(updatedPackage.getStatus()).isEqualTo("Delivered");
    }
}
