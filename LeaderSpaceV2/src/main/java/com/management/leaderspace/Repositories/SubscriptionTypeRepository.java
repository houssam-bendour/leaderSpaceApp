package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionType, UUID> {
//    @Query("select sb from SubscriptionType sb where sb.duration = :l and sb.serviceType.name like :s")
//    SubscriptionType getBYNameAnsDuration(String s, long l);
}
