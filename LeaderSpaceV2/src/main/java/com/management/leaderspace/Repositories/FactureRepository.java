package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Devis;
import com.management.leaderspace.Entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FactureRepository extends JpaRepository<Facture, UUID> {
    @Query("SELECT f FROM Facture f WHERE YEAR(f.date) = :year")
    List<Facture> findByYear(@Param("year") int year);
}
