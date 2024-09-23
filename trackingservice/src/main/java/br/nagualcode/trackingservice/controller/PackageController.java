package br.nagualcode.trackingservice.controller;

import br.nagualcode.trackingservice.model.Package;
import br.nagualcode.trackingservice.service.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping("/{trackingNumber}")
    public ResponseEntity<Package> getPackageStatus(@PathVariable String trackingNumber) {
        return packageService.findByTrackingNumber(trackingNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Package> createPackage(@RequestBody Package newPackage) {
        Package createdPackage = packageService.savePackage(newPackage);
        return ResponseEntity.ok(createdPackage);
    }

    @PutMapping("/{trackingNumber}/status")
    public ResponseEntity<Package> updatePackageStatus(@PathVariable String trackingNumber, @RequestBody String status) {
        Package updatedPackage = packageService.updatePackageStatus(trackingNumber, status);
        return updatedPackage != null ? ResponseEntity.ok(updatedPackage) : ResponseEntity.notFound().build();
    }
}
