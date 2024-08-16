package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.SnacksAndBoissons;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SnacksAndBoissonsRepository extends JpaRepository<SnacksAndBoissons, UUID> {
    List<SnacksAndBoissons> findByName(String name);
}
