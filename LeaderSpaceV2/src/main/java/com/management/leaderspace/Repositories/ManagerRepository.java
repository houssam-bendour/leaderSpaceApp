package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ManagerRepository extends JpaRepository<Manager, UUID> {
    Manager findByEmail(String email);
}
