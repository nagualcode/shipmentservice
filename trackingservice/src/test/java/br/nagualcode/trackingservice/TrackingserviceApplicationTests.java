package br.nagualcode.trackingservice;

import br.nagualcode.trackingservice.model.Package;
import br.nagualcode.trackingservice.repository.PackageRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(properties = "spring.config.name=application-test")
public class TrackingserviceApplicationTests {

    @Container
    public static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PackageRepository packageRepository;

    @BeforeAll
    public static void setup() {
        System.setProperty("spring.datasource.url", postgresDB.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresDB.getUsername());
        System.setProperty("spring.datasource.password", postgresDB.getPassword());
    }

    @AfterEach
    public void cleanUp() {
        packageRepository.deleteAll();
    }

    @Test
    @DisplayName("Test Create Package")
    public void testCreatePackage() throws Exception {
        String jsonPackage = "{\"trackingNumber\":\"123ABC\",\"status\":\"shipped\"}";

        mockMvc.perform(post("/packages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPackage))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trackingNumber").value("123ABC"))
                .andExpect(jsonPath("$.status").value("shipped"));
    }

    @Test
    @DisplayName("Test Get All Packages")
    public void testGetAllPackages() throws Exception {
        // Add sample data
        Package pack = new Package();
        pack.setTrackingNumber("123ABC");
        pack.setStatus("shipped");
        packageRepository.save(pack);

        mockMvc.perform(get("/packages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trackingNumber").value("123ABC"))
                .andExpect(jsonPath("$[0].status").value("shipped"));
    }

    @Test
    @DisplayName("Test Update Package Status")
    public void testUpdatePackageStatus() throws Exception {
        // Add a sample package
        Package pack = new Package();
        pack.setTrackingNumber("123ABC");
        pack.setStatus("shipped");
        packageRepository.save(pack);

        String updatedStatus = "delivered";
        mockMvc.perform(put("/packages/123ABC")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedStatus))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("delivered"));
    }

    @Test
    @DisplayName("Test Delete Package")
    public void testDeletePackage() throws Exception {
        // Add a sample package
        Package pack = new Package();
        pack.setTrackingNumber("123ABC");
        pack.setStatus("shipped");
        packageRepository.save(pack);

        mockMvc.perform(delete("/packages/123ABC"))
                .andExpect(status().isOk());

      
        mockMvc.perform(get("/packages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
