package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Bank;
import com.management.leaderspace.Entities.Caisse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BankRepository extends JpaRepository<Bank, UUID> {
    @Query("SELECT b FROM Bank b ORDER BY b.date DESC, b.time DESC")
    List<Bank> findTopByOrderByDateTimeDesc();

    @Query("SELECT b FROM Bank b WHERE b.somme >= 0 ORDER BY b.date DESC, b.time DESC")
    Page<Bank> findAllFromCaisseToBank(Pageable pageable);

    @Query("SELECT b FROM Bank b WHERE b.somme < 0 ORDER BY b.date DESC, b.time DESC")
    Page<Bank> findAllFromBank(Pageable pageable);

    @Query("SELECT d FROM Bank d WHERE d.somme < 0 AND d.date >= :dateDebut AND d.date <= :dateFin ORDER BY d.date DESC")
    Page<Bank> filterFromBankByDate(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin, Pageable pageable);

    @Query("SELECT d FROM Bank d WHERE d.somme >= 0 AND d.date >= :dateDebut AND d.date <= :dateFin ORDER BY d.date DESC")
    Page<Bank> filterToBankByDate(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin, Pageable pageable);
}
