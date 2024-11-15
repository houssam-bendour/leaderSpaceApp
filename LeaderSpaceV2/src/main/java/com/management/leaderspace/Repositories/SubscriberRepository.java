package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Subscriber;
import com.management.leaderspace.Entities.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
    Subscriber findByEmail(String email);

    @Query("select s from Subscriber s where s.first_name like concat('%',:name,'%') or s.last_name like concat('%',:name,'%')")
    Page<Subscriber> getSubscribersByName(@Param("name") String name, Pageable pageable);

    @Query("select v from  Visit v where v.subscriber.id = :id and v.day >= :date_debut and v.day <= :date_fin order by v.day desc , v.StartTime desc")
    Page<Visit> getVisitsByDate(@Param("id") UUID id,@Param("date_debut") LocalDate date_debut,@Param("date_fin") LocalDate date_fin, Pageable pageable);

    @Query("select v from  Visit v where v.subscriber.id = :id order by v.day desc , v.StartTime desc ")
    Page<Visit> getVisits(@Param("id") UUID id,Pageable pageable);


    @Query("select s from Subscriber s order by s.date_debut desc ")
    Page<Subscriber> getAllSubscriberOrdered(Pageable pageable);

    @Query("select s from Subscriber s where s.date_fin= :date_fin")
    List<Subscriber> SubscriptionAboutToExpire(@Param("date_fin") LocalDate localDate);

    @Query("select s from Subscriber s where s.date_fin > :date and s.number_of_visits<= 3 and s.sendEmail= false ")
    List<Subscriber> VisitsAreAboutToExpire(@Param("date") LocalDate localDate);

}
