package com.project.BidIT.Repo;

import com.project.BidIT.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin, Long> {
    Optional<Admin> findAdminByEmail(String email);

    boolean existsByEmail(String email);
}
