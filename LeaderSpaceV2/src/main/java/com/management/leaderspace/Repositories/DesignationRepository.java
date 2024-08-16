package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DesignationRepository extends JpaRepository<Designation, UUID> {
}
