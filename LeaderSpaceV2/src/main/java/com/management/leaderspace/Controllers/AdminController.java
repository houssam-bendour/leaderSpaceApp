package com.management.leaderspace.Controllers;

import com.management.leaderspace.Entities.*;
import com.management.leaderspace.Repositories.AdminRepository;
import com.management.leaderspace.Repositories.ManagerRepository;
import com.management.leaderspace.Services.Admin.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
}
