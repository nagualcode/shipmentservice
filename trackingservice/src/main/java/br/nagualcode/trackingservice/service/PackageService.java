package br.nagualcode.trackingservice.service;

import br.nagualcode.trackingservice.model.Package;
import br.nagualcode.trackingservice.repository.PackageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PackageService {

    private final PackageRepository packageRepository;

    public PackageService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public Optional<Package> findByTrackingNumber(String trackingNumber) {
        return Optional.ofNullable(packageRepository.findByTrackingNumber(trackingNumber));
    }

    public Package savePackage(Package pkg) {
        return packageRepository.save(pkg);
    }

    public Package updatePackageStatus(String trackingNumber, String status) {
        Package pkg = packageRepository.findByTrackingNumber(trackingNumber);
        if (pkg != null) {
            pkg.setStatus(status);
            return packageRepository.save(pkg);
        }
        return null;
    }
}
