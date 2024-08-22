package com.management.leaderspace;

import com.management.leaderspace.Entities.*;
import com.management.leaderspace.Repositories.*;
import com.management.leaderspace.model.NumberToWordsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class LeaderspaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeaderspaceApplication.class, args);
    }

    @Bean
    CommandLineRunner run(AdminRepository adminRepository,
                          PasswordEncoder passwordEncoder,
                          ManagerRepository managerRepository,
                          BusinessRepository businessRepository,
                          ReceptionistRepository receptionistRepository,
                          SubscriberRepository subscriberRepository,
                          ServiceTypeRepository serviceTypeRepository,
                          SubscriptionTypeRepository subscriptionTypeRepository,
                          VisitRepository visitRepository,
                          SnacksAndBoissonsRepository snacksAndBoissonsRepository,
                          NotSubscriberRepository notSubscriberRepository,
                          DevisRepository devisRepository,
                          DesignationRepository designationRepository,
                          FactureRepository factureRepository) {
        return args -> {

            /*Admin receptionist = new Admin();
            receptionist.setEmail("admin@admin.com");
            receptionist.setPassword(passwordEncoder.encode("1234"));
            receptionist.setCIN("Z144421");
            receptionist.setFirst_name("admin");
            receptionist.setLast_name("admin");
            receptionist.setPhone("0712345678");
            receptionist.setCNSS_number("111111");
            adminRepository.save(receptionist);*/

//            ServiceType serviceType = new ServiceType();
//            serviceType.setName("Demi-Journée");
//            serviceType.setPrice(15);
//            serviceType.setDuration(6L);
//            serviceType.setNumber_of_Free_Drinks(1);
//            serviceTypeRepository.save(serviceType);
//            serviceType = new ServiceType();
//            serviceType.setName("Journée");
//            serviceType.setPrice(24);
//            serviceType.setDuration(14L);
//            serviceType.setNumber_of_Free_Drinks(2);
//            serviceTypeRepository.save(serviceType);
//            serviceType = new ServiceType();
//            serviceType.setName("bureau privée (heur)");
//            serviceType.setPrice(50);
//            serviceType.setDuration(1L);
//            serviceType.setNumber_of_Free_Drinks(1);
//            serviceTypeRepository.save(serviceType);
//            serviceType = new ServiceType();
//            serviceType.setName("salle de réunion (heur)");
//            serviceType.setPrice(150);
//            serviceType.setDuration(1L);
//            serviceType.setNumber_of_Free_Drinks(0);
//            serviceTypeRepository.save(serviceType);
//
//            NotSubscriber notSubscriber = new NotSubscriber();
//            for (int i=0;i<10;i++) {
//                notSubscriberRepository.save(notSubscriber);
//                notSubscriber = new NotSubscriber();
//            }


//subscriberRepository.deleteAll(subscriberRepository.findAll());
//            SubscriptionType subscriptionType = new SubscriptionType();
//            subscriptionType.setName("Journée (15 jours)");
//            subscriptionType.setDuration(15L);
//            subscriptionType.setHour_of_day(14);
//            subscriptionType.setFlexibility_duration(30L);
//            subscriptionType.setNumber_of_Free_Drinks_of_day(2);
//            subscriptionType.setPrice(300);
//            subscriptionTypeRepository.save(subscriptionType);
//            subscriptionType = new SubscriptionType();
//            subscriptionType.setName("Journée (Mois)");
//            subscriptionType.setDuration(30L);
//            subscriptionType.setHour_of_day(14);
//            subscriptionType.setFlexibility_duration(60L);
//            subscriptionType.setNumber_of_Free_Drinks_of_day(2);
//            subscriptionType.setPrice(500);
//            subscriptionTypeRepository.save(subscriptionType);
//            subscriptionType = new SubscriptionType();
//            subscriptionType.setName("Journée (3 Mois)");
//            subscriptionType.setDuration(90L);
//            subscriptionType.setHour_of_day(14);
//            subscriptionType.setFlexibility_duration(90L);
//            subscriptionType.setNumber_of_Free_Drinks_of_day(2);
//            subscriptionType.setPrice(1200);
//            subscriptionTypeRepository.save(subscriptionType);
//            subscriptionType = new SubscriptionType();
//            subscriptionType.setName("Demi-Journée (15 jours)");
//            subscriptionType.setDuration(15L);
//            subscriptionType.setHour_of_day(6);
//            subscriptionType.setFlexibility_duration(30L);
//            subscriptionType.setNumber_of_Free_Drinks_of_day(1);
//            subscriptionType.setPrice(150);
//            subscriptionTypeRepository.save(subscriptionType);
//            subscriptionType = new SubscriptionType();
//            subscriptionType.setName("Demi-Journée (1 Mois)");
//            subscriptionType.setDuration(15L);
//            subscriptionType.setHour_of_day(6);
//            subscriptionType.setFlexibility_duration(30L);
//            subscriptionType.setNumber_of_Free_Drinks_of_day(1);
//            subscriptionType.setPrice(250);
//            subscriptionTypeRepository.save(subscriptionType);
//
//            Receptionist receptionist = new Receptionist();
//            receptionist.setEmail("receptionist@receptionist.com");
//            receptionist.setPassword(passwordEncoder.encode("receptionist"));
//            receptionist.setCIN("Z144421");
//            receptionist.setFirst_name("receptionist");
//            receptionist.setLast_name("receptionist");
//            receptionist.setPhone("122286789");
//            receptionistRepository.save(receptionist);


//            NumberToWordsService service = new NumberToWordsService();
//            System.out.println(service.convertToWords(23341L));  // vingt-trois mille trois cent quarante et un
//            System.out.println(service.convertToWords(1000000L));  // un million
//            System.out.println(service.convertToWords(2000000L));  // deux millions
//            System.out.println(service.convertToWords(1001L));  // mille un
//            devisRepository.deleteAll();
//            factureRepository.deleteAll();
//            designationRepository.deleteAll();

//            Designation designation = new Designation("Abonnement Journée (mois)", 500);
//            designationRepository.save(designation);
//            designation = new Designation("Abonnement Demmi-Journée (mois)", 350);
//            designationRepository.save(designation);
//            designation = new Designation("Abonnement Journée (15 JOURS)", 250);
//            designationRepository.save(designation);
//

//            Manager manager = new Manager();
//            manager.setEmail("manager@manager.com");
//            manager.setPassword(passwordEncoder.encode("manager"));
//            manager.setCIN("Z174295");
//            manager.setFirst_name("manager");
//            manager.setLast_name("manager");
//            manager.setPhone("121156789");
//            managerRepository.save(manager);

            /*List<Devis> devis=devisRepository.findAll();
            if(devis.isEmpty()){
                System.out.println("No devis found ");

            }else {
                for (Devis devis1 : devis) {
                    for (Designation devis2 : devis1.getDesignations()) {
                        System.out.println(devis2.getService_request());
                        System.out.println(devis2.getId());
                        System.out.println(devis2.getHT());
                    }

                }
            }*/

            /*LocalDate dateDebut = LocalDate.of(2024, 7, 1);
            LocalDate dateFin = LocalDate.of(2024, 8, 1);
            List<Visit> visits = visitRepository.getVisitsBetween(dateDebut, dateFin);
            if (visits.isEmpty()) {
                System.out.println("No visits between " + dateDebut + " and " + dateFin);
            } else {
                System.out.println("============List<Visit>==============");
                for (Visit visit : visits) {
                    System.out.println(visit.getId());
                    System.out.println(visit.getDay());
                    System.out.println(visit.getStartTime());
                    System.out.println(visit.getEndTime());
                    System.out.println("==========================");
                }
            }*/

           /* NotSubscriber notSubscriber = new NotSubscriber();
            for (int i=0;i<10;i++) {
                notSubscriberRepository.save(notSubscriber);
                notSubscriber = new NotSubscriber();
            }*/



            /*List<Visit> visits =visitRepository.findAll();
            if(!visits.isEmpty()){
                visitRepository.deleteAll(visits);
            }*/





            /*  *//*List<SnacksAndBoissons> snacksAndBoissons =snacksAndBoissonsRepository.findByName("snack"+1);
            if(!snacksAndBoissons.isEmpty()){
                snacksAndBoissonsRepository.deleteAll(snacksAndBoissons);
            }*//*


            SnacksAndBoissons snacksAndBoissons = new SnacksAndBoissons();
            snacksAndBoissons.setName("boisson" + 1);
            snacksAndBoissons.setPurchase_price(1.0 + 1);
            snacksAndBoissons.setSelling_price(2.0 + 1);
            snacksAndBoissons.setType("Boisson");
            snacksAndBoissonsRepository.save(snacksAndBoissons);
            snacksAndBoissons = new SnacksAndBoissons();
            snacksAndBoissons.setName("boisson" + 2);
            snacksAndBoissons.setPurchase_price(2.0 + 1);
            snacksAndBoissons.setSelling_price(3.0 + 1);
            snacksAndBoissons.setType("Boisson");
            snacksAndBoissonsRepository.save(snacksAndBoissons);
            snacksAndBoissons = new SnacksAndBoissons();
            snacksAndBoissons.setName("snack" + 1);
            snacksAndBoissons.setPurchase_price(1.0 + 1);
            snacksAndBoissons.setSelling_price(2.0 + 1);
            snacksAndBoissons.setType("Snack");
            snacksAndBoissonsRepository.save(snacksAndBoissons);
            snacksAndBoissons = new SnacksAndBoissons();
            snacksAndBoissons.setName("snack" + 2);
            snacksAndBoissons.setPurchase_price(5.0 + 1);
            snacksAndBoissons.setSelling_price(8.0 + 1);
            snacksAndBoissons.setType("Snack");
            snacksAndBoissonsRepository.save(snacksAndBoissons);




            *//*List<Visit> visits=visitRepository.deleteByClientId(UUID.fromString("6f46a27c-314a-4188-bfbe-f44b7c2c589c"));
            if(!visits.isEmpty()){
                visitRepository.deleteAll(visits);
            }*/

            /*Admin admin = new Admin();
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setCIN("Z179365");
            admin.setFirst_name("admin");
            admin.setLast_name("admin");
            admin.setPhone("194276789");
            adminRepository.save(admin);

            Manager manager = new Manager();
            manager.setEmail("manager@manager.com");
            manager.setPassword(passwordEncoder.encode("manager"));
            manager.setCIN("Z174295");
            manager.setFirst_name("manager");
            manager.setLast_name("manager");
            manager.setPhone("121156789");
            managerRepository.save(manager);

            Receptionist receptionist = new Receptionist();
            receptionist.setEmail("receptionist@receptionist.com");
            receptionist.setPassword(passwordEncoder.encode("receptionist"));
            receptionist.setCIN("Z144421");
            receptionist.setFirst_name("receptionist");
            receptionist.setLast_name("receptionist");
            receptionist.setPhone("122286789");
            receptionistRepository.save(receptionist);

            Business business = new Business();
            business.setEmail("business@business.com");
            business.setPassword(passwordEncoder.encode("business"));
            business.setCIN("Z126661");
            business.setFirst_name("business");
            business.setLast_name("business");
            business.setPhone("123446789");
            business.setICE("12345");
            business.setCompany_name("company name");
            businessRepository.save(business);

            ServiceType serviceType = new ServiceType();
            serviceType.setName("Demi-journée");
            serviceType.setDuration(6L);
            serviceType.setNumber_of_Free_Drinks(1);
            serviceType.setPrice(15);
            serviceTypeRepository.save(serviceType);
            serviceType = new ServiceType();
            serviceType.setName("Journée");
            serviceType.setDuration(14L);
            serviceType.setNumber_of_Free_Drinks(2);
            serviceType.setPrice(24);
            serviceTypeRepository.save(serviceType);
            serviceType = new ServiceType();
            serviceType.setName("Bureau Privé (Heure)");
            serviceType.setDuration(1L);
            serviceType.setNumber_of_Free_Drinks(0);
            serviceType.setPrice(20);
            serviceTypeRepository.save(serviceType);
            serviceType = new ServiceType();
            serviceType.setName("Bureau Privé (Jeur)");
            serviceType.setDuration(24L);
            serviceType.setNumber_of_Free_Drinks(1);
            serviceType.setPrice(60);
            serviceTypeRepository.save(serviceType);
            serviceType = new ServiceType();
            serviceType.setName("Bureau Privé (Mois)");
            serviceType.setDuration(24L * 30);
            serviceType.setNumber_of_Free_Drinks(2);
            serviceType.setPrice(600);
            serviceTypeRepository.save(serviceType);
            serviceType = new ServiceType();
            serviceType.setName("Salle De Réunion / Formation (Heur)");
            serviceType.setDuration(1L);
            serviceType.setNumber_of_Free_Drinks(0);
            serviceType.setPrice(150);
            serviceTypeRepository.save(serviceType);

//=======================================================


            SubscriptionType subscriptionType = new SubscriptionType();
            subscriptionType.setDuration(15L);
            serviceType = serviceTypeRepository.getByName("Demi-journée");
            subscriptionType.setServiceType(serviceType);
            subscriptionTypeRepository.save(subscriptionType);

            subscriptionType = new SubscriptionType();
            subscriptionType.setDuration(30L);
            serviceType = serviceTypeRepository.getByName("Demi-journée");
            subscriptionType.setServiceType(serviceType);
            subscriptionTypeRepository.save(subscriptionType);


            subscriptionType = new SubscriptionType();
            subscriptionType.setDuration(15L);
            serviceType = serviceTypeRepository.getByName("Journée");
            subscriptionType.setServiceType(serviceType);
            subscriptionTypeRepository.save(subscriptionType);


            subscriptionType = new SubscriptionType();
            subscriptionType.setDuration(30L);
            serviceType = serviceTypeRepository.getByName("Journée");
            subscriptionType.setServiceType(serviceType);
            subscriptionTypeRepository.save(subscriptionType);

            subscriptionType = new SubscriptionType();
            subscriptionType.setDuration(90L);
            serviceType = serviceTypeRepository.getByName("Journée");
            subscriptionType.setServiceType(serviceType);
            subscriptionTypeRepository.save(subscriptionType);

            subscriptionType = new SubscriptionType();
            subscriptionType.setDuration(30L);
            serviceType = serviceTypeRepository.getByName("Bureau Privé (Mois)");
            subscriptionType.setServiceType(serviceType);
            subscriptionTypeRepository.save(subscriptionType);
*/

            /*LocalDate dateDebut = LocalDate.of(2024, 7, 26);
            LocalDate dateFin = LocalDate.of(2024, 7, 30);
            Subscriber subscriber = new Subscriber();
            subscriber.setEmail("subscriber1@subscriber.com");
            subscriber.setPassword(passwordEncoder.encode("subscriber1"));
            subscriber.setCIN("Z121177");
            subscriber.setFirst_name("subscriber1");
            subscriber.setLast_name("subscriber1");
            subscriber.setPhone("121111555");
            subscriber.setDate_debut(dateDebut);
            subscriber.setDate_fin(dateFin);
             subscriptionType = subscriptionTypeRepository.getBYNameAnsDuration("Journée", 15L);
            subscriber.setSubscription_type(subscriptionType);
            subscriberRepository.save(subscriber);*/



















/*// Set the date_debut to the current date
            Calendar calendar = Calendar.getInstance();
            Date dateDebut = calendar.getTime();
            subscriber.setDate_debut(dateDebut);

// Set the date_fin to a specific date (e.g., 30 days later)
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date dateFin = calendar.getTime();
            subscriber.setDate_fin(dateFin);*/


        };
    }
}
