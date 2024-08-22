package com.management.leaderspace.Services;

import com.management.leaderspace.Entities.Utilisateur;
import com.management.leaderspace.Repositories.UtilisateurRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.UUID;
@AllArgsConstructor
@Service
public class PasswordServiceImp implements PasswordService {
    private final PasswordEncoder passwordEncoder;
    private final UtilisateurRepository utilisateurRepository;
    JavaMailSender mailSender;

    @Override
    public boolean sendPasswordResetLink(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email);
        if (utilisateur == null) {
            return false; // Email non trouvé
        }
        String token = UUID.randomUUID().toString();
        utilisateur.setResetToken(token);
        utilisateurRepository.save(utilisateur);
        System.out.println("http://localhost:9090/reset-password?token=" + token);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(utilisateur.getEmail());
            helper.setFrom("noreply@leaderspace.net"); // Set the sender's email address
            helper.setSubject("Réinitialisation de votre mot de passe");
            helper.setText("Pour réinitialiser votre mot de passe, veuillez cliquer sur le lien suivant : "
                    + "http://localhost:9090/reset-password?token=" + token, true);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean validatePasswordResetToken(String token) {
        Utilisateur user = utilisateurRepository.findByResetToken(token);
        return user != null;
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        // Rechercher l'utilisateur par le token
        Utilisateur user = utilisateurRepository.findByResetToken(token);
        if (user == null) {
            return false;
        }

        // Réinitialiser le mot de passe (assurez-vous de hacher le mot de passe avant de le sauvegarder)
        user.setPassword(passwordEncoder.encode(newPassword)); // Hypothetical hashPassword method
        user.setResetToken(null); // Supprimer le token après utilisation
        utilisateurRepository.save(user);

        return true;
    }
}
