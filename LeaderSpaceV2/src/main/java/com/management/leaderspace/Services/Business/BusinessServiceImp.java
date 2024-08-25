package com.management.leaderspace.Services.Business;

import com.management.leaderspace.Entities.Business;
import com.management.leaderspace.Repositories.BusinessRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;

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
        Business business = businessRepository.findByEmail(username);
        if (business.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(business.getImage());
            business.setBase64Image("data:image/png;base64,"+base64Image);
        } else
            business.setBase64Image("https://cdn.pixabay.com/photo/2017/08/06/21/01/louvre-2596278_960_720.jpg");
        return business;
    }


}
