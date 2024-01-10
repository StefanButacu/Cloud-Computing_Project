package com.auth.servies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
    Optional<Credentials> findUserByUsername(String username);
}
