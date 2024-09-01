package com.management.leaderspace.Services.Subscriber;

import com.management.leaderspace.Entities.Subscriber;
import com.management.leaderspace.Entities.Visit;
import com.management.leaderspace.Repositories.SubscriberRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Base64;
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
        Subscriber subscriber =subscriberRepository.findByEmail(username);
        if (subscriber.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(subscriber.getImage());
            subscriber.setBase64Image("data:image/png;base64,"+base64Image);
        }else
            subscriber.setBase64Image("https://cdn.pixabay.com/photo/2017/08/06/21/01/louvre-2596278_960_720.jpg");
        return subscriber;
    }

    @Override
    public Page<Visit> getAllVisits(Pageable pageable) {
        return subscriberRepository.getVisits(getProfile().getId(),pageable);
    }

    @Override
    public Page<Visit> getVisitsByDate(LocalDate dateDebut, LocalDate dateFin, Pageable pageable) {

        return subscriberRepository.getVisitsByDate(getProfile().getId(),dateDebut,dateFin,pageable);
    }


}
