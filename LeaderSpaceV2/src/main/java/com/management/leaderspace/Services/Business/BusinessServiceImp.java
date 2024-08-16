package com.management.leaderspace.Services.Business;

import com.management.leaderspace.Entities.Business;
import com.management.leaderspace.Entities.Subscriber;
import com.management.leaderspace.Repositories.BusinessRepository;
import com.management.leaderspace.Repositories.SubscriberRepository;
import com.management.leaderspace.Services.Subscriber.SubscriberService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Service
@AllArgsConstructor
public class BusinessServiceImp implements BusinessService {
    BusinessRepository businessRepository;
    @Override
    public Business getProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return (Business) businessRepository.findByEmail(username);
    }









}
