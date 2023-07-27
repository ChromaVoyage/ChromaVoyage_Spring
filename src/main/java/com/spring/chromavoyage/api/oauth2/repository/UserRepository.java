package com.spring.chromavoyage.api.oauth2.repository;

import com.spring.chromavoyage.api.oauth2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    // SELECT * FROM user WHERE username = ?1
    User findByUsername(String username);

    // SELECT * FROM user WHERE provider = ?1 and email = ?2
    Optional<User> findByProviderAndEmail(String provider, String email);
    Optional<User> findByEmail(String email);
}