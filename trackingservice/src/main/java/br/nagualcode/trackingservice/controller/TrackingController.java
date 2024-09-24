package br.nagualcode.trackingservice.controller;

import br.nagualcode.trackingservice.model.Package;
import br.nagualcode.trackingservice.repository.PackageRepository;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import br.nagualcode.trackingservice.exception.PackageNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/packages")
public class TrackingController {

    private final PackageRepository packageRepository;

    public TrackingController(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @GetMapping
    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    @PostMapping
    public Package createPackage(@RequestBody Package pack) {
        return packageRepository.save(pack);
    }

    @PutMapping("/{trackingNumber}")
    public Package updatePackageStatus(@PathVariable String trackingNumber, @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        return packageRepository.findByTrackingNumber(trackingNumber)
                .map(pack -> {
                    pack.setStatus(status);
                    return packageRepository.save(pack);
                })
                .orElseThrow(() -> new PackageNotFoundException("Package not found"));
    }


    @GetMapping("/{trackingNumber}/status")
    public String getPackageStatus(@PathVariable String trackingNumber) {
        return packageRepository.findByTrackingNumber(trackingNumber)
                .map(Package::getStatus)
                .orElseThrow(() -> new RuntimeException("Package not found"));
    }

    @DeleteMapping("/{trackingNumber}")
    @Transactional
    public void deletePackage(@PathVariable String trackingNumber) {
        packageRepository.deleteByTrackingNumber(trackingNumber);
    }
}
