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
    List<Visit> getVisitsByDate(@Param("id") UUID id,@Param("date_debut") LocalDate date_debut,@Param("date_fin") LocalDate date_fin);
    @Query("select v from  Visit v where v.subscriber.id = :id order by v.day desc , v.StartTime desc ")
    List<Visit> getVisits(@Param("id") UUID id);
}
