package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.SnacksAndBoissons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SnacksAndBoissonsRepository extends JpaRepository<SnacksAndBoissons, UUID> {
//    List<SnacksAndBoissons> findByName(String name);
    List<SnacksAndBoissons> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    @Query("select sb from SnacksAndBoissons sb order by sb.name ASC")
    List<SnacksAndBoissons> getAllByNameOrderByNameAsc();
}
