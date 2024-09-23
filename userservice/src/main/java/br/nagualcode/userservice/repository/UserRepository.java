package br.nagualcode.userservice.repository;

import br.nagualcode.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
