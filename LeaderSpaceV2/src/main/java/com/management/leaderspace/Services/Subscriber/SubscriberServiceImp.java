package com.management.leaderspace.Services.Subscriber;

import com.management.leaderspace.Entities.Subscriber;
import com.management.leaderspace.Repositories.SubscriberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
}
