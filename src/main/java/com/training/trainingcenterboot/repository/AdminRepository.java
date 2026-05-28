package com.training.trainingcenterboot.repository;

import com.training.trainingcenterboot.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByEmail(String email);

    Optional<Admin> findByUserUsername(String username);
}