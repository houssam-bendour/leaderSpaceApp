package com.management.leaderspace.Services.Manager;

import com.management.leaderspace.Entities.*;
import com.management.leaderspace.model.DesignationForm;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ManagerService {
    Manager getProfile();
    public SnacksAndBoissons saveSnackWithImage(String name, double sellingPrice, double purchasePrice, Integer saveSnackWithImage, String type, MultipartFile imageFile , Long quantity) throws IOException ;

    List<SnacksAndBoissons> getAllSnacks();
    ServiceType getServiceById(UUID uuid);

    void saveServicesToDevis(UUID devisId, DesignationForm designationForm);

    void saveServicesToFacture(UUID factureId, DesignationForm designationForm);

    List<Visit> getAllVisitsBetweenStartDayAndEndTime(LocalDate dateDebut,LocalDate dateFin);

    Map<UUID,Double> getTotalPriceOfSnacksAndBoissons(List<Visit> listAllVisitsBetweenStartDayAndEndTime);

    Double calculeTotale(List<Visit> listVisits , Map<UUID, Double> map);

    List<SubscriptionHistory> getAllSubscriptionsByDateDebutBetweenStartDayAndEndDay(LocalDate date_debut, LocalDate date_fin);

    Double calculeTotaleOfSubscriptions(List<SubscriptionHistory> listSubscriptions);

    List<VisitOfRoom> findVisitOfRoomByDate(LocalDate dateDebut, LocalDate dateFin);

    Map<UUID,Double> sommeOfSnacksAndBoissonsOfRoom(List<VisitOfRoom> listAllVisitOfRoom);

    Map<UUID, Map<UUID,Double>> sommeOfSnacksAndBoissonsForParticipantOfVisitRoom(List<VisitOfRoom> allVisitsOfRoom);

    Map<UUID, Double> calculeSommeForVisitOfRoom(List<VisitOfRoom> listAllVisitOfRoom,Map<UUID,Double> sommeOfSnacksAndBoissonsOfRoom,Map<UUID,Map<UUID,Double>> sommeOfSnacksAndBoissonsForParticipantOfVisitRoom);

    Double calculeTotaleOfAllVisitsOfRoom(Map<UUID,Double> sommesForVisitsOfRoom);

    List<VisitOfDesk> findVisitOfDeskByDate(LocalDate dateDebut, LocalDate dateFin);

    Map<UUID,Double> sommeOfsnacksAndBoissonsByVisit(List<VisitOfDesk> allVisitsOfDeskByDay);

    Double totaleOfVisitsOfDesk(List<VisitOfDesk> allVisitOfDeskByDate, Map<UUID,Double> sommeOfsnacksAndBoissonsByVisit);


    void saveSubscriber(Subscriber subscriber, UUID subscriptionType_id);

    void saveUpdateSubscriber(Subscriber subscriber);

    void saveResubscribeOfSubscriber(Subscriber subscriber, UUID subscriptionTypeId);

    Double calculeSommeOfConsommations(Map<UUID,Double> mapConsommations);

    Double calculeSommeConsommationForRoomParticipants(Map<UUID,Map<UUID,Double>> mapRoomParticipants);

    Double calculeSommeOfServicePriceForNormaleVisits(List<Visit> allVisits);
    Double calculeSommeOfServicePriceForRoom(List<VisitOfRoom> allVisitsOfRoom);
    Double calculeSommeOfServicePriceForDesk(List<VisitOfDesk> allVisitsOfDesk);

    List<Visit> getVisitsOfSubscribers();

    List<Visit> getVisitsOfNotSubscribers();

    List<VisitOfRoom> getVisitsOfRoom();

    List<VisitOfDesk> getVisitsOfDesk();

    Map<LocalDate, Double> totaleVisitsNormaleCharts(LocalDate dateDebut, LocalDate dateFin);

    Map<LocalDate,Double> totaleVisitsRoomCharts(LocalDate dateDebut, LocalDate dateFin);

    Map<LocalDate,Double> totaleVisitOfDesk(LocalDate dateDebut, LocalDate dateFin);

    Map<LocalDate,Double> totaleSubscriptions(LocalDate dateDebut, LocalDate dateFin);

    Map<LocalDate,Double> totaleTurnoverForCharts(Map<LocalDate,Double> totaleVisitsNormaleCharts,Map<LocalDate,Double> totaleVisitsRoomCharts,Map<LocalDate,Double> totaleVisitOfDesk, Map<LocalDate,Double> totaleVisitsTeamChartByDates, Map<LocalDate,Double> totaleSubscriptions,Map<LocalDate,Double> totaleContractsCherts);

    Map<LocalDate,Double> totaleContractsCherts(LocalDate dateDebut, LocalDate dateFin);

    Double totaleTurnoverCherts(Map<LocalDate, Double> totaleTurnoverForCharts);

    Map<UUID,Double> sommeOfSnacksAndBoissonsByVisitFomTeam(List<VisitOfTeam> allVisitsOfTeam);

    Double sommeSnacksAndBoissonsForVisitsTeam(Map<UUID,Double> sommeOfSnacksAndBoissonsByVisitFomTeam);

    Map<LocalDate,Double> totaleVisitsTeamChartByDates(LocalDate dateDebut, LocalDate dateFin);
}
