package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Devis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DevisRepository extends JpaRepository<Devis, UUID> {
    @Query("SELECT d FROM Devis d WHERE YEAR(d.date) = :year")
    List<Devis> findByYear(@Param("year") int year);
}
