package com.management.leaderspace.Services.Receptionist;

import com.management.leaderspace.Entities.*;
import com.management.leaderspace.Repositories.*;
import com.management.leaderspace.model.SnackForm;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@AllArgsConstructor
public class ReceptionistServiceImp implements ReceptionistService {
    ReceptionistRepository receptionistRepository;
    VisitRepository visitRepository;
    VisitOfDeskRepository visitOfDeskRepository;
    VisitOfRoomRepository visitOfRoomRepository;
    SubscriberRepository subscriberRepository;
    SnacksAndBoissonsOfVisitRepository snacksAndBoissonsOfVisitRepository;
    PasswordEncoder passwordEncoder;
    SubscriptionTypeRepository subscriptionTypeRepository;
    NotSubscriberRepository notSubscriberRepository;
    SnacksAndBoissonsRepository snacksAndBoissonsRepository;
    SubscriptionHistoryRepository subscriptionHistoryRepository;
    ParticipantOfvisitRoomRepository participantOfvisitRoomRepository;
    UtilisateurRepository utilisateurRepository;
    VisitOfTeamRepository visitOfTeamRepository;

    @Override
    public Receptionist getProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        Receptionist receptionist=receptionistRepository.findByEmail(username);
        if (receptionist.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(receptionist.getImage());
            receptionist.setBase64Image("data:image/png;base64,"+base64Image);
        }else
            receptionist.setBase64Image("https://cdn.pixabay.com/photo/2017/08/06/21/01/louvre-2596278_960_720.jpg");
        return receptionist;    }

    @Override
    public Visit createVisitForSubscriber(Subscriber subscriber) {

        Visit newVisit = new Visit();

        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

        LocalDate localDate = moroccoDateTime.toLocalDate();

        LocalTime localTime = moroccoDateTime.toLocalTime();

        if (subscriber.getSubscription_type().getHour_of_day()>6L){

            List<Visit> visitsToday=visitRepository.getVisitsTodayOfSubscriberAndDate(subscriber.getId(),localDate);

            if(visitsToday.isEmpty()){

                subscriber.setNumber_of_visits(subscriber.getNumber_of_visits()-1);

            }
        }
        else {

            subscriber.setNumber_of_visits(subscriber.getNumber_of_visits()-1);

        }
        subscriberRepository.save(subscriber);

        newVisit.setDay(localDate);

        newVisit.setStartTime(localTime);

        newVisit.setSubscriber(subscriber);

        if (subscriber.getNumber_of_visits() == 0) {

            subscriber.setFinished(true);

            subscriberRepository.save(subscriber);

        }
        return visitRepository.save(newVisit);
    }
    @Override
    @Transactional
    public void updateSnackQuantity(UUID visitId, UUID snackId, int quantity) {

        Visit visit = visitRepository.findById(visitId).orElseThrow(() -> new RuntimeException("Visit not found"));

        Optional<SnacksAndBoissonsOfVisit> optionalSnacksAndBoissonsOfVisit = visit.getSnacksAndBoissonsOfVisits()
                .stream()
                .filter(snack -> snack.getSnacksAndBoissons().getId().equals(snackId))
                .findFirst();

        if (optionalSnacksAndBoissonsOfVisit.isPresent()) {
            SnacksAndBoissonsOfVisit snacksAndBoissonsOfVisit = optionalSnacksAndBoissonsOfVisit.get();
            ////////////////////zt
            SnacksAndBoissons snacksAndBoissons = snacksAndBoissonsOfVisit.getSnacksAndBoissons();
            if (snacksAndBoissons.getQuantity()!=null){
                int q1 =  quantity-snacksAndBoissonsOfVisit.getQuantity();
                snacksAndBoissons.setQuantity(snacksAndBoissons.getQuantity()-q1);
                snacksAndBoissonsRepository.save(snacksAndBoissons);
            }
            //////////////zt
            // Update the quantity
            snacksAndBoissonsOfVisit.setQuantity(quantity);
            // Save the updated entity
            snacksAndBoissonsOfVisitRepository.save(snacksAndBoissonsOfVisit);
        } else {
            throw new RuntimeException("Snack not found for the given visit");
        }
    }

    @Override
    @Transactional
    public void updateSnackQuantityForRoom(UUID visitId, UUID snackId, int quantity) {

        VisitOfRoom visit = visitOfRoomRepository.findById(visitId).orElseThrow(() -> new RuntimeException("Visit not found"));

        Optional<SnacksAndBoissonsOfVisit> optionalSnacksAndBoissonsOfVisit = visit.getSnacksAndBoissonsOfVisitRoom()
                .stream()
                .filter(snack -> snack.getSnacksAndBoissons().getId().equals(snackId))
                .findFirst();

        if (optionalSnacksAndBoissonsOfVisit.isPresent()) {
            SnacksAndBoissonsOfVisit snacksAndBoissonsOfVisit = optionalSnacksAndBoissonsOfVisit.get();
            SnacksAndBoissons snacksAndBoissons = snacksAndBoissonsOfVisit.getSnacksAndBoissons();
            if (snacksAndBoissons.getQuantity()!=null){
                int q1 =  quantity-snacksAndBoissonsOfVisit.getQuantity();
                snacksAndBoissons.setQuantity(snacksAndBoissons.getQuantity()-q1);
                snacksAndBoissonsRepository.save(snacksAndBoissons);
            }
            snacksAndBoissonsOfVisit.setQuantity(quantity);
            // Save the updated entity
            snacksAndBoissonsOfVisitRepository.save(snacksAndBoissonsOfVisit);
        } else {
            throw new RuntimeException("Snack not found for the given visit");
        }
    }

    @Override
    public List<Visit> getVisitsToday() {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);


        LocalDate localDate = moroccoDateTime.toLocalDate();
        return visitRepository.getVisitsToday(localDate);
    }

    @Override
    public List<Visit> getVisitsTodayByName(String name) {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);


        LocalDate localDate = moroccoDateTime.toLocalDate();
        return visitRepository.getVisitsTodayByName(localDate,name);
    }

    @Override
    public Page<Subscriber> getSubscribers(Pageable pageable) {
        return subscriberRepository.findAll(pageable);
    }

    @Override
    public Page<Subscriber> getSubscribersByName(String name, Pageable pageable) {
        return subscriberRepository.getSubscribersByName(name,pageable);
    }

    @Override
    public Visit createVisitForNotSubscriber(NotSubscriber notSubscriber) {
        Visit newVisit = new Visit();

        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);


        LocalDate localDate = moroccoDateTime.toLocalDate();

        LocalTime localTime = moroccoDateTime.toLocalTime();

        newVisit.setDay(localDate);

        newVisit.setStartTime(localTime);

        newVisit.setNotSubscriber(notSubscriber);

        return visitRepository.save(newVisit);
    }

    @Override
    public List<Visit> getVisitsOfSubscribersToday() {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

        LocalDate localDate = moroccoDateTime.toLocalDate();

        return visitRepository.getVisitsTodayOfSubscribers(localDate);
    }

    @Override
    public List<Visit> getVisitsOfNotSubscribersToday() {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);


        LocalDate localDate = moroccoDateTime.toLocalDate();
        return visitRepository.getVisitsTodayOfNotSubscribers(localDate);
    }

    @Override
    public void saveSubscriber(UUID subscriptionTypeId,int quantity, String cin, String firstName, String lastName, String email, String phone, String password) {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

        SubscriptionType subscriptionType = subscriptionTypeRepository.findById(subscriptionTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscription type ID: " + subscriptionTypeId));

        LocalDate localDate = moroccoDateTime.toLocalDate();


        LocalDate endDate = localDate.plusMonths(subscriptionType.getFlexibility_duration()*quantity);

        Subscriber subscriber = new Subscriber();

        subscriber.setSubscription_type(subscriptionType);

        subscriber.setDate_debut(localDate);

        subscriber.setDate_fin(endDate);

        subscriber.setCIN(cin);

        subscriber.setFirst_name(firstName);

        subscriber.setLast_name(lastName);

        subscriber.setEmail(email);

        subscriber.setPhone(phone);

        subscriber.setQuantity(quantity);

        subscriber.setNumber_of_visits(subscriptionType.getDuration()*quantity);

        subscriber.setPassword(passwordEncoder.encode(password));

        subscriber.setPrice_actuel_d_abonnemet(subscriptionType.getPrice()*quantity);

        subscriberRepository.save(subscriber);

        SubscriptionHistory subscriptionHistory = new SubscriptionHistory();
        subscriptionHistory.setSubscriber(subscriber);
        subscriptionHistory.setStartDate(localDate);
        subscriptionHistory.setEndDate(endDate);
        subscriptionHistory.setSubscriptionType(subscriptionType);
        subscriptionHistory.setQuantity(quantity);
        subscriptionHistory.setPrice(subscriber.getPrice_actuel_d_abonnemet());
        subscriptionHistoryRepository.save(subscriptionHistory);

    }

    @Override
    public String newVisit(String result) {

        Subscriber subscriber = subscriberRepository.findById(UUID.fromString(result)).orElse(null);

        NotSubscriber notSubscriber = notSubscriberRepository.findById(UUID.fromString(result)).orElse(null);

        if (subscriber == null && notSubscriber != null) {
            return "redirect:/reception/new-visit-not-subscriber?userId=" + notSubscriber.getId().toString();

        } else if (subscriber != null && notSubscriber == null) {

            return "redirect:/reception/new-visit-subscriber?userId=" + subscriber.getId().toString();

        } else {

            return "redirect:/reception/visit-subscriber";
        }
    }

    @Override
    public String newParticipant(String result ,String visitId) {

        NotSubscriber notSubscriber = notSubscriberRepository.findById(UUID.fromString(result)).orElse(null);

        if (notSubscriber != null) {
            return "redirect:/reception/new-visit-participant?userId=" + notSubscriber.getId().toString()+"&visitId="+visitId;

        } else {

            return "redirect:/reception/snacks-to-participantRoom?visitId="+visitId;
        }
    }

    @Override
    public String newVisitSubscriber(String result) {
        try {
            Subscriber subscriber = subscriberRepository.findById(UUID.fromString(result)).orElse(null);

            ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

            ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

            LocalDate localDate = moroccoDateTime.toLocalDate();

            List<Visit> Expires_today = visitRepository.getByDateAndSubscriberAndEndCheckoutTime(subscriber.getId(), localDate);

            if (!Expires_today.isEmpty()) {

                if (Expires_today.size() == 2 && subscriber.getSubscription_type().getHour_of_day() == 6L)
                    return "redirect:/reception/Expires_today";

            }


            if (subscriber.isFinished() || subscriber.getDate_fin().isBefore(localDate)||subscriber.getNumber_of_visits()==0) {

                subscriber.setFinished(true);

                subscriberRepository.save(subscriber);

                return "redirect:/reception/subscribtion-fished";
            }

            Visit visitBySub = visitRepository.getByDateAndSubscriberAndEndTime(subscriber.getId(), localDate);

            if (visitBySub != null)
                return "redirect:/reception/visit-subscriber-profile?visitId=" + visitBySub.getId();

            Visit visit = createVisitForSubscriber(subscriber);

            return "redirect:/reception/visit-subscriber-profile?visitId=" + visit.getId();
        } catch (Exception e) {

            return "error";
        }
    }

    @Override
    public String newVisitNotSubscriber(String result) {
        try {
            NotSubscriber notSubscriber = notSubscriberRepository.findById(UUID.fromString(result)).orElse(null);

            ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

            ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

            LocalDate localDate = moroccoDateTime.toLocalDate();

            Visit visitBySub = visitRepository.getByDateAndNotSubscriberAndEndTime(notSubscriber.getId(), localDate);

            if (visitBySub != null)
                return "redirect:/reception/visit-subscriber-profile?visitId=" + visitBySub.getId();

            Visit visit = createVisitForNotSubscriber(notSubscriber);

            return "redirect:/reception/visit-subscriber-profile?visitId=" + visit.getId();
        } catch (Exception e) {

            return "error";
        }
    }

    @Override
    public String newVisitParticipant(String result, String visitId) {
        try {
            NotSubscriber notSubscriber = notSubscriberRepository.findById(UUID.fromString(result)).orElse(null);

            VisitOfRoom visitOfRoom = visitOfRoomRepository.findById(UUID.fromString(visitId)).orElse(null);

            ParticipantOfvisitRoom participantOfvisitRoom = participantOfvisitRoomRepository.getParticipantOfvisitRoomsByNotSubscriberAndDay(notSubscriber.getId(), visitOfRoom.getId());

            if (participantOfvisitRoom != null)
                return "redirect:/reception/visit-participant-profile?visitId=" + visitOfRoom.getId()+"&participantId="+participantOfvisitRoom.getId();

            ParticipantOfvisitRoom participantOfvisitRoom1 = new ParticipantOfvisitRoom();
            participantOfvisitRoom1.setNotSubscriber(notSubscriber);
            participantOfvisitRoom1.setVisitOfRoom(visitOfRoom);
            participantOfvisitRoomRepository.save(participantOfvisitRoom1);

            return "redirect:/reception/visit-participant-profile?visitId=" + visitOfRoom.getId()+"&participantId="+participantOfvisitRoom1.getId();


        } catch (Exception e) {

        return "error";
    }
    }

    @Override
    public void saveSnacksToVisitor(UUID visitId, SnackForm snackForm) {
        Visit visit = visitRepository.findById(visitId).orElseThrow(() -> new IllegalArgumentException("Invalid visit ID"));

        List<SnacksAndBoissonsOfVisit> snacksList = visit.getSnacksAndBoissonsOfVisits();

        snackForm.getSnackQuantities().forEach((snackId, quantity) -> {

            if (quantity == null)
                quantity = 0;

            if (quantity > 0) {

                SnacksAndBoissons snack = snacksAndBoissonsRepository.findById(snackId).orElseThrow(() -> new IllegalArgumentException("Invalid snack ID"));

                boolean snackExists = false;

                if (!snacksList.isEmpty()) {

                    for (SnacksAndBoissonsOfVisit snackVisit : snacksList) {

                        if (snackVisit.getSnacksAndBoissons().getId().equals(snackId)) {

                            snackVisit.setQuantity(snackVisit.getQuantity() + quantity);

                            snackExists = true;

                            break;
                        }
                    }

                }
                if (!snackExists) {

                    SnacksAndBoissonsOfVisit newSnackVisit = new SnacksAndBoissonsOfVisit();

                    newSnackVisit.setSnacksAndBoissons(snack);

                    newSnackVisit.setVisit(visit);

                    newSnackVisit.setQuantity(quantity);

                    newSnackVisit.setSelling_price(snack.getSelling_price());

                    newSnackVisit.setPurchase_price(snack.getPurchase_price());

                    snacksList.add(newSnackVisit);
                }
                if (snack.getQuantity()!=null){
                    snack.setQuantity(snack.getQuantity()-quantity);
                    snacksAndBoissonsRepository.save(snack);
                }
            }
        });

        visitRepository.save(visit);
    }



    @Override
    public void saveSnacksToVisitRoom(UUID visitId, SnackForm snackForm) {
        VisitOfRoom visit = visitOfRoomRepository.findById(visitId).orElseThrow(() -> new IllegalArgumentException("Invalid visit ID"));

        List<SnacksAndBoissonsOfVisit> snacksList = visit.getSnacksAndBoissonsOfVisitRoom();

        snackForm.getSnackQuantities().forEach((snackId, quantity) -> {

            if (quantity == null)
                quantity = 0;

            if (quantity > 0) {

                SnacksAndBoissons snack = snacksAndBoissonsRepository.findById(snackId).orElseThrow(() -> new IllegalArgumentException("Invalid snack ID"));

                boolean snackExists = false;

                if (!snacksList.isEmpty()) {

                    for (SnacksAndBoissonsOfVisit snackVisit : snacksList) {

                        if (snackVisit.getSnacksAndBoissons().getId().equals(snackId)) {

                            snackVisit.setQuantity(snackVisit.getQuantity() + quantity);

                            snackExists = true;

                            break;
                        }
                    }

                }
                if (!snackExists) {

                    SnacksAndBoissonsOfVisit newSnackVisit = new SnacksAndBoissonsOfVisit();

                    newSnackVisit.setSnacksAndBoissons(snack);

                    newSnackVisit.setVisitOfRoom(visit);

                    newSnackVisit.setQuantity(quantity);

                    newSnackVisit.setSelling_price(snack.getSelling_price());

                    newSnackVisit.setPurchase_price(snack.getPurchase_price());

                    snacksList.add(newSnackVisit);
                }
                if (snack.getQuantity()!=null){
                    snack.setQuantity(snack.getQuantity()-quantity);
                    snacksAndBoissonsRepository.save(snack);
                }
            }
        });

        visitOfRoomRepository.save(visit);
    }



    @Override
    public void deleteSnack(UUID visitId, UUID snackId) {
        Visit visit = visitRepository.findById(visitId).orElseThrow(() -> new IllegalArgumentException("Invalid visit ID"));

        List<SnacksAndBoissonsOfVisit> snacksList = visit.getSnacksAndBoissonsOfVisits();

        SnacksAndBoissonsOfVisit snackVisit = snacksList.stream()
                .filter(sv -> sv.getSnacksAndBoissons().getId().equals(snackId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Snack not found in visit"));

        SnacksAndBoissons snacksAndBoissons = snackVisit.getSnacksAndBoissons();
        if (snacksAndBoissons.getQuantity()!=null){
            snacksAndBoissons.setQuantity(snacksAndBoissons.getQuantity()+snackVisit.getQuantity());
            snacksAndBoissonsRepository.save(snacksAndBoissons);
        }

        snacksList.remove(snackVisit);

        visitRepository.save(visit);
    }

    @Override
    public List<VisitOfRoom> getVisitsOfRoomToday() {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);


        LocalDate localDate = moroccoDateTime.toLocalDate();
        return visitOfRoomRepository.getVisitsTodayOfRoom(localDate);
    }

    @Override
    public List<VisitOfDesk> getVisitsOfDeskToday() {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);


        LocalDate localDate = moroccoDateTime.toLocalDate();
        return visitOfDeskRepository.getVisitsTodayOfDesk(localDate);
    }

    @Override
    public void deleteSnackForRoom(UUID visitId, UUID snackId) {
        VisitOfRoom visit = visitOfRoomRepository.findById(visitId).orElseThrow(() -> new IllegalArgumentException("Invalid visit ID"));

        List<SnacksAndBoissonsOfVisit> snacksList = visit.getSnacksAndBoissonsOfVisitRoom();

        SnacksAndBoissonsOfVisit snackVisit = snacksList.stream()
                .filter(sv -> sv.getSnacksAndBoissons().getId().equals(snackId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Snack not found in visit"));

        SnacksAndBoissons snacksAndBoissons = snackVisit.getSnacksAndBoissons();
        if (snacksAndBoissons.getQuantity()!=null){
            snacksAndBoissons.setQuantity(snacksAndBoissons.getQuantity()+snackVisit.getQuantity());
            snacksAndBoissonsRepository.save(snacksAndBoissons);
        }

        snacksList.remove(snackVisit);

        visitOfRoomRepository.save(visit);
    }

    @Override
    public void saveSnacksToParticipant(UUID participantId, SnackForm snackForm) {
        ParticipantOfvisitRoom participant = participantOfvisitRoomRepository.findById(participantId).orElseThrow(() -> new IllegalArgumentException("Invalid participant ID"));

        List<SnacksAndBoissonsOfVisit> snacksList = participant.getSnacksAndBoissonsOfVisits();

        snackForm.getSnackQuantities().forEach((snackId, quantity) -> {

            if (quantity == null)
                quantity = 0;

            if (quantity > 0) {

                SnacksAndBoissons snack = snacksAndBoissonsRepository.findById(snackId).orElseThrow(() -> new IllegalArgumentException("Invalid snack ID"));

                boolean snackExists = false;

                if (!snacksList.isEmpty()) {

                    for (SnacksAndBoissonsOfVisit snackVisit : snacksList) {

                        if (snackVisit.getSnacksAndBoissons().getId().equals(snackId)) {

                            snackVisit.setQuantity(snackVisit.getQuantity() + quantity);

                            snackExists = true;

                            break;
                        }
                    }

                }
                if (!snackExists) {

                    SnacksAndBoissonsOfVisit newSnackVisit = new SnacksAndBoissonsOfVisit();

                    newSnackVisit.setSnacksAndBoissons(snack);

                    newSnackVisit.setParticipant(participant);

                    newSnackVisit.setQuantity(quantity);

                    newSnackVisit.setSelling_price(snack.getSelling_price());

                    newSnackVisit.setPurchase_price(snack.getPurchase_price());

                    snacksList.add(newSnackVisit);
                }
                if (snack.getQuantity()!=null){
                    snack.setQuantity(snack.getQuantity()-quantity);
                    snacksAndBoissonsRepository.save(snack);
                }
            }
        });

        participantOfvisitRoomRepository.save(participant);
    }

    @Override
    public void deleteSnackForParticipantOfRoom(UUID participantId, UUID snackId) {
        ParticipantOfvisitRoom participant = participantOfvisitRoomRepository.findById(participantId).orElseThrow(() -> new IllegalArgumentException("Invalid participant ID"));

        List<SnacksAndBoissonsOfVisit> snacksList = participant.getSnacksAndBoissonsOfVisits();

        SnacksAndBoissonsOfVisit snackVisit = snacksList.stream()
                .filter(sv -> sv.getSnacksAndBoissons().getId().equals(snackId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Snack not found in visit"));

        SnacksAndBoissons snacksAndBoissons = snackVisit.getSnacksAndBoissons();
        if (snacksAndBoissons.getQuantity()!=null){
            snacksAndBoissons.setQuantity(snacksAndBoissons.getQuantity()+snackVisit.getQuantity());
            snacksAndBoissonsRepository.save(snacksAndBoissons);
        }

        snacksList.remove(snackVisit);

        participantOfvisitRoomRepository.save(participant);
    }
    @Transactional
    @Override
    public void updateSnackQuantityForParticipantOfRoom(UUID participantId, UUID snackId, int quantity) {
        ParticipantOfvisitRoom participant = participantOfvisitRoomRepository.findById(participantId).orElseThrow(() -> new RuntimeException("participant not found"));

        Optional<SnacksAndBoissonsOfVisit> optionalSnacksAndBoissonsOfVisit = participant.getSnacksAndBoissonsOfVisits()
                .stream()
                .filter(snack -> snack.getSnacksAndBoissons().getId().equals(snackId))
                .findFirst();

        if (optionalSnacksAndBoissonsOfVisit.isPresent()) {
            SnacksAndBoissonsOfVisit snacksAndBoissonsOfVisit = optionalSnacksAndBoissonsOfVisit.get();
            SnacksAndBoissons snacksAndBoissons = snacksAndBoissonsOfVisit.getSnacksAndBoissons();
            if (snacksAndBoissons.getQuantity()!=null){
                int q1 =  quantity-snacksAndBoissonsOfVisit.getQuantity();
                snacksAndBoissons.setQuantity(snacksAndBoissons.getQuantity()-q1);
                snacksAndBoissonsRepository.save(snacksAndBoissons);
            }
            snacksAndBoissonsOfVisit.setQuantity(quantity);
            // Save the updated entity
            snacksAndBoissonsOfVisitRepository.save(snacksAndBoissonsOfVisit);
        } else {
            throw new RuntimeException("Snack not found for the given visit");
        }
    }

    @Override
    public void saveSnacksToVisitDesk(UUID visitId, SnackForm snackForm) {
        VisitOfDesk visit = visitOfDeskRepository.findById(visitId).orElseThrow(() -> new IllegalArgumentException("Invalid visit ID"));

        List<SnacksAndBoissonsOfVisit> snacksList = visit.getSnacksAndBoissonsOfVisits();

        snackForm.getSnackQuantities().forEach((snackId, quantity) -> {

            if (quantity == null)
                quantity = 0;

            if (quantity > 0) {

                SnacksAndBoissons snack = snacksAndBoissonsRepository.findById(snackId).orElseThrow(() -> new IllegalArgumentException("Invalid snack ID"));

                boolean snackExists = false;

                if (!snacksList.isEmpty()) {

                    for (SnacksAndBoissonsOfVisit snackVisit : snacksList) {

                        if (snackVisit.getSnacksAndBoissons().getId().equals(snackId)) {

                            snackVisit.setQuantity(snackVisit.getQuantity() + quantity);

                            snackExists = true;

                            break;
                        }
                    }

                }
                if (!snackExists) {

                    SnacksAndBoissonsOfVisit newSnackVisit = new SnacksAndBoissonsOfVisit();

                    newSnackVisit.setSnacksAndBoissons(snack);

                    newSnackVisit.setVisitOfDesk(visit);

                    newSnackVisit.setQuantity(quantity);

                    newSnackVisit.setSelling_price(snack.getSelling_price());

                    newSnackVisit.setPurchase_price(snack.getPurchase_price());

                    snacksList.add(newSnackVisit);
                }
                if (snack.getQuantity()!=null){
                    snack.setQuantity(snack.getQuantity()-quantity);
                    snacksAndBoissonsRepository.save(snack);
                }
            }
        });

        visitOfDeskRepository.save(visit);
    }

    @Override
    public void deleteSnackForDesk(UUID visitId, UUID snackId) {
        VisitOfDesk visit = visitOfDeskRepository.findById(visitId).orElseThrow(() -> new IllegalArgumentException("Invalid visit ID"));

        List<SnacksAndBoissonsOfVisit> snacksList = visit.getSnacksAndBoissonsOfVisits();

        SnacksAndBoissonsOfVisit snackVisit = snacksList.stream()
                .filter(sv -> sv.getSnacksAndBoissons().getId().equals(snackId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Snack not found in visit"));
        SnacksAndBoissons snacksAndBoissons = snackVisit.getSnacksAndBoissons();
        if (snacksAndBoissons.getQuantity()!=null){
            snacksAndBoissons.setQuantity(snacksAndBoissons.getQuantity()+snackVisit.getQuantity());
            snacksAndBoissonsRepository.save(snacksAndBoissons);
        }
        snacksList.remove(snackVisit);

        visitOfDeskRepository.save(visit);
    }

    @Override
    public void updateSnackQuantityForDesk(UUID visitId, UUID snackId, int quantity) {
        VisitOfDesk visit = visitOfDeskRepository.findById(visitId).orElseThrow(() -> new RuntimeException("Visit not found"));

        Optional<SnacksAndBoissonsOfVisit> optionalSnacksAndBoissonsOfVisit = visit.getSnacksAndBoissonsOfVisits()
                .stream()
                .filter(snack -> snack.getSnacksAndBoissons().getId().equals(snackId))
                .findFirst();

        if (optionalSnacksAndBoissonsOfVisit.isPresent()) {
            SnacksAndBoissonsOfVisit snacksAndBoissonsOfVisit = optionalSnacksAndBoissonsOfVisit.get();
            SnacksAndBoissons snacksAndBoissons = snacksAndBoissonsOfVisit.getSnacksAndBoissons();
            if (snacksAndBoissons.getQuantity()!=null){
                int q1 =  quantity-snacksAndBoissonsOfVisit.getQuantity();
                snacksAndBoissons.setQuantity(snacksAndBoissons.getQuantity()-q1);
                snacksAndBoissonsRepository.save(snacksAndBoissons);
            }
            snacksAndBoissonsOfVisit.setQuantity(quantity);
            // Save the updated entity
            snacksAndBoissonsOfVisitRepository.save(snacksAndBoissonsOfVisit);
        } else {
            throw new RuntimeException("Snack not found for the given visit");
        }
    }

    @Override
    public void updateQuantityStockOfSnackOrBoisson(List<SnacksAndBoissonsOfVisit> snacksAndBoissonsOfVisit) {
        for (SnacksAndBoissonsOfVisit snackOrBoisson : snacksAndBoissonsOfVisit){
            SnacksAndBoissons snacksAndBoissons = snackOrBoisson.getSnacksAndBoissons();
            if (snacksAndBoissons.getQuantity()!=null){
                snacksAndBoissons.setQuantity(snacksAndBoissons.getQuantity() - snackOrBoisson.getQuantity());
            }
        }
    }

    @Override
    public String newVisitOfTeam(String result) {
        Utilisateur user = utilisateurRepository.findById(UUID.fromString(result)).orElse(null);
        if(user==null)
            return "redirect:/reception/visit-of-team";
        if(user.getDiscriminatorValue().equals("SUBSCRIBER") || user.getDiscriminatorValue().equals("BUSINESS")){
            return "redirect:/reception/visit-of-team";
        }else{
            return "redirect:/reception/new-visit-of-team-profile?userId=" + user.getId().toString();
        }
    }

    @Override
    public String newVisitOfTeamProfile(String result) {
        try {
            Utilisateur user = utilisateurRepository.findById(UUID.fromString(result)).orElse(null);

            ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

            ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

            LocalDate localDate = moroccoDateTime.toLocalDate();

            VisitOfTeam visitOfTeam = visitOfTeamRepository.getByDateAndUserAndEndTime(user.getId(), localDate);

            if (visitOfTeam != null)
                return "redirect:/reception/visit-of-team-profile?visitId=" + visitOfTeam.getId();

            VisitOfTeam newVisitOfTeam = createVisitForTeam(user);

            return "redirect:/reception/visit-of-team-profile?visitId=" + newVisitOfTeam.getId();
        } catch (Exception e) {

            return "error";
        }
    }
    @Override
    public VisitOfTeam createVisitForTeam(Utilisateur user) {

        VisitOfTeam newVisit = new VisitOfTeam();

        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

        LocalDate localDate = moroccoDateTime.toLocalDate();

        LocalTime localTime = moroccoDateTime.toLocalTime();

        newVisit.setDay(localDate);

        newVisit.setStartTime(localTime);

        newVisit.setUtilisateur(user);

        return visitOfTeamRepository.save(newVisit);

    }

    @Override
    public void saveSnacksToVisitOfTeam(UUID visitId, SnackForm snackForm) {
        VisitOfTeam visit = visitOfTeamRepository.findById(visitId).orElseThrow(() -> new IllegalArgumentException("Invalid visit ID"));

        List<SnacksAndBoissonsOfVisit> snacksList = visit.getSnacksAndBoissonsOfVisits();

        snackForm.getSnackQuantities().forEach((snackId, quantity) -> {

            if (quantity == null)
                quantity = 0;

            if (quantity > 0) {

                SnacksAndBoissons snack = snacksAndBoissonsRepository.findById(snackId).orElseThrow(() -> new IllegalArgumentException("Invalid snack ID"));

                boolean snackExists = false;

                if (!snacksList.isEmpty()) {

                    for (SnacksAndBoissonsOfVisit snackVisit : snacksList) {

                        if (snackVisit.getSnacksAndBoissons().getId().equals(snackId)) {

                            snackVisit.setQuantity(snackVisit.getQuantity() + quantity);

                            snackExists = true;

                            break;
                        }
                    }

                }
                if (!snackExists) {

                    SnacksAndBoissonsOfVisit newSnackVisit = new SnacksAndBoissonsOfVisit();

                    newSnackVisit.setSnacksAndBoissons(snack);

                    newSnackVisit.setVisitOfTeam(visit);

                    newSnackVisit.setQuantity(quantity);

                    newSnackVisit.setSelling_price(snack.getSelling_price());

                    newSnackVisit.setPurchase_price(snack.getPurchase_price());

                    snacksList.add(newSnackVisit);
                }
                if (snack.getQuantity()!=null){
                    snack.setQuantity(snack.getQuantity()-quantity);
                    snacksAndBoissonsRepository.save(snack);
                }
            }
        });

        visitOfTeamRepository.save(visit);
    }

    @Override
    public void deleteSnackForVisitTeam(UUID visitId, UUID snackId) {
        VisitOfTeam visit = visitOfTeamRepository.findById(visitId).orElseThrow(() -> new IllegalArgumentException("Invalid visit ID"));

        List<SnacksAndBoissonsOfVisit> snacksList = visit.getSnacksAndBoissonsOfVisits();

        SnacksAndBoissonsOfVisit snackVisit = snacksList.stream()
                .filter(sv -> sv.getSnacksAndBoissons().getId().equals(snackId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Snack not found in visit"));
        SnacksAndBoissons snacksAndBoissons = snackVisit.getSnacksAndBoissons();
        if (snacksAndBoissons.getQuantity()!=null){
            snacksAndBoissons.setQuantity(snacksAndBoissons.getQuantity()+snackVisit.getQuantity());
            snacksAndBoissonsRepository.save(snacksAndBoissons);
        }

        snacksList.remove(snackVisit);

        visitOfTeamRepository.save(visit);
    }

    @Override
    public void updateSnackQuantityForTeam(UUID visitId, UUID snackId, int quantity) {
        VisitOfTeam visit = visitOfTeamRepository.findById(visitId).orElseThrow(() -> new RuntimeException("Visit not found"));

        Optional<SnacksAndBoissonsOfVisit> optionalSnacksAndBoissonsOfVisit = visit.getSnacksAndBoissonsOfVisits()
                .stream()
                .filter(snack -> snack.getSnacksAndBoissons().getId().equals(snackId))
                .findFirst();

        if (optionalSnacksAndBoissonsOfVisit.isPresent()) {
            SnacksAndBoissonsOfVisit snacksAndBoissonsOfVisit = optionalSnacksAndBoissonsOfVisit.get();
            SnacksAndBoissons snacksAndBoissons = snacksAndBoissonsOfVisit.getSnacksAndBoissons();
            if (snacksAndBoissons.getQuantity()!=null){
                int q1 =  quantity-snacksAndBoissonsOfVisit.getQuantity();
                snacksAndBoissons.setQuantity(snacksAndBoissons.getQuantity()-q1);
                snacksAndBoissonsRepository.save(snacksAndBoissons);
            }
            snacksAndBoissonsOfVisit.setQuantity(quantity);
            // Save the updated entity
            snacksAndBoissonsOfVisitRepository.save(snacksAndBoissonsOfVisit);
        } else {
            throw new RuntimeException("Snack not found for the given visit");
        }
    }

    @Override
    public double totalePriceOfSnackAndBoissons(List<SnacksAndBoissonsOfVisit> snacksAndBoissonsOfVisit) {
        double somme=0.0;
        for (SnacksAndBoissonsOfVisit snackOrBoisson : snacksAndBoissonsOfVisit){
            somme +=snackOrBoisson.getSelling_price()*snackOrBoisson.getQuantity();
        }
        return somme;
    }

    @Override
    public LocalTime maximumTimeAvailabale(LocalDate reservationDate, LocalTime reservationTime) {
        List<LocalTime> listReservationsStartTime = visitOfRoomRepository.maximumTimeAvailabale(reservationDate,reservationTime);
        if (listReservationsStartTime.isEmpty())return null;
        else return listReservationsStartTime.getFirst();
    }

    @Override
    public LocalTime maximumTimeAvailabaleDesk(LocalDate reservationDate, LocalTime reservationTime) {
        List<LocalTime> listReservationsStartTime = visitOfDeskRepository.maximumTimeAvailabale(reservationDate,reservationTime);
        if (listReservationsStartTime.isEmpty())return null;
        else return listReservationsStartTime.getFirst();
    }

}
