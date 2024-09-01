package com.management.leaderspace.Services.Subscriber;

import com.management.leaderspace.Entities.Admin;
import com.management.leaderspace.Entities.Subscriber;
import com.management.leaderspace.Entities.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface SubscriberService {
    Subscriber getProfile();
    Page<Visit> getAllVisits(Pageable pageable);



    Page<Visit> getVisitsByDate(LocalDate dateDebut, LocalDate dateFin,Pageable pageable);
}
