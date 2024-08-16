package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessRepository extends JpaRepository<Business, UUID> {
    Business findByEmail(String email);
}
