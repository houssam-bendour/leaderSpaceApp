package com.management.leaderspace.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class RedirectController {
    @GetMapping("/defaultRedirect")
    public String defaultAfterLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        String role = user.getAuthorities().iterator().next().getAuthority();

        return switch (role) {
            case "ROLE_ADMIN" -> "redirect:/Admin-Home";
            case "ROLE_BUSINESS" -> "redirect:/Business-Home";
            case "ROLE_MANAGER" -> "redirect:/Manager-Home";
            case "ROLE_RECEPTIONIST" -> "redirect:/Receptionist-Home";
            case "ROLE_SUBSCRIBER" -> "redirect:/Subscriber-Home";
            default -> "redirect:/login";
        };
    }
}
