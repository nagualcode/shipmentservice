package br.nagualcode.userservice.repository;

import br.nagualcode.userservice.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Package, Long> {

    boolean existsByTrackingNumber(String trackingNumber);
}
