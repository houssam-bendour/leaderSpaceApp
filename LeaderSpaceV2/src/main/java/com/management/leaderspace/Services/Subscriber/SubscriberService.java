package com.management.leaderspace.Services.Subscriber;

import com.management.leaderspace.Entities.Admin;
import com.management.leaderspace.Entities.Subscriber;
import com.management.leaderspace.Entities.Visit;

import java.time.LocalDate;
import java.util.List;

public interface SubscriberService {
    Subscriber getProfile();
    List<Visit> getAllVisits();



    List<Visit> getVisitsByDate(LocalDate dateDebut, LocalDate dateFin);
}
