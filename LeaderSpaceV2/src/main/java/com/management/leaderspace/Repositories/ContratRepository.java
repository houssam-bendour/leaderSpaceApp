package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ContratRepository extends JpaRepository<Contrat, UUID> {

    @Query("select c from Contrat c where c.date>= :dateDebut and c.date<= :dateFin order by c.date desc ")
    List<Contrat> allContractByDate(@Param("dateDebut") LocalDate dateDebut , @Param("dateFin") LocalDate DateFin);

    @Query("select coalesce(sum(c.montant_de_location_chiffre),0.0) from Contrat c where c.date>= :dateDebut and c.date<= :dateFin")
    double totaleMontantOfContractByDates(LocalDate dateDebut, LocalDate dateFin);


}
