package br.nagualcode.trackingservice.repository;

import br.nagualcode.trackingservice.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Package, Long> {
    Package findByTrackingNumber(String trackingNumber);
}
