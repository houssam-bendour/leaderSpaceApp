package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.SnacksAndBoissonsHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SnacksAndBoissonsHistoryRepository extends JpaRepository<SnacksAndBoissonsHistory, UUID> {
}
