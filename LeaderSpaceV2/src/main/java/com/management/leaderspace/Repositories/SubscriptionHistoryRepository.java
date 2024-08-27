package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Subscriber;
import com.management.leaderspace.Entities.SubscriptionHistory;
import com.management.leaderspace.Entities.SubscriptionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory, UUID> {

    @Query("select sh from SubscriptionHistory sh WHERE sh.startDate>= :start and sh.startDate<= :end order by sh.startDate desc")
    List<SubscriptionHistory> findAllSubscriptionsByDateDebutBetweenStartDayAndEndDay(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("select s from SubscriptionHistory s where s.subscriber = :sub and s.subscriptionType= :subType and s.startDate = :sDate and s.endDate = :eDate")
    SubscriptionHistory getBySubscriber(@Param("subType") SubscriptionType subscriptionType, @Param("sub") Subscriber subscriber, @Param("sDate") LocalDate dateDebut, @Param("eDate") LocalDate dateFin);

    @Query("select sh from SubscriptionHistory sh WHERE sh.startDate>= :start and sh.startDate<= :end order by sh.startDate desc")
    Page<SubscriptionHistory> subscriptionsByDayAndPage(@Param("start") LocalDate start, @Param("end") LocalDate end, Pageable pageable);

    @Query("select coalesce(sum(sh.price),0.0) from SubscriptionHistory sh where sh.startDate >= :dateDebut and sh.startDate <= :dateFin")
    double sumPriceOfSubscriptions (@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

}
