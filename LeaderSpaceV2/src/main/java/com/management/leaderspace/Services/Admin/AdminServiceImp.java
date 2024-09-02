package com.management.leaderspace.Services.Admin;

import com.management.leaderspace.Entities.Admin;
import com.management.leaderspace.Entities.Manager;
import com.management.leaderspace.Entities.Subscriber;
import com.management.leaderspace.Repositories.AdminRepository;
import com.management.leaderspace.Repositories.ManagerRepository;
import com.management.leaderspace.Repositories.SubscriberRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImp implements AdminService {
    private final ManagerRepository managerRepository;
    private final SubscriberRepository subscriberRepository;
    AdminRepository adminRepository;
    PasswordEncoder passwordEncoder;
    JavaMailSender mailSender;

    @Override
    public Admin getProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        Admin admin = adminRepository.findByEmail(username);
        if (admin.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(admin.getImage());
            admin.setBase64Image("data:image/png;base64," + base64Image);
        } else
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


//    @Scheduled(cron = "0 0 11 * * ?")
//    public void SubscriptionAboutToExpire() {
//        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");
//        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);
//
//        LocalDate localDate = moroccoDateTime.toLocalDate().plusDays(3);
//        List<Subscriber> subscribers = subscriberRepository.SubscriptionAboutToExpire(localDate);
//        for (Subscriber subscriber : subscribers) {
//            try {
//                MimeMessage message = mailSender.createMimeMessage();
//                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//                // Remplir les informations dynamiques du client
//                String clientName = subscriber.getFirst_name()+" "+subscriber.getLast_name();
//                long usedVisits = subscriber.getSubscription_type().getDuration()-subscriber.getNumber_of_visits();
//                long remainingVisits = subscriber.getNumber_of_visits();
//
//                helper.setTo(subscriber.getEmail());
//                helper.setFrom("noreply@leaderspace.net");
//                helper.setSubject("Votre abonnement approche de sa fin… Ne manquez pas l'occasion de le renouvelez dès maintenant !");
//
//                String emailContent = "<p>Bonjour " + clientName + ",</p>" +
//                        "<p>Nous espérons que vous allez bien !</p>" +
//                        "<p>Nous tenions à vous informer que vous avez utilisé <strong>" + usedVisits + " visites</strong> sur votre abonnement actuel à Leader Space. " +
//                        "Il vous reste seulement <strong>" + remainingVisits + " visites</strong> avant l'épuisement de votre abonnement.</p>" +
//                        "<p>Pour continuer à profiter de notre espace dynamique et inspirant sans interruption, nous vous invitons à renouveler votre abonnement dès aujourd'hui.</p>" +
//                        "<p>Si vous avez des questions ou besoin d'assistance pour le renouvellement de votre abonnement, n'hésitez pas à nous contacter à <strong>[Email/Téléphone]</strong>. " +
//                        "Nous sommes à votre disposition pour vous aider !</p>" +
//                        "<p>Merci pour votre confiance et pour faire partie de notre communauté Leader Space. " +
//                        "Nous avons hâte de continuer à vous accueillir et à soutenir votre travail.</p>" +
//                        "<p>Cordialement,</p>" +
//                        "<p>HIBA ESSAIH<br>Chargée de communication<br>LEADER SPACE<br>AV Kessou meddah, Résidence bella, Bureau N°4<br><a href='https://www.leaderspace.net'>www.leaderspace.net</a></p>";
//
//                helper.setText(emailContent, true);
//
//                mailSender.send(message);
//            } catch (Exception e) {
//                // Gérer l'exception comme nécessaire (par exemple, journaliser l'erreur)
//            }
//        }
//    }

    @Scheduled(cron = "0 32 23 * * ?")
    public void SubscriptionAboutToExpire() {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");
        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

        LocalDate localDate = moroccoDateTime.toLocalDate().plusDays(3);
        Subscriber subscriber = subscriberRepository.findByEmail("hsmbndr1@gmail.com");

            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                // Remplir les informations dynamiques du client
                String clientName = subscriber.getFirst_name()+" "+subscriber.getLast_name();
                long usedVisits = subscriber.getSubscription_type().getDuration()-subscriber.getNumber_of_visits();
                long remainingVisits = subscriber.getNumber_of_visits();

                helper.setTo(subscriber.getEmail());
                helper.setFrom("noreply@leaderspace.net");
                helper.setSubject("Votre abonnement approche de sa fin… Ne manquez pas l'occasion de le renouvelez dès maintenant !");

                String emailContent = "<p>Bonjour " + clientName + ",</p>" +
                        "<p>Nous espérons que vous allez bien !</p>" +
                        "<p>Nous tenions à vous informer que vous avez utilisé <strong>" + usedVisits + " visites</strong> sur votre abonnement actuel à Leader Space. " +
                        "Il vous reste seulement <strong>" + remainingVisits + " visites</strong> avant l'épuisement de votre abonnement.</p>" +
                        "<p>Pour continuer à profiter de notre espace dynamique et inspirant sans interruption, nous vous invitons à renouveler votre abonnement dès aujourd'hui.</p>" +
                        "<p>Si vous avez des questions ou besoin d'assistance pour le renouvellement de votre abonnement, n'hésitez pas à nous contacter à <strong><a href='mailto:contact@leaderspace.net'>contact@leaderspace.net</a> ou (+212 808 69 16 16 / +212 653 56 36 72)</strong>. " +
                        "Nous sommes à votre disposition pour vous aider !</p>" +
                        "<p>Merci pour votre confiance et pour faire partie de notre communauté Leader Space. " +
                        "Nous avons hâte de continuer à vous accueillir et à soutenir votre travail.</p>" +
                        "<p>Cordialement,</p>" +
                        "<p>HIBA ESSAIH<br>Chargée de communication<br>LEADER SPACE<br>AV Kessou meddah, Résidence bella, Bureau N°4<br><a href='https://www.leaderspace.net'>www.leaderspace.net</a></p>";

                helper.setText(emailContent, true);

                mailSender.send(message);
            } catch (Exception e) {
                // Gérer l'exception comme nécessaire (par exemple, journaliser l'erreur)
            }

    }

}
