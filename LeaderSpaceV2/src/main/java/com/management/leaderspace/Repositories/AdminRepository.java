package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Admin findByEmail(String email);
}
