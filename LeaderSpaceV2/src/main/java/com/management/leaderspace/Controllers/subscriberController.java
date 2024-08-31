package com.management.leaderspace.Controllers;

import com.management.leaderspace.Entities.Admin;
import com.management.leaderspace.Entities.Receptionist;
import com.management.leaderspace.Entities.Subscriber;
import com.management.leaderspace.Entities.Utilisateur;
import com.management.leaderspace.Repositories.SubscriberRepository;
import com.management.leaderspace.Repositories.UtilisateurRepository;
import com.management.leaderspace.Services.Subscriber.SubscriberService;
import com.management.leaderspace.model.QrCodeGenerator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;

@Controller
@RequestMapping("/subscriber")
@AllArgsConstructor
@Transactional
public class subscriberController {

    private final SubscriberRepository subscriberRepository;
    private final UtilisateurRepository utilisateurRepository;
    SubscriberService subscriberService;
    PasswordEncoder passwordEncoder;

    @GetMapping("visit")
    public String visit(@RequestParam(name = "date_debut", required = false) LocalDate date_debut,
                        @RequestParam(name = "date_fin", required = false) LocalDate date_fin,
                        Model model) {
        if (date_debut==null || date_fin==null) {
            model.addAttribute("visits", subscriberService.getAllVisits());
        }else {
            model.addAttribute("visits", subscriberService.getVisitsByDate(date_debut,date_fin));
        }


        return "/Subscriber_espace/visit";
    }

    @GetMapping("profile")
    String profile( Model model) {
        Subscriber subscriber = subscriberService.getProfile();
        String qrCodeBase64 = QrCodeGenerator.generateQrCodeBase64(subscriber.getId().toString());
        model.addAttribute("qrCodeBase64", qrCodeBase64);
        model.addAttribute("profile", subscriber);
        model.addAttribute("profileImage", subscriber.getBase64Image());
        return "Subscriber_espace/profile";
    }
    @GetMapping("updateProfile")
    String updateProfile(Model model) {
        Subscriber subscriber = subscriberService.getProfile();
        model.addAttribute("profile", subscriber);
        return "Subscriber_espace/updateProfile";
    }
    @PostMapping("saveUpdateProfile")
    String saveUpdateProfile(@RequestParam("profileImage") MultipartFile file,
                             @RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("email") String email,
                             @RequestParam("phone") String phone,
                             @RequestParam("CIN") String CIN,
                             Model model) {
        Subscriber subscriber = subscriberService.getProfile();
        subscriber.setEmail(email);
        subscriber.setPhone(phone);
        subscriber.setCIN(CIN);
        subscriber.setLast_name(lastName);
        subscriber.setFirst_name(firstName);
        try {
            if (!file.isEmpty()) {
                byte[] imageData = file.getBytes();
                subscriber.setImage(imageData);
            }
            subscriberRepository.save(subscriber);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to upload image.");
        }
        return "redirect:/subscriber/profile";
    }
    @GetMapping("update-password")
    String updatePassword(@RequestParam(defaultValue = "") String message, Model model) {
        model.addAttribute("message", message);
        return "Subscriber_espace/updatePassword";
    }
    @PostMapping("validity-password")
    String validityPassword(@RequestParam String oldPassword,
                            Model model) {
       Subscriber subscriber = subscriberService.getProfile();
        if (passwordEncoder.matches(oldPassword, subscriber.getPassword())) {
            // Si le mot de passe est correct, on peut changer le mot de passe
            return "Subscriber_espace/new-password-form";
        } else {
            // Mot de passe actuel incorrect
            return "redirect:update-password?message=Mot de passe actuel incorrect";
        }
    }

    @PostMapping("save-new-password")
    String SaveNewPassword(@RequestParam("newPassword") String newPassword, Model model) {
        Subscriber subscriber = subscriberService.getProfile();
        subscriber.setPassword(passwordEncoder.encode(newPassword));
        subscriberRepository.save(subscriber);
        return "Subscriber_espace/password-changed-successfully";
    }


    @GetMapping("about")
    String about() {
        return "Subscriber_espace/about";
    }
    @GetMapping("badge")
    String badg(Model model) {
        Subscriber subscriber = subscriberService.getProfile();
        String qrCodeBase64 = QrCodeGenerator.generateQrCodeBase64(subscriber.getId().toString());
        model.addAttribute("user", subscriber);
        model.addAttribute("qrCodeBase64", qrCodeBase64);
        return "Subscriber_espace/badge";
    }}

