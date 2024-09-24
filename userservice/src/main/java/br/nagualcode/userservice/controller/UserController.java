package br.nagualcode.userservice.controller;

import br.nagualcode.userservice.model.User;
import br.nagualcode.userservice.model.Package;
import br.nagualcode.userservice.repository.UserRepository;
import br.nagualcode.userservice.repository.PackageRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PackageRepository packageRepository;

    public UserController(UserRepository userRepository, PackageRepository packageRepository) {
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail(userDetails.getEmail());
                    user.setPackages(userDetails.getPackages());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @PostMapping("/{id}/packages")
    public User addPackageToUser(@PathVariable Long id, @RequestBody Package pack) {

   
        if (packageRepository.existsByTrackingNumber(pack.getTrackingNumber())) {
            throw new RuntimeException("Package with tracking number " + pack.getTrackingNumber() + " already exists.");
        }

        return userRepository.findById(id)
                .map(user -> {
                    pack.setUser(user);  
                    packageRepository.save(pack);  
                    user.getPackages().add(pack); 
                    return userRepository.save(user);  
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @DeleteMapping("/{id}/packages/{trackingNumber}")
    public User removePackageFromUser(@PathVariable Long id, @PathVariable String trackingNumber) {
        return userRepository.findById(id)
                .map(user -> {
                    
                    user.getPackages().removeIf(p -> p.getTrackingNumber().equals(trackingNumber));
                    return userRepository.save(user);  
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
