package br.nagualcode.trackingservice.repository;

import br.nagualcode.trackingservice.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package, Long> {

    // Find a package by its tracking number
    Optional<Package> findByTrackingNumber(String trackingNumber);

    // Delete a package by its tracking number
    void deleteByTrackingNumber(String trackingNumber);
}
