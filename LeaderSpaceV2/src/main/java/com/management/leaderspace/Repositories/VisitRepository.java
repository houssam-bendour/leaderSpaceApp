package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Subscriber;
import com.management.leaderspace.Entities.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VisitRepository extends JpaRepository<Visit, UUID> {
    @Query("select v from Visit v where v.subscriber.id=:id")
    List<Visit> deleteByClientId(@Param("id") UUID id);
    @Query("select v from Visit v where v.subscriber.id = :id and v.day = :day and v.EndTime is null")
    Visit getByDateAndSubscriberAndEndTime(@Param("id") UUID id, @Param("day") LocalDate localDate);
    @Query("select v from Visit v where v.subscriber.id = :id and v.day = :day and v.EndTime is not null")
    List<Visit> getByDateAndSubscriberAndEndCheckoutTime(@Param("id") UUID id, @Param("day") LocalDate day);
    @Query("select v from Visit v where v.day = :day order by v.StartTime")
    List<Visit> getVisitsToday(@Param("day") LocalDate localDate);
    @Query("select v from Visit v where v.day = :day and (v.subscriber.last_name like concat('%',:n,'%') or v.subscriber.first_name like concat('%',:n,'%')) order by v.StartTime")
    List<Visit> getVisitsTodayByName(@Param("day") LocalDate localDate,@Param("n") String name);
    @Query("select v from Visit v where v.notSubscriber.id = :id and v.day = :day and v.EndTime is null")
    Visit getByDateAndNotSubscriberAndEndTime(@Param("id") UUID id,@Param("day") LocalDate localDate);
    @Query("select v from Visit v where v.day = :day and v.notSubscriber is null order by v.StartTime DESC")
    List<Visit> getVisitsTodayOfSubscribers(@Param("day")LocalDate localDate);
    @Query("select v from Visit v where v.day = :day and v.subscriber is null order by v.StartTime DESC")
    List<Visit> getVisitsTodayOfNotSubscribers(@Param("day")LocalDate localDate);
    @Query("select v from Visit v where v.day >= :date1 and v.day <= :date2")
    List<Visit> getVisitsBetween(@Param("date1") LocalDate localDate1, @Param("date2") LocalDate localDate2);
    @Query("select v from Visit v where v.subscriber.id = :id and v.day = :day")
    List<Visit> getVisitsTodayOfSubscriberAndDate(@Param("id") UUID id,@Param("day")LocalDate localDate);

    @Query("select v from Visit v where v.day >= :dateDebut and v.day <= :dateFin and (v.service_suplementaire_price != 0 or v.service_price != 0 or v.snacksAndBoissonsOfVisits is not empty) order by v.day desc , v.StartTime desc ")
    List<Visit> getAllVisitsBetweenStartDayAndEndTime(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    @Query("select v from Visit v where v.notSubscriber is null order by v.day DESC, v.StartTime DESC")
    Page<Visit> getVisitsOfSubscribers(Pageable pageable);

    @Query("select v from Visit v where v.subscriber is null order by v.day DESC, v.StartTime DESC")
    Page<Visit> getVisitsOfNotSubscribers(Pageable pageable);

    @Query("select coalesce(sum(v.service_suplementaire_price),0.0) from Visit v where v.day>= :dateDebut and v.day<= :dateFin")
    Double sommeServiePriceSupplementaireOfVisits(@Param("dateDebut") LocalDate dateDebut,@Param("dateFin") LocalDate dateFin);

    @Query("select v from Visit v where v.day >= :dateDebut and v.day <= :dateFin and (v.service_suplementaire_price != 0 or v.service_price != 0 or v.snacksAndBoissonsOfVisits is not empty) order by v.day desc , v.StartTime desc ")
    Page<Visit> listNormaleVisitsByDayAndPage(LocalDate dateDebut, LocalDate dateFin, Pageable pageable);

    @Query("select coalesce(sum(v.service_price),0.0) from Visit v where v.day >= :dateDebut and v.day <= :dateFin")
    double sumServicePriceNormaleVisits(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    @Query("select coalesce(sum(sbv.selling_price*sbv.quantity),0.0) from SnacksAndBoissonsOfVisit sbv , Visit v where sbv.visit=v and v.day >= :dateDebut and v.day <= :dateFin")
    double sumSnacksAndBoissonsOfVisitsForVisits(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

}

