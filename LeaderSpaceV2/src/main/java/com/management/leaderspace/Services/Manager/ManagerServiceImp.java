package com.management.leaderspace.Services.Manager;

import com.management.leaderspace.Entities.*;
import com.management.leaderspace.Repositories.*;
import com.management.leaderspace.model.DesignationForm;
import lombok.AllArgsConstructor;

import org.antlr.v4.runtime.misc.LogManager;

import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;

import org.springframework.cglib.core.Local;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class ManagerServiceImp implements ManagerService {
    private final ServiceTypeRepository serviceTypeRepository;
    private final VisitOfRoomRepository visitOfRoomRepository;
    private final DataSourcePoolMetadataProvider hikariPoolDataSourceMetadataProvider;
    private final VisitOfDeskRepository visitOfDeskRepository;
    private final VisitOfTeamRepository visitOfTeamRepository;
    ManagerRepository managerRepository;
    DevisRepository devisRepository;
    DesignationRepository designationRepository;
    private SnacksAndBoissonsRepository snacksAndBoissonsRepository;
    FactureRepository factureRepository;
    private VisitRepository visitRepository;
    private SnacksAndBoissonsOfVisitRepository snacksAndBoissonsOfVisitRepository;
    private SubscriptionHistoryRepository subscriptionHistoryRepository;
    private SnacksAndBoissonsHistoryRepository snacksAndBoissonsHistoryRepository;
    SubscriptionTypeRepository subscriptionTypeRepository;
    PasswordEncoder passwordEncoder;
    SubscriberRepository subscriberRepository;
    ContratRepository contratRepository;

    @Override
    public Manager getProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return (Manager) managerRepository.findByEmail(username);
    }

    @Override
    public SnacksAndBoissons saveSnackWithImage(String name, double sellingPrice, double purchasePrice, Integer requiredQuantity, String type, MultipartFile imageFile, Long quantity) throws IOException {
        SnacksAndBoissons snack = new SnacksAndBoissons();
        snack.setName(name);
        snack.setSelling_price(sellingPrice);
        snack.setPurchase_price(purchasePrice);
        snack.setRequiredQuantity(requiredQuantity);
        snack.setType(type);
        if (!imageFile.isEmpty()) {
            byte[] imageData = imageFile.getBytes();
            snack.setImage(imageData);
        }
        snack.setQuantity(quantity);

        SnacksAndBoissons savedSnack = snacksAndBoissonsRepository.save(snack);

        SnacksAndBoissonsHistory snackHistory = new SnacksAndBoissonsHistory();
        snackHistory.setQuantity(savedSnack.getQuantity());
        snackHistory.setSelling_price(savedSnack.getSelling_price());
        snackHistory.setPurchase_price(savedSnack.getPurchase_price());
        snackHistory.setSnacksAndBoissons(savedSnack);

        ZonedDateTime nowInMorocco = ZonedDateTime.now(ZoneId.of("Africa/Casablanca"));
        LocalDate currentDate = nowInMorocco.toLocalDate();
        LocalTime currentTime = nowInMorocco.toLocalTime();

        snackHistory.setPurchase_date(currentDate);
        snackHistory.setPurchase_time(currentTime);

        snacksAndBoissonsHistoryRepository.save(snackHistory);

        return savedSnack;
    }

    @Override
    public List<SnacksAndBoissons> getAllSnacks() {
        return snacksAndBoissonsRepository.findAll();
    }

    @Override
    public ServiceType getServiceById(UUID uuid) {
        return serviceTypeRepository.findById(uuid).orElse(null);
    }

    @Override
    public void saveServicesToDevis(UUID devisId, DesignationForm designationForm) {
        Devis devis = devisRepository.findById(devisId).orElseThrow(() -> new IllegalArgumentException("Invalid devis ID"));

        List<DesignationOfDevis> DesignationsList = devis.getDesignations();

        designationForm.getServiceQuantities().forEach((designationId, quantity) -> {

            if (quantity == null)
                quantity = 0;

            if (quantity > 0) {

                ServiceType serviceType = serviceTypeRepository.findById(designationId).orElse(null);
                SubscriptionType subscriptionType = subscriptionTypeRepository.findById(designationId).orElse(null);

                boolean serviceExists = false;

                if (!DesignationsList.isEmpty()) {

                    for (DesignationOfDevis designationDevis : DesignationsList) {

                        if (designationDevis.getServiceType() != null && designationDevis.getSubscriptionType() == null) {
                            if (designationDevis.getServiceType().getId().equals(designationId)) {

                                designationDevis.setQuantity(designationDevis.getQuantity() + quantity);

                                serviceExists = true;

                                break;
                            }
                        }else {
                            if (designationDevis.getSubscriptionType().getId().equals(designationId)) {

                                designationDevis.setQuantity(designationDevis.getQuantity() + quantity);

                                serviceExists = true;

                                break;
                            }
                        }
                    }

                }
                if (!serviceExists) {

                    DesignationOfDevis designationOfDevis = new DesignationOfDevis();

                    designationOfDevis.setServiceType(serviceType);

                    designationOfDevis.setSubscriptionType(subscriptionType);

                    designationOfDevis.setDevis(devis);

                    designationOfDevis.setQuantity(quantity);

                    DesignationsList.add(designationOfDevis);
                }
            }
        });

        devisRepository.save(devis);
    }

    @Override
    public void saveServicesToFacture(UUID factureId, DesignationForm designationForm) {
        Facture facture = factureRepository.findById(factureId).orElseThrow(() -> new IllegalArgumentException("Invalid devis ID"));

        List<DesignationOfDevis> DesignationsList = facture.getDesignations();

        designationForm.getServiceQuantities().forEach((designationId, quantity) -> {

            if (quantity == null)
                quantity = 0;

            if (quantity > 0) {

                SubscriptionType subscriptionType = subscriptionTypeRepository.findById(designationId).orElseThrow(() -> new IllegalArgumentException("Invalid designation ID"));

                boolean subscriptionTypeExists = false;

                if (!DesignationsList.isEmpty()) {

                    for (DesignationOfDevis designationDevis : DesignationsList) {

                        if (designationDevis.getSubscriptionType().getId().equals(designationId)) {

                            designationDevis.setQuantity(designationDevis.getQuantity() + quantity);

                            subscriptionTypeExists = true;

                            break;
                        }
                    }

                }
                if (!subscriptionTypeExists) {

                    DesignationOfDevis designationOfDevis = new DesignationOfDevis();

                    designationOfDevis.setSubscriptionType(subscriptionType);

                    designationOfDevis.setFacture(facture);

                    designationOfDevis.setQuantity(quantity);

                    DesignationsList.add(designationOfDevis);
                }
            }
        });

        factureRepository.save(facture);
    }

    @Override
    public List<Visit> getAllVisitsBetweenStartDayAndEndTime(LocalDate dateDebut, LocalDate dateFin) {
        return visitRepository.getAllVisitsBetweenStartDayAndEndTime(dateDebut, dateFin);
    }

    @Override
    public Map<UUID, Double> getTotalPriceOfSnacksAndBoissons(List<Visit> listAllVisitsBetweenStartDayAndEndTime) {

        Map<UUID, Double> mapTotalPriceOfSnacksAndBoissonsByVisit = null;
        if (listAllVisitsBetweenStartDayAndEndTime != null) {
            mapTotalPriceOfSnacksAndBoissonsByVisit = new HashMap<>();
            for (Visit visit : listAllVisitsBetweenStartDayAndEndTime) {
                Double val = snacksAndBoissonsOfVisitRepository.getTotalPriceOfSnacksAndBoissonsByVisit(visit.getId());
                if (val != null) {
                    //System.out.println("listSnacksAndBoissonsOfVisits ========== "+visit.getSnacksAndBoissonsOfVisits());
                    mapTotalPriceOfSnacksAndBoissonsByVisit.put(visit.getId(), val);
                } else {
                    mapTotalPriceOfSnacksAndBoissonsByVisit.put(visit.getId(), 0.0);
                }
            }
        }
        return mapTotalPriceOfSnacksAndBoissonsByVisit;
    }

    @Override
    public Double calculeTotale(List<Visit> listVisits, Map<UUID, Double> map) {
        double total = 0.0;
        if (listVisits != null) {
            for (Visit visit : listVisits) {
                if (!visit.getSnacksAndBoissonsOfVisits().isEmpty()) {
                    total += map.get(visit.getId());
                }
                total += visit.getService_price()+visit.getService_suplementaire_price();
            }
        }
        return total;
    }

    @Override
    public List<SubscriptionHistory> getAllSubscriptionsByDateDebutBetweenStartDayAndEndDay(LocalDate date_debut, LocalDate date_fin) {
        return subscriptionHistoryRepository.findAllSubscriptionsByDateDebutBetweenStartDayAndEndDay(date_debut, date_fin);
    }

    @Override
    public Double calculeTotaleOfSubscriptions(List<SubscriptionHistory> listSubscriptions) {
        double total = 0.0;
        if (listSubscriptions != null) {
            for (SubscriptionHistory subscriptionHistory : listSubscriptions) {
                total += subscriptionHistory.getPrice();
            }
        }
        return total;
    }

    @Override
    public void saveSubscriber(Subscriber subscriber,UUID subscriptionType_id) {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

        SubscriptionType subscriptionType = subscriptionTypeRepository.findById(subscriptionType_id).orElse(null);
        subscriber.setSubscription_type(subscriptionType);
        LocalDate localDate = moroccoDateTime.toLocalDate();


        LocalDate endDate = localDate.plusMonths(subscriber.getSubscription_type().getFlexibility_duration()*subscriber.getQuantity());

        subscriber.setDate_debut(localDate);

        subscriber.setDate_fin(endDate);


        subscriber.setNumber_of_visits(subscriber.getSubscription_type().getDuration()*subscriber.getQuantity());


        subscriber.setPassword(passwordEncoder.encode(subscriber.getPassword()));

        subscriber.setPrice_actuel_d_abonnemet(subscriber.getSubscription_type().getPrice()*subscriber.getQuantity());


        subscriberRepository.save(subscriber);

        SubscriptionHistory subscriptionHistory = new SubscriptionHistory();
        subscriptionHistory.setSubscriber(subscriber);
        subscriptionHistory.setStartDate(localDate);
        subscriptionHistory.setEndDate(endDate);
        subscriptionHistory.setSubscriptionType(subscriber.getSubscription_type());
        subscriptionHistory.setQuantity(subscriber.getQuantity());
        subscriptionHistory.setPrice(subscriber.getPrice_actuel_d_abonnemet());
        subscriptionHistoryRepository.save(subscriptionHistory);
    }

    @Override
    public void saveUpdateSubscriber(Subscriber subscriber, SubscriptionHistory subscriptionHistory,UUID subscriptionType_id) {
        Subscriber subscriberToUpdate = subscriberRepository.findById(subscriber.getId()).orElse(null);

        assert subscriberToUpdate != null;
        LocalDate localDate = subscriberToUpdate.getDate_debut();

        SubscriptionType subscriptionType = subscriptionTypeRepository.findById(subscriptionType_id).orElse(null);
        subscriberToUpdate.setSubscription_type(subscriptionType);
        subscriberToUpdate.setQuantity(subscriber.getQuantity());
        subscriberToUpdate.setCIN(subscriber.getCIN());
        subscriberToUpdate.setFirst_name(subscriber.getFirst_name());
        subscriberToUpdate.setLast_name(subscriber.getLast_name());
        subscriberToUpdate.setPhone(subscriber.getPhone());
        LocalDate endDate = localDate.plusMonths(subscriberToUpdate.getSubscription_type().getFlexibility_duration()*subscriberToUpdate.getQuantity());


        subscriberToUpdate.setDate_fin(endDate);


        subscriberToUpdate.setNumber_of_visits(subscriberToUpdate.getSubscription_type().getDuration()*subscriberToUpdate.getQuantity());


        //subscriber.setPassword(passwordEncoder.encode(subscriber.getPassword()));

        subscriberToUpdate.setPrice_actuel_d_abonnemet(subscriberToUpdate.getSubscription_type().getPrice()*subscriberToUpdate.getQuantity());


        subscriberRepository.save(subscriberToUpdate);
        subscriptionHistory.setSubscriber(subscriberToUpdate);
        subscriptionHistory.setEndDate(endDate);
        subscriptionHistory.setSubscriptionType(subscriberToUpdate.getSubscription_type());
        subscriptionHistory.setQuantity(subscriberToUpdate.getQuantity());
        subscriptionHistory.setPrice(subscriberToUpdate.getPrice_actuel_d_abonnemet());
        subscriptionHistoryRepository.save(subscriptionHistory);
    }

    @Override
    public void saveResubscribeOfSubscriber(Subscriber subscriber, UUID subscriptionTypeId) {
        Subscriber subscriberToUpdate = subscriberRepository.findById(subscriber.getId()).orElse(null);
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");
        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);
        LocalDate localDate = moroccoDateTime.toLocalDate();
        assert subscriberToUpdate != null;
        SubscriptionType subscriptionType = subscriptionTypeRepository.findById(subscriptionTypeId).orElse(null);
        subscriberToUpdate.setSubscription_type(subscriptionType);

        subscriberToUpdate.setQuantity(subscriber.getQuantity());
        LocalDate endDate = localDate.plusMonths(subscriberToUpdate.getSubscription_type().getFlexibility_duration() * subscriberToUpdate.getQuantity());


        subscriberToUpdate.setDate_fin(endDate);


        subscriberToUpdate.setNumber_of_visits(subscriberToUpdate.getSubscription_type().getDuration() * subscriberToUpdate.getQuantity());


        //subscriber.setPassword(passwordEncoder.encode(subscriber.getPassword()));

        subscriberToUpdate.setPrice_actuel_d_abonnemet(subscriberToUpdate.getSubscription_type().getPrice() * subscriberToUpdate.getQuantity());


        subscriberRepository.save(subscriberToUpdate);

        SubscriptionHistory subscriptionHistory = new SubscriptionHistory();
        subscriptionHistory.setSubscriber(subscriberToUpdate);
        subscriptionHistory.setStartDate(localDate);
        subscriptionHistory.setEndDate(endDate);
        subscriptionHistory.setSubscriptionType(subscriberToUpdate.getSubscription_type());
        subscriptionHistory.setQuantity(subscriberToUpdate.getQuantity());
        subscriptionHistory.setPrice(subscriberToUpdate.getPrice_actuel_d_abonnemet());
        subscriptionHistoryRepository.save(subscriptionHistory);
    }

    @Override
    public Double calculeSommeOfConsommations(Map<UUID, Double> mapConsommations) {
        double somme = 0.0;
        for (Double sommeConsommations : mapConsommations.values()) {
            somme += sommeConsommations;
        }
        return somme;
    }

    @Override
    public Double calculeSommeConsommationForRoomParticipants(Map<UUID, Map<UUID, Double>> mapRoomParticipants) {
        double somme = 0.0;
        for (Map<UUID,Double> inerMap : mapRoomParticipants.values()) {
            for (Double val : inerMap.values()) {
                somme +=val;
            }
        }
        return somme;
    }

    @Override
    public Double calculeSommeOfServicePriceForNormaleVisits(List<Visit> allVisits) {
        double somme = 0.0;
        if (allVisits != null) {
            for (Visit visit : allVisits) {
                if (visit.getNotSubscriber()!=null){
                    somme+=visit.getService_price();
                }
            }
        }
        return somme;
    }

    @Override
    public Double calculeSommeOfServicePriceForRoom(List<VisitOfRoom> allVisitsOfRoom) {
        double somme = 0.0;
        for (VisitOfRoom visitOfRoom : allVisitsOfRoom) {
            somme+=visitOfRoom.getService_room_price();
        }
        return somme;
    }

    @Override
    public Double calculeSommeOfServicePriceForDesk(List<VisitOfDesk> allVisitsOfDesk) {
        double somme = 0.0;
        for (VisitOfDesk visitOfDesk : allVisitsOfDesk) {
            somme+=visitOfDesk.getService_desk_price();
        }
        return somme;
    }

    @Override
    public List<Visit> getVisitsOfSubscribers() {
        return visitRepository.getVisitsOfSubscribers();
    }

    @Override
    public List<Visit> getVisitsOfNotSubscribers() {
        return visitRepository.getVisitsOfNotSubscribers();
    }

    @Override
    public List<VisitOfRoom> getVisitsOfRoom() {
        return visitOfRoomRepository.getVisitOfRoom();
    }

    @Override
    public List<VisitOfDesk> getVisitsOfDesk() {
        return visitOfDeskRepository.getVisitsOfDesk();
    }

    public List<VisitOfRoom> findVisitOfRoomByDate(LocalDate dateDebut, LocalDate dateFin) {
        return visitOfRoomRepository.findVisitsOfRoomByDate(dateDebut, dateFin);
    }

    @Override
    public Map<UUID, Double> sommeOfSnacksAndBoissonsOfRoom(List<VisitOfRoom> listAllVisitOfRoom) {
        Map<UUID,Double> mapSommeOfSnacksAndBoissonsOfRoom = new HashMap<>();
        if (listAllVisitOfRoom != null) {
            for (VisitOfRoom visitOfRoom : listAllVisitOfRoom) {
                double somme = 0.0;
                for (SnacksAndBoissonsOfVisit snacksAndBoissonsOfVisit:visitOfRoom.getSnacksAndBoissonsOfVisitRoom()){
                    somme+=snacksAndBoissonsOfVisit.getSelling_price()*snacksAndBoissonsOfVisit.getQuantity();
                }
                mapSommeOfSnacksAndBoissonsOfRoom.put(visitOfRoom.getId(), somme);
            }
        }
        return mapSommeOfSnacksAndBoissonsOfRoom;
    }

    @Override
    public Map<UUID, Map<UUID,Double>> sommeOfSnacksAndBoissonsForParticipantOfVisitRoom(List<VisitOfRoom> allVisitsOfRoom) {
        Map<UUID,Map<UUID,Double>> mapSommeOfSnacksAndBoissonsForParticipantOfVisitRoom = new HashMap<>();
        if (allVisitsOfRoom != null) {
            for (VisitOfRoom visitOfRoom : allVisitsOfRoom) {
                Map<UUID,Double> inerMap = new HashMap<>();;
                if (!visitOfRoom.getParticipant().isEmpty()) {
                    for (ParticipantOfvisitRoom participantOfvisitRoom : visitOfRoom.getParticipant()) {
                        double somme = 0.0;
                        for (SnacksAndBoissonsOfVisit snacksAndBoissonsOfVisit : participantOfvisitRoom.getSnacksAndBoissonsOfVisits()){
                            somme+=snacksAndBoissonsOfVisit.getQuantity()*snacksAndBoissonsOfVisit.getSelling_price();
                        }
                        inerMap.put(participantOfvisitRoom.getId(), somme+participantOfvisitRoom.getService_suplementaire_price());

                    }
                }
                mapSommeOfSnacksAndBoissonsForParticipantOfVisitRoom.put(visitOfRoom.getId(), inerMap);
            }
        }
        return mapSommeOfSnacksAndBoissonsForParticipantOfVisitRoom;
    }

    @Override
    public Map<UUID, Double> calculeSommeForVisitOfRoom(List<VisitOfRoom> listAllVisitOfRoom,Map<UUID,Double> sommeOfSnacksAndBoissonsOfRoom,Map<UUID,Map<UUID,Double>> sommeOfSnacksAndBoissonsForParticipantOfVisitRoom) {
        Map<UUID,Double> mapCalculeSommeForVisitOfRoom = new HashMap<>();
        if (listAllVisitOfRoom!=null){
            for (VisitOfRoom visitOfRoom : listAllVisitOfRoom) {
                double somme = 0.0;
                somme+=(visitOfRoom.getService_room_price()+visitOfRoom.getService_suplementaire_price()+sommeOfSnacksAndBoissonsOfRoom.get(visitOfRoom.getId()));
                if (sommeOfSnacksAndBoissonsForParticipantOfVisitRoom.get(visitOfRoom.getId())!=null) {
                    for (ParticipantOfvisitRoom participantOfvisitRoom : visitOfRoom.getParticipant()) {
                        somme+=sommeOfSnacksAndBoissonsForParticipantOfVisitRoom.get(visitOfRoom.getId()).get(participantOfvisitRoom.getId());
                    }
                }
                mapCalculeSommeForVisitOfRoom.put(visitOfRoom.getId(), somme);
            }
        }
        return mapCalculeSommeForVisitOfRoom;
    }

    @Override
    public Double calculeTotaleOfAllVisitsOfRoom(Map<UUID, Double> sommesForVisitsOfRoom) {
        double s=0.0;
        if (!sommesForVisitsOfRoom.isEmpty()){
            for (Double value : sommesForVisitsOfRoom.values()) {
                s += value;
            }
        }
        return s;

    }

    @Override
    public List<VisitOfDesk> findVisitOfDeskByDate(LocalDate dateDebut, LocalDate dateFin) {
        return visitOfDeskRepository.visitsOfTodayOfDesk(dateDebut , dateFin);
    }

    @Override
    public Map<UUID, Double> sommeOfsnacksAndBoissonsByVisit(List<VisitOfDesk> allVisitsOfDeskByDay) {
        Map<UUID,Double> sommeOfsnacksAndBoissonsByVisit = new HashMap<>();
        if (allVisitsOfDeskByDay != null) {
            for (VisitOfDesk visitOfDesk : allVisitsOfDeskByDay) {
                double somme = 0.0;
                for (SnacksAndBoissonsOfVisit snacksAndBoissonsOfVisit : visitOfDesk.getSnacksAndBoissonsOfVisits()) {
                    somme+=snacksAndBoissonsOfVisit.getSelling_price()*snacksAndBoissonsOfVisit.getQuantity();
                }
                sommeOfsnacksAndBoissonsByVisit.put(visitOfDesk.getId(), somme);
            }
        }
        return sommeOfsnacksAndBoissonsByVisit;
    }

    @Override
    public Double totaleOfVisitsOfDesk(List<VisitOfDesk> allVisitOfDeskByDate, Map<UUID, Double> sommeOfsnacksAndBoissonsByVisit) {
        double somme = 0.0;
        if (allVisitOfDeskByDate != null) {
            for (VisitOfDesk visitOfDesk : allVisitOfDeskByDate) {
                somme+=sommeOfsnacksAndBoissonsByVisit.get(visitOfDesk.getId())+visitOfDesk.getService_desk_price()+visitOfDesk.getService_suplementaire_price();
            }
        }
        return somme;
    }

    @Override
    public Map<LocalDate, Double> totaleVisitsNormaleCharts(LocalDate dateDebut, LocalDate dateFin) {
        List<Visit> visits = visitRepository.getAllVisitsBetweenStartDayAndEndTime(dateDebut,dateFin);
        Map<LocalDate,Double> totaleVisitsCharts = new HashMap<>();
        if (!visits.isEmpty()) {
            for (Visit visit : visits){
                Double sommme=0.0;
                sommme+=visit.getService_price()+visit.getService_suplementaire_price()+ snacksAndBoissonsOfVisitRepository.getTotalPriceOfSnacksAndBoissonsByVisit(visit.getId());
                if (totaleVisitsCharts.containsKey(visit.getDay())) {
                    sommme+=totaleVisitsCharts.get(visit.getDay());
                }
                totaleVisitsCharts.put(visit.getDay(), sommme);
            }
        }
        return totaleVisitsCharts;
    }

    @Override
    public Map<LocalDate, Double> totaleVisitsRoomCharts(LocalDate dateDebut, LocalDate dateFin) {
        List<VisitOfRoom> allVisitsRoom = visitOfRoomRepository.findVisitsOfRoomByDate(dateDebut,dateFin);
        Map<LocalDate,Double> totaleVisitsRoomeCharts = new HashMap<>();
        if (!allVisitsRoom.isEmpty()) {
            for (VisitOfRoom visitOfRoom : allVisitsRoom) {
                Double somme = 0.0;
                somme+=visitOfRoom.getService_room_price()+visitOfRoom.getService_suplementaire_price()+ snacksAndBoissonsOfVisitRepository.getTotalPriceOfSnacksAndBoissonsByVisitOfRoom(visitOfRoom.getId());
                for (ParticipantOfvisitRoom participantOfvisitRoom : visitOfRoom.getParticipant()) {
                    somme+= snacksAndBoissonsOfVisitRepository.getTotalPriceOfSnacksAndBoissonsByParticipant(participantOfvisitRoom.getId())+participantOfvisitRoom.getService_suplementaire_price();
                }
                if (totaleVisitsRoomeCharts.containsKey(visitOfRoom.getDay())) {
                    somme+=totaleVisitsRoomeCharts.get(visitOfRoom.getDay());
                }
                totaleVisitsRoomeCharts.put(visitOfRoom.getDay(), somme);
            }
        }
        return totaleVisitsRoomeCharts;
    }

    @Override
    public Map<LocalDate, Double> totaleVisitOfDesk(LocalDate dateDebut, LocalDate dateFin) {
        List<VisitOfDesk> allVisitsOfDesk = visitOfDeskRepository.visitsOfTodayOfDesk(dateDebut,dateFin);
        Map<LocalDate,Double> totaleVisitOfDesk = new HashMap<>();
        if (!allVisitsOfDesk.isEmpty()) {
            for (VisitOfDesk visitOfDesk : allVisitsOfDesk) {
                Double somme = 0.0;
                somme+=visitOfDesk.getService_desk_price()+visitOfDesk.getService_suplementaire_price()+ snacksAndBoissonsOfVisitRepository.getTotalPriceOfSnacksAndBoissonsByVisitOfDesk(visitOfDesk.getId());
                if (totaleVisitOfDesk.containsKey(visitOfDesk.getDay())) {
                    somme+=totaleVisitOfDesk.get(visitOfDesk.getDay());
                }
                totaleVisitOfDesk.put(visitOfDesk.getDay(), somme);
            }
        }
        return totaleVisitOfDesk;
    }

    @Override
    public Map<LocalDate, Double> totaleSubscriptions(LocalDate dateDebut, LocalDate dateFin) {
        List<SubscriptionHistory> allSubsciptions = subscriptionHistoryRepository.findAllSubscriptionsByDateDebutBetweenStartDayAndEndDay(dateDebut,dateFin);
        Map<LocalDate,Double> totaleSubscriptions = new HashMap<>();
        if (!allSubsciptions.isEmpty()) {
            for (SubscriptionHistory subscriptionHistory : allSubsciptions) {
                Double somme = 0.0;
                somme+=subscriptionHistory.getPrice();
                if (totaleSubscriptions.containsKey(subscriptionHistory.getStartDate())) {
                    somme+=totaleSubscriptions.get(subscriptionHistory.getStartDate());
                }
                totaleSubscriptions.put(subscriptionHistory.getStartDate(), somme);
            }
        }
        return totaleSubscriptions;
    }

    @Override
    public Map<LocalDate, Double> totaleTurnoverForCharts(Map<LocalDate, Double> totaleVisitsNormaleCharts, Map<LocalDate, Double> totaleVisitsRoomCharts, Map<LocalDate, Double> totaleVisitOfDesk, Map<LocalDate,Double> totaleVisitsTeamChartByDates, Map<LocalDate, Double> totaleSubscriptions, Map<LocalDate, Double> totaleContractsCherts) {

        for (LocalDate date : totaleVisitsRoomCharts.keySet()) {
            if (totaleVisitsNormaleCharts.containsKey(date)) {
                totaleVisitsNormaleCharts.put(date, totaleVisitsNormaleCharts.get(date)+totaleVisitsRoomCharts.get(date));
            }else {
                totaleVisitsNormaleCharts.put(date,totaleVisitsRoomCharts.get(date));
            }

        }
        for (LocalDate date : totaleVisitOfDesk.keySet()) {
            if (totaleVisitsNormaleCharts.containsKey(date)) {
                totaleVisitsNormaleCharts.put(date, totaleVisitsNormaleCharts.get(date)+totaleVisitOfDesk.get(date));
            }else {
                totaleVisitsNormaleCharts.put(date,totaleVisitOfDesk.get(date));
            }

        }
        for (LocalDate date : totaleSubscriptions.keySet()) {
            if (totaleVisitsNormaleCharts.containsKey(date)) {
                totaleVisitsNormaleCharts.put(date, totaleVisitsNormaleCharts.get(date)+totaleSubscriptions.get(date));
            }else {
                totaleVisitsNormaleCharts.put(date,totaleSubscriptions.get(date));
            }
        }
        for (LocalDate date : totaleContractsCherts.keySet()) {
            if (totaleVisitsNormaleCharts.containsKey(date)) {
                totaleVisitsNormaleCharts.put(date, totaleVisitsNormaleCharts.get(date)+totaleContractsCherts.get(date));
            }else {
                totaleVisitsNormaleCharts.put(date,totaleContractsCherts.get(date));
            }
        }
        for (LocalDate date : totaleVisitsTeamChartByDates.keySet()) {
            if (totaleVisitsNormaleCharts.containsKey(date)) {
                totaleVisitsNormaleCharts.put(date, totaleVisitsNormaleCharts.get(date)+totaleVisitsTeamChartByDates.get(date));
            }else {
                totaleVisitsNormaleCharts.put(date,totaleVisitsTeamChartByDates.get(date));
            }
        }
        return totaleVisitsNormaleCharts;
    }

    @Override
    public Map<LocalDate, Double> totaleContractsCherts(LocalDate dateDebut, LocalDate dateFin) {
        List<Contrat> allContracts = contratRepository.allContractByDate(dateDebut,dateFin);
        Map<LocalDate,Double> totaleContractsCherts = new HashMap<>();
        if (!allContracts.isEmpty()) {
            for (Contrat contrat : allContracts) {
                Double somme = 0.0;
                somme+=contrat.getMontant_de_location_chiffre();
                if (totaleContractsCherts.containsKey(contrat.getDate())) {
                    somme+=totaleContractsCherts.get(contrat.getDate());
                }
                totaleContractsCherts.put(contrat.getDate(), somme);
            }
        }
        return totaleContractsCherts;
    }

    @Override
    public Double totaleTurnoverCherts(Map<LocalDate, Double> totaleTurnoverForCharts) {
        Double somme = 0.0;
        for (LocalDate date : totaleTurnoverForCharts.keySet()) {
            somme+=totaleTurnoverForCharts.get(date);
        }
        return somme;
    }

    @Override
    public Map<UUID, Double> sommeOfSnacksAndBoissonsByVisitFomTeam(List<VisitOfTeam> allVisitsOfTeam) {
        Map<UUID,Double> sommeMap = null;
        if (!allVisitsOfTeam.isEmpty()) {
            sommeMap = new HashMap<>();
            for (VisitOfTeam visitOfTeam : allVisitsOfTeam) {
                sommeMap.put(visitOfTeam.getId(),snacksAndBoissonsOfVisitRepository.sommePriceOfSnacksAndBoissonsByVisitOfTeam(visitOfTeam.getId()));
            }
        }
        return sommeMap;
    }

    @Override
    public Double sommeSnacksAndBoissonsForVisitsTeam(Map<UUID, Double> sommeOfSnacksAndBoissonsByVisitFomTeam) {
        double somme = 0.0;
        for (Double value : sommeOfSnacksAndBoissonsByVisitFomTeam.values()) {
            somme+=value;
        }
        return somme;
    }

    @Override
    public Map<LocalDate, Double> totaleVisitsTeamChartByDates(LocalDate dateDebut, LocalDate dateFin) {
        List<VisitOfTeam> allVisitsTeam= visitOfTeamRepository.allVisitOfTeamByDate(dateDebut,dateFin);
        Map<LocalDate,Double> totaleVisitsTeamCharts = new HashMap<>();
        if (!allVisitsTeam.isEmpty()) {
            for (VisitOfTeam visitOfTeam : allVisitsTeam) {
                double somme = 0.0;
                somme += visitOfTeam.getService_suplementaire_price()+snacksAndBoissonsOfVisitRepository.sommePriceOfSnacksAndBoissonsByVisitOfTeam(visitOfTeam.getId());
                if (totaleVisitsTeamCharts.containsKey(visitOfTeam.getDay())) {
                    somme+=totaleVisitsTeamCharts.get(visitOfTeam.getDay());
                }
                totaleVisitsTeamCharts.put(visitOfTeam.getDay(),somme);
            }
        }
        return totaleVisitsTeamCharts;
    }
}