package com.management.leaderspace.Services.Subscriber;

import com.management.leaderspace.Entities.Subscriber;
import com.management.leaderspace.Entities.Visit;
import com.management.leaderspace.Repositories.SubscriberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class SubscriberServiceImp implements SubscriberService{
    SubscriberRepository subscriberRepository;
    @Override
    public Subscriber getProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return (Subscriber) subscriberRepository.findByEmail(username);
    }

    @Override
    public List<Visit> getAllVisits() {
        return subscriberRepository.getVisits(getProfile().getId());
    }

    @Override
    public List<Visit> getVisitsByDate(LocalDate dateDebut, LocalDate dateFin) {

        return subscriberRepository.getVisitsByDate(getProfile().getId(),dateDebut,dateFin);
    }


}
