package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.VisitOfTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VisitOfTeamRepository extends JpaRepository<VisitOfTeam, UUID> {

    @Query("select vt from VisitOfTeam vt where vt.day>= :dateDebut and vt.day<= :dateFin and (vt.service_suplementaire_price!=0 or vt.snacksAndBoissonsOfVisits is not empty ) order by vt.day desc, vt.StartTime desc ")
    List<VisitOfTeam> allVisitOfTeamByDate(@Param("dateDebut")LocalDate dateDebut, @Param("dateFin")LocalDate dateFin);

    @Query("select coalesce(sum(vt.service_suplementaire_price),0.0) from VisitOfTeam vt where vt.day>= :dateDebut and vt.day<= :dateFin")
    Double sommeServiceSupplimentaiePriceForTeams(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);


}
