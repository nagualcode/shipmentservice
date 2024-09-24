package br.nagualcode.userservice;

import br.nagualcode.userservice.model.User;
import br.nagualcode.userservice.model.Package;
import br.nagualcode.userservice.repository.UserRepository;
import br.nagualcode.userservice.repository.PackageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
@Testcontainers
@ExtendWith(SpringExtension.class)
public class UserserviceApplicationTests {

    @Container
    private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("userservice_db")
            .withUsername("postgres")
            .withPassword("password");

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PackageRepository packageRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User("test@example.com", new ArrayList<>());

        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User existingUser = new User("test@example.com", new ArrayList<>());
        User updatedUser = new User("new-email@example.com", new ArrayList<>());

        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new-email@example.com"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        Mockito.doNothing().when(userRepository).deleteById(anyLong());

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddPackageToUser() throws Exception {
        User user = new User("test@example.com", new ArrayList<>());
        Package pack = new Package("TRACK123"); // Construtor corrigido

        // Simula o comportamento do repository para checar se o trackingNumber já existe
        Mockito.when(packageRepository.existsByTrackingNumber("TRACK123")).thenReturn(false);
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(packageRepository.save(any(Package.class))).thenReturn(pack);

        mockMvc.perform(post("/users/{id}/packages", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pack)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testRemovePackageFromUser() throws Exception {
        Package pack = new Package("TRACK123"); // Construtor corrigido
        User user = new User("test@example.com", new ArrayList<>());
        user.getPackages().add(pack);

        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(delete("/users/{id}/packages/{trackingNumber}", 1L, "TRACK123"))
                .andExpect(status().isOk());
    }

}
