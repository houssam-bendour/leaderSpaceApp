package com.management.leaderspace.Services.Admin;

import com.management.leaderspace.Entities.Admin;
import com.management.leaderspace.Entities.Manager;
import com.management.leaderspace.Repositories.AdminRepository;
import com.management.leaderspace.Repositories.ManagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@AllArgsConstructor
public class AdminServiceImp implements AdminService{
    private final ManagerRepository managerRepository;
    AdminRepository adminRepository;
    PasswordEncoder passwordEncoder;
    @Override
    public Admin getProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        Admin admin=adminRepository.findByEmail(username);
        if (admin.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(admin.getImage());
            admin.setBase64Image(base64Image);
        }else
            admin.setBase64Image("https://cdn.pixabay.com/photo/2017/08/06/21/01/louvre-2596278_960_720.jpg");
        return admin;
    }

    @Override
    public void saveManager(Manager manager) {
        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        managerRepository.save(manager);
    }

    @Override
    public void saveUpdateManager(Manager manager) {
        Manager managerToUpdate = managerRepository.findById(manager.getId()).orElse(null);
        assert managerToUpdate != null;
        managerToUpdate.setCIN(manager.getCIN());
        managerToUpdate.setFirst_name(manager.getFirst_name());
        managerToUpdate.setLast_name(manager.getLast_name());
        managerToUpdate.setEmail(manager.getEmail());
        managerToUpdate.setPhone(manager.getPhone());
        managerToUpdate.setCNSS_number(manager.getCNSS_number());
        if (manager.getPassword() != null && !manager.getPassword().isEmpty()) {
            managerToUpdate.setPassword(passwordEncoder.encode(manager.getPassword()));
        }
        managerRepository.save(managerToUpdate);
    }

    @Override
    public void saveAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
    }

    @Override
    public void saveUpdateAdmin(Admin admin) {
        Admin adminToUpdate = adminRepository.findById(admin.getId()).orElse(null);
        assert adminToUpdate != null;
        adminToUpdate.setCIN(admin.getCIN());
        adminToUpdate.setFirst_name(admin.getFirst_name());
        adminToUpdate.setLast_name(admin.getLast_name());
        adminToUpdate.setEmail(admin.getEmail());
        adminToUpdate.setPhone(admin.getPhone());
        adminToUpdate.setCNSS_number(admin.getCNSS_number());
        if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
            adminToUpdate.setPassword(passwordEncoder.encode(admin.getPassword()));
        }
        adminRepository.save(adminToUpdate);
    }
}
