package com.management.leaderspace.Controllers;

import com.management.leaderspace.Entities.*;
import com.management.leaderspace.Services.Admin.AdminService;
import com.management.leaderspace.Services.Business.BusinessService;
import com.management.leaderspace.Services.Manager.ManagerService;
import com.management.leaderspace.Services.Receptionist.ReceptionistService;
import com.management.leaderspace.Services.Subscriber.SubscriberService;
import com.management.leaderspace.model.QrCodeGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;

@Controller
@AllArgsConstructor
public class HomeController {

    //test new branche

    AdminService adminService;
    BusinessService businessService;
    ManagerService managerService;
    ReceptionistService receptionistService;
    SubscriberService subscriberService;

    //StudentService studentService;
    @GetMapping("/default")
    public String defaultAfterLogin() {
        return "redirect:/defaultRedirect";
    }

    @GetMapping("/Admin-Home")
    public String homeAdmin(Model model) {
        Admin admin= adminService.getProfile();
        String qrCodeBase64 = QrCodeGenerator.generateQrCodeBase64(admin.getId().toString());
        model.addAttribute("qrCodeBase64", qrCodeBase64);
        model.addAttribute("profile", admin);
        return "/Admin_espace/Home";
    }

    @GetMapping("/Subscriber-Home")
    public String homeSubscriber(Model model) {
        Subscriber subscriber= subscriberService.getProfile();
        String qrCodeBase64 = QrCodeGenerator.generateQrCodeBase64(subscriber.getId().toString());
        model.addAttribute("qrCodeBase64", qrCodeBase64);
        model.addAttribute("profile", subscriber);
        return "/Subscriber_espace/Home";
    }

    @GetMapping("/Receptionist-Home")
    public String homeReceptionist(Model model) {
        Receptionist receptionist= receptionistService.getProfile();
        String qrCodeBase64 = QrCodeGenerator.generateQrCodeBase64(receptionist.getId().toString());
        model.addAttribute("qrCodeBase64", qrCodeBase64);
        model.addAttribute("profile", receptionist);
        return "/Receptionist_espace/Home";
    }

    @GetMapping("/Manager-Home")
    public String homeManager(Model model) {
        Manager manager= managerService.getProfile();
        String qrCodeBase64 = QrCodeGenerator.generateQrCodeBase64(manager.getId().toString());
        model.addAttribute("qrCodeBase64", qrCodeBase64);
        model.addAttribute("profile", manager);
        if (manager.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(manager.getImage());
            manager.setBase64Image(base64Image);
        }
        return "/Manager_espace/Home";
    }

    @GetMapping("/Business-Home")
    public String homeBusiness(Model model) {
        Business business = businessService.getProfile();
        String qrCodeBase64 = QrCodeGenerator.generateQrCodeBase64(business.getId().toString());
        model.addAttribute("qrCodeBase64", qrCodeBase64);
        model.addAttribute("profile", business);
        return "/Business_espace/Home";
    }


}
