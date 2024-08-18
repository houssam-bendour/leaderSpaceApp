package com.management.leaderspace.Controllers;

import com.management.leaderspace.Services.PasswordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class PasswordController {
    PasswordService passwordService;
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        // Logic to handle sending the reset link

        boolean emailSent = passwordService.sendPasswordResetLink(email);

        if (emailSent) {
            model.addAttribute("message", "Un lien de réinitialisation a été envoyé à votre adresse e-mail.");
        } else {
            model.addAttribute("error", "Aucun compte trouvé avec cette adresse e-mail.");
        }

        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        // Validate the token
        if (!passwordService.validatePasswordResetToken(token)) {
            model.addAttribute("error", "Lien de réinitialisation du mot de passe invalide ou expiré.");
            return "error";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       Model model) {
        boolean resetSuccessful = passwordService.resetPassword(token, password);

        if (resetSuccessful) {
            model.addAttribute("message", "Votre mot de passe a été réinitialisé avec succès.");
            return "login";
        } else {
            model.addAttribute("error", "Échec de la réinitialisation du mot de passe.");
            return "reset-password";
        }
    }
}
