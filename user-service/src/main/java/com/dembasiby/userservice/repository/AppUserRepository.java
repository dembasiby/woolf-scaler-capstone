package com.dembasiby.userservice.repository;

import com.dembasiby.userservice.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByIdAndIsDeletedFalse(Long id);
    Optional<AppUser> findByEmailAndIsDeletedFalse(String email);
    boolean existsByEmail(String email);
}
