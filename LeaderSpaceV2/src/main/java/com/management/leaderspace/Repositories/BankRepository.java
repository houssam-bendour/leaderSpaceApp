package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Bank;
import com.management.leaderspace.Entities.Caisse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BankRepository extends JpaRepository<Bank, UUID> {
    @Query("SELECT b FROM Bank b ORDER BY b.date DESC, b.time DESC")
    List<Bank> findTopByOrderByDateTimeDesc();
}
