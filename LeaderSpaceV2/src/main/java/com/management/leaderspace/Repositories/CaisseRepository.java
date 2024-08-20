package com.management.leaderspace.Repositories;
import com.management.leaderspace.Entities.Caisse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CaisseRepository extends JpaRepository<Caisse, UUID> {

    @Query("SELECT c FROM Caisse c ORDER BY c.date DESC, c.time DESC")
    List<Caisse> findTopByOrderByDateTimeDesc();
}
