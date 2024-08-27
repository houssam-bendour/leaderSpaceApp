package com.management.leaderspace.Repositories;
import com.management.leaderspace.Entities.Caisse;
import com.management.leaderspace.Entities.VisitOfDesk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CaisseRepository extends JpaRepository<Caisse, UUID> {

    @Query("SELECT c FROM Caisse c ORDER BY c.date DESC, c.time DESC")
    Page<Caisse> findTopByOrderByDateTimeDesc(Pageable pageable);

    @Query("SELECT c FROM Caisse c ORDER BY c.date DESC, c.time DESC")
    List<Caisse> findTopByOrderByDateTimeDesc();

    @Query("SELECT d FROM Caisse d WHERE d.date >= :dateDebut AND d.date <= :dateFin ORDER BY d.date DESC")
    Page<Caisse> filterCaisseByDate(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin,Pageable pageable);
}
