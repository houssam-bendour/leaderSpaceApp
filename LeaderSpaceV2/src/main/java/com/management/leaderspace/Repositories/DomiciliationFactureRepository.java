package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.DomiciliationFacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.UUID;

public interface DomiciliationFactureRepository extends JpaRepository<DomiciliationFacture, UUID> {
    @Query("SELECT f FROM DomiciliationFacture f WHERE YEAR(f.date) = :year")
    List<DomiciliationFacture> findByYear(@Param("year") int year);
}
