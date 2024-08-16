package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.NotSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotSubscriberRepository extends JpaRepository<NotSubscriber, UUID> {
}
