package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContratRepository extends JpaRepository<Contrat, UUID> {
}
