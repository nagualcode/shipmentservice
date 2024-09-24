package br.nagualcode.userservice.controller;

import br.nagualcode.userservice.model.User;
import br.nagualcode.userservice.model.Package;
import br.nagualcode.userservice.repository.UserRepository;
import feign.FeignException;
import br.nagualcode.userservice.repository.PackageRepository;
import br.nagualcode.userservice.client.TrackingServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PackageRepository packageRepository;
    private final TrackingServiceClient trackingServiceClient;

    public UserController(UserRepository userRepository, PackageRepository packageRepository, TrackingServiceClient trackingServiceClient) {
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
        this.trackingServiceClient = trackingServiceClient;
    }

 
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserWithPackageStatus(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.getPackages().forEach(pkg -> {
                try {
                    String status = trackingServiceClient.getStatus(pkg.getTrackingNumber());
                    pkg.setStatus(status);
                } catch (FeignException e) {
                    // Log detailed Feign client error
                    System.err.println("Error calling TrackingService for package: " + pkg.getTrackingNumber());
                    System.err.println("Feign Error: " + e.getMessage());
                    pkg.setStatus("Status indisponível");
                }
            });
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



   
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(updatedUser.getEmail());
            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }



   
    @PostMapping("/{id}/packages")
    public ResponseEntity<Package> addPackageToUser(@PathVariable Long id, @RequestBody Package pkg) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!packageRepository.existsByTrackingNumber(pkg.getTrackingNumber())) {
                pkg.setUser(user);
                packageRepository.save(pkg);
                user.getPackages().add(pkg);
                userRepository.save(user);
                return ResponseEntity.ok(pkg);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/packages/{trackingNumber}")
    public ResponseEntity<Void> removePackageFromUser(@PathVariable Long id, @PathVariable String trackingNumber) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Package pkgToRemove = user.getPackages().stream()
                    .filter(pkg -> pkg.getTrackingNumber().equals(trackingNumber))
                    .findFirst()
                    .orElse(null);

            if (pkgToRemove != null) {
                user.getPackages().remove(pkgToRemove);
                packageRepository.delete(pkgToRemove);
                userRepository.save(user);

                return ResponseEntity.ok().build();
            } else {
             
                return ResponseEntity.notFound().build();
            }
        } else {
          
            return ResponseEntity.notFound().build();
        }
    }


}
