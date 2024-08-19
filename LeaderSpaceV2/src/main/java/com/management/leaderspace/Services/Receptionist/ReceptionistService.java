package com.management.leaderspace.Services.Receptionist;

import com.management.leaderspace.Entities.*;
import com.management.leaderspace.model.SnackForm;

import java.text.ParseException;
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

    List<Subscriber> getSubscribers();

    List<Subscriber> getSubscribersByName(String name);

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
}
