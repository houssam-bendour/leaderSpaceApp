package com.management.leaderspace.Controllers;

import com.management.leaderspace.Entities.*;
import com.management.leaderspace.Repositories.AdminRepository;
import com.management.leaderspace.Repositories.ManagerRepository;
import com.management.leaderspace.Services.Admin.AdminService;
import com.management.leaderspace.model.QrCodeGenerator;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
@Transactional
public class AdminController {

    private final ManagerRepository managerRepository;
    private final AdminService adminService;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession httpSession;

    //==============================MANAGER CRUD============================
    @GetMapping("list-managers")
    String getAllManagers(Model model) {
        List<Manager> managers = managerRepository.findAll();
        model.addAttribute("managers", managers);
        return "Admin_espace/managers";
    }

    @GetMapping("add-manager")
    String addManager(Model model) {
        model.addAttribute("manager", new Manager());
        return "Admin_espace/add-manager-form";
    }

    @PostMapping("save-manager")
    public String saveManager(@ModelAttribute Manager manager) {
        adminService.saveManager(manager);
        return "redirect:/admin/list-managers";
    }

    @PostMapping("update-manager")
    String updateManager(@RequestParam("managerId") UUID managerId, Model model) {
        Manager manager = managerRepository.findById(managerId).orElse(null);
        model.addAttribute("manager", manager);
        return "Admin_espace/update-manager-form";
    }

    @PostMapping("save-update-manager")
    public String saveUpdateManager(@ModelAttribute Manager manager) {
        adminService.saveUpdateManager(manager);
        return "redirect:/admin/list-managers";
    }

    @PostMapping("delete-manager")
    String deleteManager(@RequestParam("managerId") UUID managerId) {
        managerRepository.deleteById(managerId);
        return "redirect:/admin/list-managers";
    }

    //==============================ADMIN CRUD============================
    @GetMapping("list-admins")
    String getAllAdmins(Model model) {
        List<Admin> admins = adminRepository.findAll();
        model.addAttribute("admins", admins);
        model.addAttribute("myId", adminService.getProfile().getId());
        return "Admin_espace/admins";
    }

    @GetMapping("add-admin")
    String addAdmin(Model model) {
        model.addAttribute("admin", new Admin());
        return "Admin_espace/add-admin-form";
    }

    @PostMapping("save-admin")
    public String saveAdmin(@ModelAttribute Admin admin) {
        adminService.saveAdmin(admin);
        return "redirect:/admin/list-admins";
    }

    @PostMapping("update-admin")
    String updateAdmin(@RequestParam("adminId") UUID adminId, Model model) {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        model.addAttribute("admin", admin);
        return "Admin_espace/update-admin-form";
    }

    @PostMapping("save-update-admin")
    public String saveUpdateAdmin(@ModelAttribute Admin admin) {
        adminService.saveUpdateAdmin(admin);
        return "redirect:/admin/list-admins";
    }

    @PostMapping("delete-admin")
    String deleteAdmin(@RequestParam("adminId") UUID adminId) {
        adminRepository.deleteById(adminId);
        return "redirect:/admin/list-admins";
    }

    //============================CHANGE PASSWORD=================================

    @GetMapping("update-password")
    String UpdatePassword(@RequestParam(name = "message", required = false) String message, Model model) {
        if (message == null) {
            message = "";
        }
        model.addAttribute("message", message);
        return "Admin_espace/update-password-form";
    }

    @PostMapping("new-password")
    String newPassword(@RequestParam("password") String oldPassword, Model model) {
        Admin admin = adminService.getProfile();
        if (passwordEncoder.matches(oldPassword, admin.getPassword())) {
            // Si le mot de passe est correct, on peut changer le mot de passe
            return "Admin_espace/new-password-form";
        } else {
            // Mot de passe actuel incorrect
            return "redirect:/admin/update-password?message=Mot de passe actuel incorrect";
        }

    }

    @PostMapping("save-new-password")
    String SaveNewPassword(@RequestParam("newPassword") String newPassword, Model model) {
        Admin admin = adminService.getProfile();
        admin.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(admin);
        return "Receptionist_espace/password-changed-successfully";

    }

    @GetMapping("profile")
    String profile( Model model) {

        Admin admin = adminService.getProfile();
        String qrCodeBase64 = QrCodeGenerator.generateQrCodeBase64(admin.getId().toString());
        model.addAttribute("qrCodeBase64", qrCodeBase64);
        model.addAttribute("profile", admin);
        model.addAttribute("profileImage", admin.getBase64Image());
        return "Receptionist_espace/profile";
    }

    @GetMapping("updateProfile")
    String updateProfile(Model model) {
        Admin admin = adminService.getProfile();
        model.addAttribute("profile", admin);
        return "Receptionist_espace/updateProfile";
    }
    @PostMapping("saveUpdateProfile")
    String saveUpdateProfile(@RequestParam("profileImage") MultipartFile file,
                             @RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("email") String email,
                             @RequestParam("phone") String phone,
                             @RequestParam("CIN") String CIN,
                             @RequestParam("CNSS") String CNSS,
                             Model model) {
        Admin admin = adminService.getProfile();
        admin.setEmail(email);
        admin.setPhone(phone);
        admin.setCNSS_number(CNSS);
        admin.setCIN(CIN);
        admin.setLast_name(lastName);
        admin.setFirst_name(firstName);
        try {
            if (!file.isEmpty()) {
                byte[] imageData = file.getBytes();
                admin.setImage(imageData);
                adminRepository.save(admin);
                String base64Image = Base64.getEncoder().encodeToString(admin.getImage());
                admin.setBase64Image("data:image/png;base64,"+base64Image);
                httpSession.setAttribute("profileImage", admin.getBase64Image());
            }
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to upload image.");
        }
        model.addAttribute("profile", admin);
        return "redirect:/admin/profile";
    }
}