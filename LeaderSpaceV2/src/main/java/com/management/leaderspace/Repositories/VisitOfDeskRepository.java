package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.VisitOfDesk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.JpaEntityGraph;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface VisitOfDeskRepository extends JpaRepository<VisitOfDesk, UUID> {
    @Query("select v from VisitOfDesk v where v.day >= :day  order by v.StartTime DESC")
    List<VisitOfDesk> getVisitsTodayOfDesk(@Param("day") LocalDate localDate);

    @Query("select vd from VisitOfDesk vd where vd.day>= :dateDebut and vd.day<= :dateFin order by vd.day desc ,vd.StartTime desc ")
    List<VisitOfDesk> visitsOfTodayOfDesk(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    @Query("select v from VisitOfDesk v order by v.day DESC, v.StartTime DESC")
    Page<VisitOfDesk> getVisitsOfDesk(Pageable pageable);

    @Query("select v from VisitOfDesk v where (v.day = :day and v.StartTime< :dateDebut and v.EndTime> :dateDebut and v.StartTime< :dateFin and v.EndTime> :dateFin) or (v.day = :day and v.StartTime> :dateDebut and v.EndTime> :dateDebut and v.StartTime< :dateFin and v.EndTime< :dateFin) or (v.day = :day and v.StartTime> :dateDebut and v.EndTime> :dateDebut and v.StartTime< :dateFin and v.EndTime> :dateFin) or (v.day = :day and v.StartTime< :dateDebut and v.EndTime> :dateDebut and v.StartTime< :dateFin and v.EndTime< :dateFin) or (v.day = :day and v.StartTime= :dateDebut and v.StartTime< :dateFin and v.EndTime> :dateFin) or (v.day = :day and v.StartTime< :dateDebut and v.EndTime> :dateDebut and v.EndTime= :dateFin) or (v.day = :day and v.StartTime= :dateDebut and v.EndTime= :dateFin)")
    List<VisitOfDesk> getVisitByDayAndStartTime(@Param("day") LocalDate day, @Param("dateDebut") LocalTime startTime, @Param("dateFin") LocalTime endTime);

    @Query("select coalesce(sum(vd.service_suplementaire_price),0.0) from VisitOfDesk vd where vd.day>= :dateDebut and vd.day<= :dateFin")
    Double sommeServiceSupplimentaiePriceOfDisk(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    @Query("select vd.StartTime from VisitOfDesk vd where vd.day= :reservationDate and vd.StartTime > :reservationTime order by vd.StartTime desc ")
    List<LocalTime> maximumTimeAvailabale(@Param("reservationDate") LocalDate reservationDate, @Param("reservationTime") LocalTime reservationTime);

    @Query("select vd from VisitOfDesk vd where vd.day>= :dateDebut and vd.day<= :dateFin order by vd.day desc ,vd.StartTime desc ")
    Page<VisitOfDesk> visitsOfDeskByDayAndPage(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin, Pageable pageable);

    @Query("select coalesce(sum(vd.service_desk_price),0.0) from VisitOfDesk vd where vd.day >= :dateDebut and vd.day <= :dateFin")
    double sumServiceDeskPriceForDeskVisits(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    @Query("select coalesce(sum(sbv.selling_price*sbv.quantity),0.0) from SnacksAndBoissonsOfVisit sbv , VisitOfDesk vd where sbv.visitOfDesk=vd  and vd.day >= :dateDebut and vd.day <= :dateFin")
    double sumSnacksAndBoissonsOfVisitsForDeskVisits(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);



}
