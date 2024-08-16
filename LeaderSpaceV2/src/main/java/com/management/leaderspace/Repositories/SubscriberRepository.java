package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
    Subscriber findByEmail(String email);
    @Query("select s from Subscriber s where s.first_name like concat('%',:name,'%') or s.last_name like concat('%',:name,'%')")
    List<Subscriber> getSubscribersByName(@Param("name") String name);
}
