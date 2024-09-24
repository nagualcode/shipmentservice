package br.nagualcode.trackingservice.repository;

import br.nagualcode.trackingservice.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package, Long> {

 
    Optional<Package> findByTrackingNumber(String trackingNumber);

 
    void deleteByTrackingNumber(String trackingNumber);
}
