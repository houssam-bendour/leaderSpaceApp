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



    public void SubscriptionAboutToExpire() {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");
        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

        LocalDate localDate = moroccoDateTime.toLocalDate().plusDays(3);
        List<Subscriber> subscribers = subscriberRepository.SubscriptionAboutToExpire(localDate);
        for (Subscriber subscriber : subscribers) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                // Remplir les informations dynamiques du client
                String clientName = subscriber.getFirst_name()+" "+subscriber.getLast_name();
//                long usedVisits = subscriber.getSubscription_type().getDuration()-subscriber.getNumber_of_visits();
//                long remainingVisits = subscriber.getNumber_of_visits();

                helper.setTo(subscriber.getEmail());
                helper.setFrom("noreply@leaderspace.net");
                helper.setSubject("Expiration imminente de votre abonnement chez Leader Space");

                String emailContent = "<p>Bonjour " + clientName + ",</p>" +
                        "<p>Nous espérons que vous profitez pleinement de votre expérience au sein de Leader Space.</p>" +
                        "<p>Nous souhaitons vous informer que votre abonnement arrive à échéance dans <strong>3 jours</strong>. Si"+
                        " vous souhaitez renouveler votre abonnement et continuer à bénéficier de nos services, "+
                        "il vous suffit de nous envoyer un email à l'adresse suivante : <strong><a href='mailto:contact@leaderspace.net'>contact@leaderspace.net<a></strong>" +
                        ", ou de nous envoyer un message WhatsApp au <strong>0653 563 672</strong>.</p>" +
                        "<p>Nous restons à votre disposition pour toute question ou assistance supplémentaire.</p>" +
                        "<p>Dans l'attente de votre retour, nous vous souhaitons une excellente journée.</p>" +
                        "<p>Cordialement,</p>" +
                        "<p>[Leader Space - Coworking]<br>Adresse : Kessou Meddah, Résidence Bella,n°4,Taza<br><a href='https://www.leaderspace.net'>www.leaderspace.net</a></p>";

                helper.setText(emailContent, true);

                mailSender.send(message);
            } catch (Exception e) {
                // Gérer l'exception comme nécessaire (par exemple, journaliser l'erreur)
            }
        }
    }
    public void VisitsAreAboutToExpire() {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");
        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

        LocalDate localDate = moroccoDateTime.toLocalDate().plusDays(3);
        List<Subscriber> subscribers = subscriberRepository.VisitsAreAboutToExpire(localDate);
        for (Subscriber subscriber : subscribers) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                // Remplir les informations dynamiques du client
                String clientName = subscriber.getFirst_name()+" "+subscriber.getLast_name();

                helper.setTo(subscriber.getEmail());
                helper.setFrom("noreply@leaderspace.net");
                helper.setSubject("Information sur votre abonnement chez Leader Space");

                String emailContent = "<p>Bonjour " + clientName + ",</p>" +
                        "<p>Nous espérons que vous trouvez votre expérience chez Leader Space agréable et productive.</p>" +
                        "<p>Nous souhaitons vous informer qu'il vous reste <strong>"+subscriber.getNumber_of_visits()+" visites</strong> sur votre abonnement. Pour"+
                        " continuer à profiter de notre espace après épuisement de ces visites, vous pouvez renouveler votre abonnement en nous envoyant un email à "+
                        "<strong><a href='mailto:contact@leaderspace.net'>contact@leaderspace.net<a></strong>" +
                        ", ou de nous envoyer un message WhatsApp au <strong>0653 563 672</strong>.</p>" +
                        "<p>Si vous avez des questions ou besoin d'assistance, n'hésitez pas à nous contacter.</p>" +
                        "<p>Merci de votre fidélité et à très bientôt.</p>" +
                        "<p>Cordialement,</p>" +
                        "<p>[Leader Space - Coworking]<br>Adresse : Kessou Meddah, Résidence Bella,n°4,Taza<br>Téléphone : 0808691616<br>Email : <a href='mailto:contact@leaderspace.net'>contact@leaderspace.net</a> <br><a href='https://www.leaderspace.net'>www.leaderspace.net</a></p>";

                helper.setText(emailContent, true);

                mailSender.send(message);
                subscriber.setSendEmail(true);
                subscriberRepository.save(subscriber);
            } catch (Exception e) {
                // Gérer l'exception comme nécessaire (par exemple, journaliser l'erreur)
            }
        }
    }

    @Scheduled(cron = "0 0 11 * * ?")
    public void sendingEmail(){
        SubscriptionAboutToExpire();
        VisitsAreAboutToExpire();
    }


}
