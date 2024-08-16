package com.management.leaderspace.Services.Admin;

import com.management.leaderspace.Entities.Admin;
import com.management.leaderspace.Repositories.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminServiceImp implements AdminService{
    AdminRepository adminRepository;
    @Override
    public Admin getProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return (Admin) adminRepository.findByEmail(username);
    }
}
