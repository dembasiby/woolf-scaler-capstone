package com.dembasiby.user;

import com.dembasiby.user.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByIdAndIsDeletedFalse(Long id);
    Page<AppUser> findAllByIsDeletedFalse(Pageable pageable);
    Optional<AppUser> findByEmailAndIsDeletedFalse(String email);
    Optional<AppUser> findBySocialProfileAndIsDeletedFalse(String socialProfile);
}
