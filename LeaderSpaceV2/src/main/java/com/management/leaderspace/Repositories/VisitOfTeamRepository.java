package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.VisitOfTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    @Query("select v from VisitOfTeam v where v.utilisateur.id = :id and v.day = :day and v.EndTime is null")
    VisitOfTeam getByDateAndUserAndEndTime(@Param("id") UUID id, @Param("day") LocalDate localDate);

    @Query("select vt from VisitOfTeam vt where vt.day>= :dateDebut and vt.day<= :dateFin and (vt.service_suplementaire_price!=0 or vt.snacksAndBoissonsOfVisits is not empty ) order by vt.day desc, vt.StartTime desc ")
    Page<VisitOfTeam> allVisitOfTeamByDayAndPage(@Param("dateDebut")LocalDate dateDebut, @Param("dateFin")LocalDate dateFin, Pageable pageable);

    @Query("select coalesce(sum(sbv.selling_price*sbv.quantity),0.0) from SnacksAndBoissonsOfVisit sbv , VisitOfTeam vt where sbv.visitOfTeam=vt and vt.day >= :dateDebut and vt.day <= :dateFin")
    double sumSnacksAndBoissonsOfVisitsForTeamVisits(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);



}
