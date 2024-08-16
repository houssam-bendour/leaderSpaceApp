package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReceptionistRepository extends JpaRepository<Receptionist, UUID> {
    Receptionist findByEmail(String email);
}
