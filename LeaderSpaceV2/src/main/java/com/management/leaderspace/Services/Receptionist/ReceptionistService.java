package com.management.leaderspace.Services.Receptionist;

import com.management.leaderspace.Entities.*;
import com.management.leaderspace.model.SnackForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReceptionistService {
    Receptionist getProfile();

    Visit createVisitForSubscriber(Subscriber subscriber) throws ParseException;

    void updateSnackQuantity(UUID visitId, UUID snackId, int quantity);
    void updateSnackQuantityForRoom(UUID visitId, UUID snackId, int quantity);
    List<Visit> getVisitsToday();

    List<Visit> getVisitsTodayByName(String name);

    Page<Subscriber> getSubscribers(Pageable pageable);

    Page<Subscriber> getSubscribersByName(String name, Pageable pageable);

    Visit createVisitForNotSubscriber(NotSubscriber notSubscriber);

    List<Visit> getVisitsOfSubscribersToday();

    List<Visit> getVisitsOfNotSubscribersToday();

    void saveSubscriber(UUID subscriptionTypeId, int quantity, String cin, String firstName, String lastName, String email, String phone, String password);

    String newVisit(String result);

    String newParticipant(String result ,String visitId);

    String newVisitSubscriber(String result);

    String newVisitNotSubscriber(String result);

    String newVisitParticipant(String result, String visitId);

    void saveSnacksToVisitor(UUID visitId, SnackForm snackForm);

    void saveSnacksToVisitRoom(UUID visitId, SnackForm snackForm);

    void deleteSnack(UUID visitId, UUID snackId);

    List<VisitOfRoom> getVisitsOfRoomToday();

    List<VisitOfDesk> getVisitsOfDeskToday();

    void deleteSnackForRoom(UUID visitId, UUID snackId);

    void saveSnacksToParticipant(UUID participantId, SnackForm snackForm);

    void deleteSnackForParticipantOfRoom(UUID participantId, UUID snackId);

    void updateSnackQuantityForParticipantOfRoom(UUID participantId, UUID snackId, int quantity);

    void saveSnacksToVisitDesk(UUID visitId, SnackForm snackForm);

    void deleteSnackForDesk(UUID visitId, UUID snackId);

    void updateSnackQuantityForDesk(UUID visitId, UUID snackId, int quantity);

    void updateQuantityStockOfSnackOrBoisson(List<SnacksAndBoissonsOfVisit> snacksAndBoissonsOfVisit);

    String newVisitOfTeam(String result);

    String newVisitOfTeamProfile(String result);

    VisitOfTeam createVisitForTeam(Utilisateur user);

    void saveSnacksToVisitOfTeam(UUID visitId, SnackForm snackForm);

    void deleteSnackForVisitTeam(UUID visitId, UUID snackId);

    void updateSnackQuantityForTeam(UUID visitId, UUID snackId, int quantity);

    double totalePriceOfSnackAndBoissons(List<SnacksAndBoissonsOfVisit> snacksAndBoissonsOfVisit);

    LocalTime maximumTimeAvailabale(LocalDate reservationDate,LocalTime reservationTime);

    LocalTime maximumTimeAvailabaleDesk(LocalDate reservationDate,LocalTime reservationTime);
}
