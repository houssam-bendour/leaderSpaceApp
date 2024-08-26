package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Visit;
import com.management.leaderspace.Entities.VisitOfRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface VisitOfRoomRepository extends JpaRepository<VisitOfRoom, UUID> {
    @Query("select v from VisitOfRoom v where v.day >= :day  order by v.StartTime DESC")
    List<VisitOfRoom> getVisitsTodayOfRoom(@Param("day") LocalDate localDate);

    @Query("select v from VisitOfRoom v order by v.day DESC, v.StartTime DESC")
    List<VisitOfRoom> getVisitOfRoom();                                                                                                                                                                                           //>=
    @Query("select v from VisitOfRoom v where (v.day = :day and v.StartTime< :dateDebut and v.EndTime> :dateDebut and v.StartTime< :dateFin and v.EndTime> :dateFin) or (v.day = :day and v.StartTime> :dateDebut and v.EndTime> :dateDebut and v.StartTime< :dateFin and v.EndTime< :dateFin) or (v.day = :day and v.StartTime> :dateDebut and v.EndTime> :dateDebut and v.StartTime< :dateFin and v.EndTime> :dateFin) or (v.day = :day and v.StartTime< :dateDebut and v.EndTime> :dateDebut and v.StartTime< :dateFin and v.EndTime< :dateFin) or (v.day = :day and v.StartTime= :dateDebut and v.StartTime< :dateFin and v.EndTime> :dateFin) or (v.day = :day and v.StartTime< :dateDebut and v.EndTime> :dateDebut and v.EndTime= :dateFin) or (v.day = :day and v.StartTime= :dateDebut and v.EndTime= :dateFin)")
    List<VisitOfRoom> getVisitByDayAndStartTime(@Param("day") LocalDate day,@Param("dateDebut") LocalTime startTime,@Param("dateFin") LocalTime endTime);

    @Query("select coalesce(sum(vr.service_suplementaire_price),0.0) from VisitOfRoom vr where vr.day>= :dateDebut and vr.day<= :dateFin")
    Double sommeServiceSuplimentaireOfVisitRoom(@Param("dateDebut")LocalDate dateDebut, @Param("dateFin")LocalDate dateFin);

    @Query("select vr.StartTime from VisitOfRoom vr where vr.day= :reservationDate and vr.StartTime > :reservationTime order by vr.StartTime desc ")
    List<LocalTime> maximumTimeAvailabale(@Param("reservationDate") LocalDate reservationDate, @Param("reservationTime") LocalTime reservationTime);

    @Query("select vr from VisitOfRoom vr where vr.day>= :dateDebut and vr.day<= :dateFin order by vr.day desc ,vr.StartTime desc ")
    List<VisitOfRoom> findVisitsOfRoomByDate(@Param("dateDebut")LocalDate dateDebut, @Param("dateFin")LocalDate dateFin);

    @Query("select vr from VisitOfRoom vr where vr.day>= :dateDebut and vr.day<= :dateFin order by vr.day desc ,vr.StartTime desc ")
    Page<VisitOfRoom> findVisitsOfRoomByDateAndPage(@Param("dateDebut")LocalDate dateDebut, @Param("dateFin")LocalDate dateFin, Pageable pageable);

    @Query("select sum(vr.service_room_price) from VisitOfRoom vr where vr.day >= :dateDebut and vr.day <= :dateFin")
    double sumServicePriceRoomVisits(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    @Query("select sum(sbv.selling_price*sbv.quantity) from SnacksAndBoissonsOfVisit sbv , VisitOfRoom vr where sbv.visitOfRoom=vr and vr.day >= :dateDebut and vr.day <= :dateFin")
    double sumSnacksAndBoissonsOfVisitsForVisitsRoom(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    @Query("select sum(sbv.selling_price * sbv.quantity) + sum(distinct pr.service_suplementaire_price) " +
            "from VisitOfRoom vr " +
            "join ParticipantOfvisitRoom pr on pr.visitOfRoom = vr " +
            "left join SnacksAndBoissonsOfVisit sbv on sbv.participant = pr " +
            "where vr.day >= :dateDebut and vr.day <= :dateFin")
    double sommeConsommationsForRoomParticipants(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);


}
