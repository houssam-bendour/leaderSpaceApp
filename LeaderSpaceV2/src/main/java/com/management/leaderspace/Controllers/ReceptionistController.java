package com.management.leaderspace.Controllers;

import com.management.leaderspace.Entities.*;
import com.management.leaderspace.Repositories.*;
import com.management.leaderspace.Services.Manager.ManagerService;
import com.management.leaderspace.Services.Manager.ManagerServiceImp;
import com.management.leaderspace.Services.Receptionist.ReceptionistService;
import com.management.leaderspace.Services.Receptionist.ReceptionistServiceImp;
import com.management.leaderspace.model.SnackForm;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@Controller
@RequestMapping("/reception")
@AllArgsConstructor
@Transactional

public class ReceptionistController {

    private final ReceptionistServiceImp receptionistServiceImp;
    private final ReceptionistRepository receptionistRepository;
    PasswordEncoder passwordEncoder;

    ReceptionistService receptionistService;

    SubscriberRepository subscriberRepository;

    SubscriptionTypeRepository subscriptionTypeRepository;

    VisitRepository visitRepository;

    SnacksAndBoissonsRepository snacksAndBoissonsRepository;

    SnacksAndBoissonsOfVisitRepository snacksAndBoissonsOfVisitRepository;

    NotSubscriberRepository notSubscriberRepository;

    VisitOfRoomRepository visitOfRoomRepository;
    VisitOfDeskRepository visitOfDeskRepository;

    ParticipantOfvisitRoomRepository participantOfvisitRoomRepository;
    ManagerService managerService;
    ManagerServiceImp  managerServiceImp;
    VisitOfTeamRepository visitOfTeamRepository;

    @GetMapping("visit-subscriber")
    public String VisitSubscriber() {

        return "/Receptionist_espace/visit-subscriber";

    }

    @PostMapping("snacks-to-participantRoom")
    public String snacksToParticipantRoom(@RequestParam("visitId") UUID visitId, Model model) {
        model.addAttribute("visitId", visitId);
        return "/Receptionist_espace/snacks-participantRoom";

    }
//    @GetMapping("snacks-to-participantRoom")
//    public String snacksToParticipantRoom1(@RequestParam("visitId") UUID visitId, Model model) {
//        model.addAttribute("visitId", visitId);
//        return "/Receptionist_espace/snacks-participantRoom";
//
//    }

    @GetMapping("visit-room")
    public String VisitRoom(@RequestParam(name = "message", required = false) String message ,Model model) {
        if (message== null){message="";}
        model.addAttribute("message", message);
        model.addAttribute("newVisitRoom", new VisitOfRoom());
        return "/Receptionist_espace/visit-of-room";

    }

    @GetMapping("visit-desk")
    public String VisitDesk(@RequestParam(name = "message", required = false) String message ,Model model) {
        if (message== null){message="";}
        model.addAttribute("message", message);
        model.addAttribute("newVisitDesk", new VisitOfDesk());
        return "/Receptionist_espace/visit-of-desk";

    }

    @PostMapping("save-visit-of-room")
    public String saveVisitOfRoom(@ModelAttribute VisitOfRoom visitOfRoom) {
        List<VisitOfRoom> exisitVisit=visitOfRoomRepository.getVisitByDayAndStartTime(visitOfRoom.getDay(),visitOfRoom.getStartTime(),visitOfRoom.getEndTime());
        if (exisitVisit.isEmpty()){
            VisitOfRoom visitOfRoomSaved = visitOfRoomRepository.save(visitOfRoom);

            return "redirect:/reception/visit-of-room?visitId=" + visitOfRoomSaved.getId();
        }
        return "redirect:/reception/visit-room?message=Il y a une autre reservation en ce moment !";
    }
    @PostMapping("save-visit-of-desk")
    public String saveVisitOfDesk(@ModelAttribute VisitOfDesk visitOfDesk) {
        List<VisitOfDesk> exisitVisit=visitOfDeskRepository.getVisitByDayAndStartTime(visitOfDesk.getDay(),visitOfDesk.getStartTime(),visitOfDesk.getEndTime());
        if (exisitVisit.isEmpty()){
            VisitOfDesk visitOfDeskSaved = visitOfDeskRepository.save(visitOfDesk);

            return "redirect:/reception/visit-of-desk?visitId=" + visitOfDeskSaved.getId();
        }
        return "redirect:/reception/visit-desk?message=Il y a une autre reservation en ce moment !";



    }
    @GetMapping("visit-of-room")
    public String saveVisitOfRoom(@RequestParam("visitId") UUID visitId, Model model) {

        VisitOfRoom visitOfRoom = visitOfRoomRepository.findById(visitId).orElse(null);
        assert visitOfRoom != null;
        List<SnacksAndBoissonsOfVisit> snacks=visitOfRoom.getSnacksAndBoissonsOfVisitRoom();

        double total=visitOfRoom.getService_room_price();

        for (SnacksAndBoissonsOfVisit snack:snacks) {
            if (snack.getSnacksAndBoissons().getImage() != null) {

                String base64Image = Base64.getEncoder().encodeToString(snack.getSnacksAndBoissons().getImage());

                snack.getSnacksAndBoissons().setBase64Image(base64Image);
            }
            total+=snack.getQuantity()*snack.getPurchase_price();
        }
        model.addAttribute("total", total);
        model.addAttribute("visitOfRoom", visitOfRoom);
        return "Receptionist_espace/visit-of-room-profile";

    }

    @GetMapping("visit-of-desk")
    public String saveVisitOfDesk(@RequestParam("visitId") UUID visitId, Model model) {

        VisitOfDesk visitOfDesk = visitOfDeskRepository.findById(visitId).orElse(null);
        assert visitOfDesk != null;
        List<SnacksAndBoissonsOfVisit> snacks=visitOfDesk.getSnacksAndBoissonsOfVisits();
        //snacks.sort((s1, s2) -> Double.compare(s2.getSnacksAndBoissons().getPurchase_price(), s1.getSnacksAndBoissons().getPurchase_price()));
        double total=visitOfDesk.getService_desk_price();
//        Map<UUID, String> msg = new HashMap<>();
        for (SnacksAndBoissonsOfVisit snack:snacks) {
            if (snack.getSnacksAndBoissons().getImage() != null) {

                String base64Image = Base64.getEncoder().encodeToString(snack.getSnacksAndBoissons().getImage());

                snack.getSnacksAndBoissons().setBase64Image(base64Image);
            }
//            if (snack.getSnacksAndBoissons().getType().equals("Boisson") && snack.getSnacksAndBoissons().getPurchase_price() <= 4 && !visitOfDesk.isFreeBoissons()) {
//                snack.setQuantity(snack.getQuantity() - 1);
//                total += snack.getQuantity() * snack.getPurchase_price();
//                visitOfDesk.setFreeBoissons(true);
//
//                msg.put(snack.getId(), "(1 free)");
//            }
            total += snack.getQuantity() * snack.getPurchase_price();
        }
//        model.addAttribute("msg",msg);
        model.addAttribute("total", total);
        model.addAttribute("visitOfDesk", visitOfDesk);
        return "Receptionist_espace/visit-of-desk-profile";

    }

    @PostMapping("visit-of-desk-checkout")
    public String VisitOfDeskCheckout(@RequestParam("visitId") UUID visitId, Model model) {

        VisitOfDesk visitOfDesk = visitOfDeskRepository.findById(visitId).orElse(null);
        assert visitOfDesk != null;
        List<SnacksAndBoissonsOfVisit> snacks=visitOfDesk.getSnacksAndBoissonsOfVisits();
        snacks.sort((s1, s2) -> Double.compare(s2.getSnacksAndBoissons().getPurchase_price(), s1.getSnacksAndBoissons().getPurchase_price()));
        double total=visitOfDesk.getService_desk_price();
        Map<UUID, String> msg = new HashMap<>();
        for (SnacksAndBoissonsOfVisit snack:snacks) {
            if (snack.getSnacksAndBoissons().getImage() != null) {

                String base64Image = Base64.getEncoder().encodeToString(snack.getSnacksAndBoissons().getImage());

                snack.getSnacksAndBoissons().setBase64Image(base64Image);
            }
            if (snack.getSnacksAndBoissons().getType().equals("Boisson") && snack.getSnacksAndBoissons().getPurchase_price() <= 4 && !visitOfDesk.isFreeBoissons() ) {
                snack.setQuantity(snack.getQuantity() - 1);
                total += snack.getQuantity() * snack.getPurchase_price();
                visitOfDesk.setFreeBoissons(true);

                msg.put(snack.getId(), "(1 free)");
            }
            total += snack.getQuantity() * snack.getPurchase_price();
        }
        model.addAttribute("msg",msg);
        model.addAttribute("total", total);
        model.addAttribute("visit", visitOfDesk);
        return "Receptionist_espace/visit-of-desk-checkout";

    }


    @PostMapping("add-snacks-to-visitRoom")
    public String visitRoomSnacks(@RequestParam("visitId") UUID visitId, Model model) {

        System.out.println("Visit ID: " + visitId);

        model.addAttribute("snaks", new SnackForm());

        model.addAttribute("visitRoom_id", visitId);

        List<SnacksAndBoissons> snacksAndBoissonsList = snacksAndBoissonsRepository.findAll();
        // Convertir les images en Base64
        for (SnacksAndBoissons snack : snacksAndBoissonsList) {

            if (snack.getImage() != null) {

                String base64Image = Base64.getEncoder().encodeToString(snack.getImage());

                snack.setBase64Image(base64Image);
            }
        }

        model.addAttribute("snacksAndBoissan", snacksAndBoissonsList);

        return "/Receptionist_espace/add-snacks-to-visitRoom";

    }

    @PostMapping("add-snacks-to-visitDesk")
    public String visitDeskSnacks(@RequestParam("visitId") UUID visitId, Model model) {

        System.out.println("Visit ID: " + visitId);

        model.addAttribute("snaks", new SnackForm());

        model.addAttribute("visitDesk_id", visitId);

        List<SnacksAndBoissons> snacksAndBoissonsList = snacksAndBoissonsRepository.findAll();
        // Convertir les images en Base64
        for (SnacksAndBoissons snack : snacksAndBoissonsList) {

            if (snack.getImage() != null) {

                String base64Image = Base64.getEncoder().encodeToString(snack.getImage());

                snack.setBase64Image(base64Image);
            }
        }

        model.addAttribute("snacksAndBoissan", snacksAndBoissonsList);

        return "/Receptionist_espace/add-snacks-to-visitDesk";

    }

    @PostMapping("save-snacks-to-visitRoom")
    public String saveSnacksToVisitRoom(@RequestParam("visitId") UUID visitId, @ModelAttribute SnackForm snackForm) {

        receptionistService.saveSnacksToVisitRoom(visitId,snackForm);

        return "redirect:/reception/visit-of-room?visitId=" + visitId;
    }

    @PostMapping("save-snacks-to-visitDesk")
    public String saveSnacksToVisitDesk(@RequestParam("visitId") UUID visitId, @ModelAttribute SnackForm snackForm) {

        receptionistService.saveSnacksToVisitDesk(visitId,snackForm);

        return "redirect:/reception/visit-of-desk?visitId=" + visitId;
    }

    @GetMapping("visit-today")
    public String VisitsToday(Model model) {

        List<Visit> visitOfSubscribers = receptionistService.getVisitsOfSubscribersToday();

        model.addAttribute("visitOfSubscribers", visitOfSubscribers);

        List<Visit> visitOfNotSubscribers = receptionistService.getVisitsOfNotSubscribersToday();

        model.addAttribute("visitOfNotSubscribers", visitOfNotSubscribers);

        List<VisitOfRoom> visitOfRooms = receptionistService.getVisitsOfRoomToday();

        model.addAttribute("visitOfRooms", visitOfRooms);

        List<VisitOfDesk> visitOfDesks = receptionistService.getVisitsOfDeskToday();

        model.addAttribute("visitOfDesks", visitOfDesks);

        return "/Receptionist_espace/visit-today";

    }

    @GetMapping("get-subscribers")
    public String getSubscribers(Model model) {

        List<Subscriber> subscribers = receptionistService.getSubscribers();

        model.addAttribute("subscribers", subscribers);

        return "/Receptionist_espace/all-subscribers";

    }

    @GetMapping("visit-Search")
    public String VisitsSearchToday(@RequestParam("Name") String Name, Model model) {

        List<Visit> visits = receptionistService.getVisitsTodayByName(Name);

        model.addAttribute("visits", visits);

        List<Visit> visitOfNotSubscribers = receptionistService.getVisitsOfNotSubscribersToday();

        model.addAttribute("visitOfNotSubscribers", visitOfNotSubscribers);

        List<VisitOfRoom> visitOfRooms = receptionistService.getVisitsOfRoomToday();

        model.addAttribute("visitOfRooms", visitOfRooms);

        List<VisitOfDesk> visitOfDesks = receptionistService.getVisitsOfDeskToday();

        model.addAttribute("visitOfDesks", visitOfDesks);

        return "/Receptionist_espace/visit-today";
    }

    @GetMapping("subscriber-Search")
    public String subscriberSearch(@RequestParam("Name") String Name, Model model) {

        List<Subscriber> subscribers = receptionistService.getSubscribersByName(Name);

        model.addAttribute("subscribers", subscribers);

        return "/Receptionist_espace/all-subscribers";

    }

    @GetMapping("add-subscriber")
    public String addSubscriber(Model model) {

        List<SubscriptionType> subscriptionTypes = subscriptionTypeRepository.findAll();

        model.addAttribute("subscriptionTypes", subscriptionTypes);

        return "/Receptionist_espace/add-subscriber";

    }

    @PostMapping("/save-subscriber")
    public String saveSubscriber(@RequestParam("subscriptionType_id") UUID subscriptionTypeId,
                                 @RequestParam("cin") String cin,
                                 @RequestParam("first_name") String firstName,
                                 @RequestParam("last_name") String lastName,
                                 @RequestParam("email") String email,
                                 @RequestParam("phone") String phone,
                                 @RequestParam("quantity") int quantity,
                                 @RequestParam("password") String password) {

        receptionistService.saveSubscriber(subscriptionTypeId,quantity,cin,firstName,lastName,email, phone,password);

        return "redirect:/reception/get-subscribers";
    }

    @GetMapping("new-visit")
    public String newVisit(@RequestParam("result") String result) {
        return receptionistService.newVisit(result);
    }

    @GetMapping("new-visit-subscriber")
    public String newVisitSubscriber(@RequestParam("userId") String result){
        return receptionistService.newVisitSubscriber(result);
    }

    @GetMapping("new-visit-not-subscriber")
    public String newVisitNotSubscriber(@RequestParam("userId") String result){
        return receptionistService.newVisitNotSubscriber(result);
    }

    @GetMapping("visit-subscriber-profile")
    public String VisitSubscriberProfile(@RequestParam("visitId") UUID visitId, Model model) {
        Visit visit = visitRepository.findById(visitId).orElse(null);
        model.addAttribute("visit", visit);


        assert visit != null;

        List<SnacksAndBoissonsOfVisit> snacksAndBoissonsList = visit.getSnacksAndBoissonsOfVisits();
        // Convertir les images en Base64

        for (SnacksAndBoissonsOfVisit snack : snacksAndBoissonsList) {

            if (snack.getSnacksAndBoissons().getImage() != null) {

                String base64Image = Base64.getEncoder().encodeToString(snack.getSnacksAndBoissons().getImage());

                snack.getSnacksAndBoissons().setBase64Image(base64Image);
            }
        }
        model.addAttribute("snacks", snacksAndBoissonsList);
        return "/Receptionist_espace/visit-subscriber-profile";

    }

    @GetMapping("subscribtion-fished")
    public String SubscribtionFinished() {

        return "/Receptionist_espace/subscribtion-fished";

    }

    @GetMapping("Expires_today")
    public String Expires_today() {

        return "/Receptionist_espace/Expires_today";

    }

    @PostMapping("add-snacks-to-visitor")
    public String visitorSnacks(@RequestParam("visitId") UUID visitId, Model model) {

        System.out.println("Visit ID: " + visitId);

        model.addAttribute("snaks", new SnackForm());

        model.addAttribute("visit_id", visitId);

        List<SnacksAndBoissons> snacksAndBoissonsList = snacksAndBoissonsRepository.findAll();
        // Convertir les images en Base64
        for (SnacksAndBoissons snack : snacksAndBoissonsList) {

            if (snack.getImage() != null) {

                String base64Image = Base64.getEncoder().encodeToString(snack.getImage());

                snack.setBase64Image(base64Image);
            }
        }

        model.addAttribute("snacksAndBoissan", snacksAndBoissonsList);

        return "/Receptionist_espace/add-snacks-to-visitor";

    }
    @PostMapping("add-snacks-to-participant-of-visit-room")
    public String participantSnacks(@RequestParam("visitId") UUID visitId,@RequestParam("participantId") UUID participantId, Model model) {

        model.addAttribute("snaks", new SnackForm());

        model.addAttribute("visit_id", visitId);

        model.addAttribute("participantId",participantId);

        List<SnacksAndBoissons> snacksAndBoissonsList = snacksAndBoissonsRepository.findAll();
        // Convertir les images en Base64
        for (SnacksAndBoissons snack : snacksAndBoissonsList) {

            if (snack.getImage() != null) {

                String base64Image = Base64.getEncoder().encodeToString(snack.getImage());

                snack.setBase64Image(base64Image);
            }
        }

        model.addAttribute("snacksAndBoissan", snacksAndBoissonsList);

        return "/Receptionist_espace/add-snacks-to-participant-of-visit-room";

    }
    @PostMapping("save-snacks-to-visitor")
    public String saveSnacksToVisitor(@RequestParam("visitId") UUID visitId, @ModelAttribute SnackForm snackForm) {

        receptionistService.saveSnacksToVisitor(visitId,snackForm);

        return "redirect:/reception/visit-subscriber-profile?visitId=" + visitId;
    }

    @PostMapping("save-snacks-to-participant-of-visit-room")
    public String saveSnacksToParticipant(@RequestParam("visitId") UUID visitId,@RequestParam("participantId") UUID participantId, @ModelAttribute SnackForm snackForm) {

        receptionistService.saveSnacksToParticipant(participantId,snackForm);

        return "redirect:/reception/visit-participant-profile?visitId=" + visitId+"&participantId="+participantId;
    }


    @PostMapping("delete-snack")
    public String deleteSnack(@RequestParam("visitId") UUID visitId, @RequestParam("snackId") UUID snackId) {

        receptionistService.deleteSnack(visitId , snackId);

        return "redirect:/reception/visit-subscriber-profile?visitId=" + visitId;
    }

    @PostMapping("delete-snack-for-room")
    public String deleteSnackForRoom(@RequestParam("visitId") UUID visitId, @RequestParam("snackId") UUID snackId) {

        receptionistService.deleteSnackForRoom(visitId , snackId);

        return "redirect:/reception/visit-of-room?visitId=" + visitId;
    }
    @PostMapping("delete-snack-for-desk")
    public String deleteSnackForDesk(@RequestParam("visitId") UUID visitId, @RequestParam("snackId") UUID snackId) {

        receptionistService.deleteSnackForDesk(visitId , snackId);

        return "redirect:/reception/visit-of-desk?visitId=" + visitId;
    }

    @PostMapping("delete-snack-for-participant-of-room")
    public String deleteSnackForParticipantOfRoom(@RequestParam("visitId") UUID visitId,@RequestParam("participantId") UUID participantId, @RequestParam("snackId") UUID snackId) {

        receptionistService.deleteSnackForParticipantOfRoom(participantId , snackId);

        return "redirect:/reception/visit-participant-profile?visitId=" + visitId+"&participantId="+participantId;
    }

    @PostMapping("/update-snack-form")
    public String showUpdateSnackForm(@RequestParam("visitId") UUID visitId, @RequestParam("snackId") UUID snackId, Model model) {

        try {
            SnacksAndBoissons snack= snacksAndBoissonsRepository.findById(snackId).get();

            model.addAttribute("snack", snack);

            SnacksAndBoissonsOfVisit snackVisit = snacksAndBoissonsOfVisitRepository.getByVisitIdAndSnackId(visitId, snackId);

            model.addAttribute("visitId", visitId);

            model.addAttribute("snackAndBoisson", snackVisit.getSnacksAndBoissons());

            model.addAttribute("quantity", snackVisit.getQuantity());

            return "/Receptionist_espace/update-snack-form";
        } catch (Exception e) {

            return e.getMessage();// Le nom du template Thymeleaf pour le formulaire de mise à jour
        }
    }

    @PostMapping("/update-snack-form-for-room")
    public String showUpdateSnackFormRoom(@RequestParam("visitId") UUID visitId, @RequestParam("snackId") UUID snackId, Model model) {

        try {
            SnacksAndBoissons snack= snacksAndBoissonsRepository.findById(snackId).get();

            model.addAttribute("snack", snack);

            SnacksAndBoissonsOfVisit snackVisit = snacksAndBoissonsOfVisitRepository.getByVisitOfRoomIdAndSnackId(visitId, snackId);

            model.addAttribute("visitId", visitId);

            model.addAttribute("snackAndBoisson", snackVisit.getSnacksAndBoissons());

            model.addAttribute("quantity", snackVisit.getQuantity());

            return "/Receptionist_espace/update-snack-form-for-room";
        } catch (Exception e) {

            return e.getMessage();
        }
    }

    @PostMapping("/update-snack-form-for-desk")
    public String showUpdateSnackFormDesk(@RequestParam("visitId") UUID visitId, @RequestParam("snackId") UUID snackId, Model model) {

        try {
            SnacksAndBoissons snack= snacksAndBoissonsRepository.findById(snackId).get();

            model.addAttribute("snack", snack);

            SnacksAndBoissonsOfVisit snackVisit = snacksAndBoissonsOfVisitRepository.getByVisitOfDeskIdAndSnackId(visitId, snackId);

            model.addAttribute("visitId", visitId);

            model.addAttribute("snackAndBoisson", snackVisit.getSnacksAndBoissons());

            model.addAttribute("quantity", snackVisit.getQuantity());

            return "/Receptionist_espace/update-snack-form-for-desk";
        } catch (Exception e) {

            return e.getMessage();
        }
    }

    @PostMapping("/update-snack-for-participant-of-room-form")
    public String showUpdateSnackForParticipantOfRoomForm(@RequestParam("visitId") UUID visitId,@RequestParam("participantId") UUID participantId, @RequestParam("snackId") UUID snackId, Model model) {

        try {
            SnacksAndBoissons snack= snacksAndBoissonsRepository.findById(snackId).get();

            model.addAttribute("snack", snack);

            SnacksAndBoissonsOfVisit snackVisit = snacksAndBoissonsOfVisitRepository.getByParticipantOfRoomIdAndSnackId(participantId, snackId);

            model.addAttribute("visitId", visitId);

            model.addAttribute("participantId", participantId);

            model.addAttribute("snackAndBoisson", snackVisit.getSnacksAndBoissons());

            model.addAttribute("quantity", snackVisit.getQuantity());

            return "/Receptionist_espace/update-snack-form-for-participant-of-room";
        } catch (Exception e) {

            return e.getMessage();
        }
    }


    @PostMapping("/update-snack")
    public String updateSnack(@RequestParam UUID visitId, @RequestParam UUID snackId, @RequestParam int quantity) {

        receptionistService.updateSnackQuantity(visitId, snackId, quantity);

        return "redirect:/reception/visit-subscriber-profile?visitId=" + visitId;
    }

    @PostMapping("/update-snack-for-room")
    public String updateSnackForRoom(@RequestParam UUID visitId, @RequestParam UUID snackId, @RequestParam int quantity) {

        receptionistService.updateSnackQuantityForRoom(visitId, snackId, quantity);

        return "redirect:/reception/visit-of-room?visitId=" + visitId;
    }

    @PostMapping("/update-snack-for-desk")
    public String updateSnackForDesk(@RequestParam UUID visitId, @RequestParam UUID snackId, @RequestParam int quantity) {

        receptionistService.updateSnackQuantityForDesk(visitId, snackId, quantity);

        return "redirect:/reception/visit-of-desk?visitId=" + visitId;
    }

    @PostMapping("/update-snack-for-participant-of-room")
    public String updateSnackForParticipantOfRoom(@RequestParam UUID visitId, @RequestParam UUID snackId,@RequestParam("participantId") UUID participantId, @RequestParam int quantity) {

        receptionistService.updateSnackQuantityForParticipantOfRoom(participantId, snackId, quantity);

        return "redirect:/reception/visit-participant-profile?visitId=" + visitId+"&participantId="+participantId;
    }

    @PostMapping("checkout")
    public String checkout(@RequestParam("visitId") UUID visitId){

        Visit visit = visitRepository.findById(visitId).orElseThrow(() -> new IllegalArgumentException("Invalid visit ID"));

        if(visit.getSubscriber()!=null)
            return "redirect:/reception/subscriber-checkout?visitId=" + visitId;
        else
            return "redirect:/reception/not-subscriber-checkout?visitId=" + visitId;
    }
    @GetMapping("subscriber-checkout")
    public String checkoutSubscriber(@RequestParam("visitId") UUID visitId, Model model) {

        Visit visit = visitRepository.findById(visitId).orElseThrow(() -> new IllegalArgumentException("Invalid visit ID"));

        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

        LocalTime localTime = moroccoDateTime.toLocalTime();

        visit.setEndTime(localTime);

        Duration duration = Duration.between(visit.getStartTime(), localTime);

        long hours = duration.toHours();

        long minutes = duration.toMinutesPart();

        List<SnacksAndBoissonsOfVisit> sortedSnacks = visit.getSnacksAndBoissonsOfVisits();

        receptionistServiceImp.updateQuantityStockOfSnackOrBoisson(sortedSnacks);

        sortedSnacks.sort((s1, s2) -> Double.compare(s2.getSnacksAndBoissons().getPurchase_price(), s1.getSnacksAndBoissons().getPurchase_price()));

        double total = 0;

        int numberOfFreeBoissons = 0;

        Map<UUID, String> msg = new HashMap<>();
        for (SnacksAndBoissonsOfVisit s : sortedSnacks) {

            if (s.getSnacksAndBoissons().getType().equals("Boisson") && s.getSnacksAndBoissons().getPurchase_price() <= 4 && s.getVisit().getSubscriber().getSubscription_type().getNumber_of_Free_Drinks_of_day() > numberOfFreeBoissons) {

                if (s.getVisit().getSubscriber().getSubscription_type().getHour_of_day() > hours) {

                    if (s.getQuantity() >= 2 && s.getVisit().getSubscriber().getSubscription_type().getNumber_of_Free_Drinks_of_day() == 2) {

                        s.setQuantity(s.getQuantity() - 2);

                        total += s.getQuantity() * s.getSnacksAndBoissons().getPurchase_price();

                        numberOfFreeBoissons += 2;

                        msg.put(s.getId(), "(2 free)");

                    } else {

                        s.setQuantity(s.getQuantity() - 1);

                        total += s.getQuantity() * s.getSnacksAndBoissons().getPurchase_price();

                        numberOfFreeBoissons++;

                        msg.put(s.getId(), "(1 free)");

                    }
                } else if (s.getVisit().getSubscriber().getSubscription_type().getHour_of_day() == hours && minutes <= 10) {

                    if (s.getQuantity() >= 2 && s.getVisit().getSubscriber().getSubscription_type().getNumber_of_Free_Drinks_of_day() == 2) {

                        s.setQuantity(s.getQuantity() - 2);

                        total += s.getQuantity() * s.getSnacksAndBoissons().getPurchase_price();

                        numberOfFreeBoissons += 2;

                        msg.put(s.getId(), "(2 free)");

                    } else {

                        s.setQuantity(s.getQuantity() - 1);

                        total += s.getQuantity() * s.getSnacksAndBoissons().getPurchase_price();

                        numberOfFreeBoissons++;

                        msg.put(s.getId(), "(1 free)");
                    }
                } else {
                    if (s.getQuantity() >= 2) {

                        s.setQuantity(s.getQuantity() - 2);

                        total += s.getQuantity() * s.getSnacksAndBoissons().getPurchase_price();

                        numberOfFreeBoissons += 2;

                        msg.put(s.getId(), "(2 free)");

                    } else {

                        s.setQuantity(s.getQuantity() - 1);

                        total += s.getQuantity() * s.getSnacksAndBoissons().getPurchase_price();

                        numberOfFreeBoissons++;

                        msg.put(s.getId(), "(1 free)");
                    }
                }
            } else {

                total += s.getQuantity() * s.getSnacksAndBoissons().getPurchase_price();
            }

        }
        if (visit.getSubscriber().getSubscription_type().getHour_of_day() < hours || visit.getSubscriber().getSubscription_type().getHour_of_day() == hours && minutes > 10) {

            Visit secondVisit = new Visit();

            secondVisit.setSubscriber(visit.getSubscriber());

            secondVisit.setEndTime(visit.getEndTime());

            secondVisit.setStartTime(visit.getStartTime());

            secondVisit.setDay(visit.getDay());

            secondVisit.setSnacksAndBoissonsOfVisits(visit.getSnacksAndBoissonsOfVisits());

            visitRepository.save(secondVisit);
        }

        total+=visit.getService_suplementaire_price();

        model.addAttribute("visit", visitRepository.save(visit));

        model.addAttribute("total", total);

        model.addAttribute("msg", msg);

        return "/Receptionist_espace/visitor-checkout";
    }

    @GetMapping("not-subscriber-checkout")
    public String checkoutNotSubscriber(@RequestParam("visitId") UUID visitId, Model model) {

        Visit visit = visitRepository.findById(visitId).orElseThrow(() -> new IllegalArgumentException("Invalid visit Id"));

        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");

        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

        LocalTime localTime = moroccoDateTime.toLocalTime();

        visit.setEndTime(localTime);

        Duration duration = Duration.between(visit.getStartTime(), localTime);

        long hours = duration.toHours();

        long minutes = duration.toMinutesPart();

        List<SnacksAndBoissonsOfVisit> sortedSnacks = visit.getSnacksAndBoissonsOfVisits();

        receptionistServiceImp.updateQuantityStockOfSnackOrBoisson(sortedSnacks);

        sortedSnacks.sort((s1, s2) -> Double.compare(s2.getSnacksAndBoissons().getPurchase_price(), s1.getSnacksAndBoissons().getPurchase_price()));

        double total = 0;

        double priceOfVisit;

        int numberOfFreeBoissons;

        if(hours <6) {

            numberOfFreeBoissons = 1;

            priceOfVisit= 15;

        }else if (hours == 6 && minutes <= 10) {

            numberOfFreeBoissons = 1;

            priceOfVisit= 15;

        }else{

            numberOfFreeBoissons = 2;

            priceOfVisit= 24;

        }
        Map<UUID, String> msg = new HashMap<>();

        for (SnacksAndBoissonsOfVisit s : sortedSnacks) {

            if (s.getSnacksAndBoissons().getType().equals("Boisson") && numberOfFreeBoissons>0) {

                if (hours <6) {

                        s.setQuantity(s.getQuantity() - 1);

                        total += s.getQuantity() * s.getSnacksAndBoissons().getPurchase_price();

                        numberOfFreeBoissons -= 1;

                        msg.put(s.getId(), "(1 free)");

                } else if (6== hours && minutes <= 10) {

                    s.setQuantity(s.getQuantity() - 1);

                    total += s.getQuantity() * s.getSnacksAndBoissons().getPurchase_price();

                    numberOfFreeBoissons -= 1;

                    msg.put(s.getId(), "(1 free)");

                } else {
                    if (s.getQuantity() >= 2) {

                        s.setQuantity(s.getQuantity() - 2);

                        total += s.getQuantity() * s.getSnacksAndBoissons().getPurchase_price();

                        numberOfFreeBoissons += 2;

                        msg.put(s.getId(), "(2 free)");

                    } else {

                        s.setQuantity(s.getQuantity() - 1);

                        total += s.getQuantity() * s.getSnacksAndBoissons().getPurchase_price();

                        numberOfFreeBoissons++;

                        msg.put(s.getId(), "(1 free)");
                    }
                }
            } else {

                total += s.getQuantity() * s.getSnacksAndBoissons().getPurchase_price();
            }

        }
        visit.setService_price(priceOfVisit);


        model.addAttribute("visit", visitRepository.save(visit));

        model.addAttribute("total", total+priceOfVisit);

        model.addAttribute("msg", msg);

        return "/Receptionist_espace/not-subscriber-checkout";
    }

    @GetMapping("new-participant")
    public String newParticipant(@RequestParam("result") String result,@RequestParam("visitId") String visitId) {
        return receptionistService.newParticipant(result,visitId);
    }
    @GetMapping("new-visit-participant")
    public String newVisitParticipant(@RequestParam("userId") String result, @RequestParam("visitId") String visitId){
        return receptionistService.newVisitParticipant(result,visitId);
    }

    @GetMapping("visit-participant-profile")
    public String VisitParticipantProfile(@RequestParam("visitId") String visitId,@RequestParam("participantId") String participantId, Model model) {
        VisitOfRoom visitOfRoom = visitOfRoomRepository.findById(UUID.fromString(visitId)).orElse(null);

        ParticipantOfvisitRoom participantOfvisitRoom = participantOfvisitRoomRepository.findById(UUID.fromString(participantId)).orElse(null);


        model.addAttribute("visit", visitOfRoom);

        assert participantOfvisitRoom != null;

        List<SnacksAndBoissonsOfVisit> snacksAndBoissonsList = participantOfvisitRoom.getSnacksAndBoissonsOfVisits();
        // Convertir les images en Base64
        double total=0;
        for (SnacksAndBoissonsOfVisit snack : snacksAndBoissonsList) {

            if (snack.getSnacksAndBoissons().getImage() != null) {

                String base64Image = Base64.getEncoder().encodeToString(snack.getSnacksAndBoissons().getImage());

                snack.getSnacksAndBoissons().setBase64Image(base64Image);
            }
            total+=snack.getSnacksAndBoissons().getPurchase_price()*snack.getQuantity();
        }
        model.addAttribute("snacks", snacksAndBoissonsList);
        model.addAttribute("participantId",participantId);
        model.addAttribute("total",total);
        model.addAttribute("participantOfvisitRoom",participantOfvisitRoom);
        return "/Receptionist_espace/visit-participant-profile";

    }
    @PostMapping("update-checkout-time-of-visit-room")
    String updateCheckoutTimeOfVisitRoom(@RequestParam("visitRoomId") UUID visitId,Model model){
        VisitOfRoom visitOfRoom = visitOfRoomRepository.findById(visitId).orElse(null);
        model.addAttribute("visit", visitOfRoom);
        return "/Receptionist_espace/update-checkout-time-of-visit-room";
    }
    @PostMapping("save-update-checkout-time-of-visit-room")
    String saveUpdateCheckoutTimeOfVisitRoom(@RequestParam("visitRoomId") UUID visitId,@RequestParam("newTime") LocalTime localTime,Model model){
        VisitOfRoom visitOfRoom = visitOfRoomRepository.findById(visitId).orElse(null);
        assert visitOfRoom != null;
        visitOfRoom.setEndTime(localTime);
        visitOfRoomRepository.save(visitOfRoom);
        return "redirect:/reception/visit-of-room?visitId=" + visitOfRoom.getId();
    }

    @PostMapping("update-checkout-time-of-visit-desk")
    String updateCheckoutTimeOfVisitDesk(@RequestParam("visitDeskId") UUID visitId,Model model){
        VisitOfDesk visitOfDesk = visitOfDeskRepository.findById(visitId).orElse(null);
        model.addAttribute("visit", visitOfDesk);
        return "/Receptionist_espace/update-checkout-time-of-visit-desk";
    }
    @PostMapping("save-update-checkout-time-of-visit-desk")
    String saveUpdateCheckoutTimeOfVisitDesk(@RequestParam("visitDeskId") UUID visitId,@RequestParam("newTime") LocalTime localTime,Model model){
        VisitOfDesk visitOfDesk = visitOfDeskRepository.findById(visitId).orElse(null);
        assert visitOfDesk != null;
        visitOfDesk.setEndTime(localTime);
        visitOfDeskRepository.save(visitOfDesk);
        return "redirect:/reception/visit-of-desk?visitId=" + visitOfDesk.getId();
    }

    @PostMapping("update-price-of-visit-room")
    String updatePriceOfVisitRoom(@RequestParam("visitRoomId") UUID visitId,Model model){
        VisitOfRoom visitOfRoom = visitOfRoomRepository.findById(visitId).orElse(null);
        model.addAttribute("visit", visitOfRoom);
        return "/Receptionist_espace/update-price-of-visit-roomm";
    }

    @PostMapping("update-price-of-visit-desk")
    String updatePriceOfVisitDesk(@RequestParam("visitDeskId") UUID visitId,Model model){
        VisitOfDesk visitOfDesk = visitOfDeskRepository.findById(visitId).orElse(null);
        model.addAttribute("visit", visitOfDesk);
        return "/Receptionist_espace/update-price-of-visit-desk";
    }

    @PostMapping("save-update-price-of-visit-room")
    String saveUpdatePriceOfVisitRoom(@RequestParam("visitRoomId") UUID visitId,@RequestParam("newPrice") double newPrice,Model model) {
        VisitOfRoom visitOfRoom = visitOfRoomRepository.findById(visitId).orElse(null);
        assert visitOfRoom != null;
        visitOfRoom.setService_room_price(newPrice);
        visitOfRoomRepository.save(visitOfRoom);
        return "redirect:/reception/visit-of-room?visitId=" + visitOfRoom.getId();
    }
    @PostMapping("save-update-price-of-visit-desk")
    String saveUpdatePriceOfVisitDesk(@RequestParam("visitDeskId") UUID visitId,@RequestParam("newPrice") double newPrice,Model model) {
        VisitOfDesk visitOfDesk = visitOfDeskRepository.findById(visitId).orElse(null);
        assert visitOfDesk != null;
        visitOfDesk.setService_desk_price(newPrice);
        visitOfDeskRepository.save(visitOfDesk);
        return "redirect:/reception/visit-of-desk?visitId=" + visitOfDesk.getId();
    }
//======================Additional-service-of-visit-desk=====================
    @PostMapping("add-Additional-service-of-visit-desk")
    String addAdditionalServiceOfVisitDesk(@RequestParam("visitDeskId") UUID visitId,Model model) {
        model.addAttribute("visitDeskId",visitId);
        return "Receptionist_espace/Additional-service-of-visit-desk-form";
    }

    @PostMapping("save-Additional-service-of-visit-desk")
    String saveAdditionalServiceOfVisitDesk(@RequestParam("visitDeskId") UUID visitId,@RequestParam("price") double price,@RequestParam("service") String service) {
        VisitOfDesk visitOfDesk = visitOfDeskRepository.findById(visitId).orElse(null);
        assert visitOfDesk != null;
        visitOfDesk.setService_suplementaire(service);
        visitOfDesk.setService_suplementaire_price(price);
        visitOfDeskRepository.save(visitOfDesk);

        return "redirect:/reception/visit-of-desk?visitId=" + visitOfDesk.getId();
    }
    @PostMapping("update-Additional-service-of-visit-desk")
    String updateAdditionalServiceOfVisitDesk(@RequestParam("visitDeskId") UUID visitId,Model model) {
        VisitOfDesk visitOfDesk = visitOfDeskRepository.findById(visitId).orElse(null);
        model.addAttribute("visit", visitOfDesk);
        model.addAttribute("visitDeskId",visitId);
        return "Receptionist_espace/update-Additional-service-of-visit-desk-form";
    }

    @PostMapping("save-update-Additional-service-of-visit-desk")
    String saveUpdateAdditionalServiceOfVisitDesk(@RequestParam("visitDeskId") UUID visitId,@RequestParam("price") double price,@RequestParam("service") String service) {
        VisitOfDesk visitOfDesk = visitOfDeskRepository.findById(visitId).orElse(null);
        assert visitOfDesk != null;
        visitOfDesk.setService_suplementaire(service);
        visitOfDesk.setService_suplementaire_price(price);
        visitOfDeskRepository.save(visitOfDesk);

        return "redirect:/reception/visit-of-desk?visitId=" + visitOfDesk.getId();
    }

    @PostMapping("delete-Additional-service-of-visit-desk")
    String deleteAdditionalServiceOfVisitDesk(@RequestParam("visitDeskId") UUID visitId) {
        VisitOfDesk visitOfDesk = visitOfDeskRepository.findById(visitId).orElse(null);
        assert visitOfDesk != null;
        visitOfDesk.setService_suplementaire(null);
        visitOfDesk.setService_suplementaire_price(0);
        visitOfDeskRepository.save(visitOfDesk);

        return "redirect:/reception/visit-of-desk?visitId=" + visitOfDesk.getId();
    }

    //======================Additional-service-of-visit-room=====================
    @PostMapping("add-Additional-service-of-visit-room")
    String addAdditionalServiceOfVisitRoom(@RequestParam("visitRoomId") UUID visitId,Model model) {
        model.addAttribute("visitRoomId",visitId);
        return "Receptionist_espace/Additional-service-of-visit-room-form";
    }

    @PostMapping("save-Additional-service-of-visit-room")
    String saveAdditionalServiceOfVisitRoom(@RequestParam("visitRoomId") UUID visitId,@RequestParam("price") double price,@RequestParam("service") String service) {
        VisitOfRoom visitOfRoom = visitOfRoomRepository.findById(visitId).orElse(null);
        assert visitOfRoom != null;
        visitOfRoom.setService_suplementaire(service);
        visitOfRoom.setService_suplementaire_price(price);
        visitOfRoomRepository.save(visitOfRoom);

        return "redirect:/reception/visit-of-room?visitId=" + visitOfRoom.getId();
    }
    @PostMapping("update-Additional-service-of-visit-room")
    String updateAdditionalServiceOfVisitRoom(@RequestParam("visitRoomId") UUID visitId,Model model) {
        VisitOfRoom visitOfRoom = visitOfRoomRepository.findById(visitId).orElse(null);
        model.addAttribute("visit", visitOfRoom);
        model.addAttribute("visitRoomId",visitId);
        return "Receptionist_espace/update-Additional-service-of-visit-room-form";
    }

    @PostMapping("save-update-Additional-service-of-visit-room")
    String saveUpdateAdditionalServiceOfVisitRoom(@RequestParam("visitRoomId") UUID visitId,@RequestParam("price") double price,@RequestParam("service") String service) {
        VisitOfRoom visitOfRoom = visitOfRoomRepository.findById(visitId).orElse(null);
        assert visitOfRoom != null;
        visitOfRoom.setService_suplementaire(service);
        visitOfRoom.setService_suplementaire_price(price);
        visitOfRoomRepository.save(visitOfRoom);

        return "redirect:/reception/visit-of-room?visitId=" + visitOfRoom.getId();
    }

    @PostMapping("delete-Additional-service-of-visit-room")
    String deleteAdditionalServiceOfVisitRoom(@RequestParam("visitRoomId") UUID visitId) {
        VisitOfRoom visitOfRoom = visitOfRoomRepository.findById(visitId).orElse(null);
        assert visitOfRoom != null;
        visitOfRoom.setService_suplementaire(null);
        visitOfRoom.setService_suplementaire_price(0);
        visitOfRoomRepository.save(visitOfRoom);

        return "redirect:/reception/visit-of-room?visitId=" + visitOfRoom.getId();
    }

    //======================Additional-service-of-visit-normal=====================
    @PostMapping("add-Additional-service-of-visit")
    String addAdditionalServiceOfVisit(@RequestParam("visitId") UUID visitId,Model model) {
        model.addAttribute("visitId",visitId);
        return "Receptionist_espace/Additional-service-of-visit-form";
    }

    @PostMapping("save-Additional-service-of-visit")
    String saveAdditionalServiceOfVisit(@RequestParam("visitId") UUID visitId,@RequestParam("price") double price,@RequestParam("service") String service) {
        Visit visit = visitRepository.findById(visitId).orElse(null);
        assert visit != null;
        visit.setService_suplementaire(service);
        visit.setService_suplementaire_price(price);
        visitRepository.save(visit);
        return "redirect:/reception/visit-subscriber-profile?visitId=" + visit.getId();
    }
    @PostMapping("update-Additional-service-of-visit")
    String updateAdditionalServiceOfVisit(@RequestParam("visitId") UUID visitId,Model model) {
        Visit visit = visitRepository.findById(visitId).orElse(null);
        model.addAttribute("visit", visit);
        model.addAttribute("visitId",visitId);
        return "Receptionist_espace/update-Additional-service-of-visit-form";
    }

    @PostMapping("save-update-Additional-service-of-visit")
    String saveUpdateAdditionalServiceOfVisit(@RequestParam("visitId") UUID visitId,@RequestParam("price") double price,@RequestParam("service") String service) {
        Visit visit = visitRepository.findById(visitId).orElse(null);
        assert visit != null;
        visit.setService_suplementaire(service);
        visit.setService_suplementaire_price(price);
        visitRepository.save(visit);
        return "redirect:/reception/visit-subscriber-profile?visitId=" + visit.getId();
    }

    @PostMapping("delete-Additional-service-of-visit")
    String deleteAdditionalServiceOfVisit(@RequestParam("visitId") UUID visitId) {
        Visit visit = visitRepository.findById(visitId).orElse(null);
        assert visit != null;
        visit.setService_suplementaire(null);
        visit.setService_suplementaire_price(0);
        visitRepository.save(visit);
        return "redirect:/reception/visit-subscriber-profile?visitId=" + visit.getId();
    }

    //======================Additional-service-for-participant-of-visit-room=====================
    @PostMapping("add-Additional-service-for-participant-of-visit-room")
    String addAdditionalServiceForParticipantOfVisitRoom(@RequestParam("visitId") UUID visitId,@RequestParam("participantVisitId") UUID participantVisitId,Model model) {
        model.addAttribute("participantVisitId",participantVisitId);
        model.addAttribute("visitId",visitId);
        return "Receptionist_espace/Additional-service-for-participant-of-visit-room-form";
    }

    @PostMapping("save-Additional-service-for-participant-of-visit-room")
    String saveAdditionalServiceForParticipantOfVisitRoom(@RequestParam("visitId") UUID visitId,@RequestParam("participantVisitId") UUID participantVisitId,@RequestParam("price") double price,@RequestParam("service") String service) {
        ParticipantOfvisitRoom participantOfvisitRoom = participantOfvisitRoomRepository.findById(participantVisitId).orElse(null);
        assert participantOfvisitRoom != null;
        participantOfvisitRoom.setService_suplementaire(service);
        participantOfvisitRoom.setService_suplementaire_price(price);
        participantOfvisitRoomRepository.save(participantOfvisitRoom);

        return "redirect:/reception/visit-participant-profile?visitId=" + visitId+"&participantId="+participantOfvisitRoom.getId();
    }
    @PostMapping("update-Additional-service-for-participant-of-visit-room")
    String updateAdditionalServiceForParticipantOfVisitRoom(@RequestParam("visitId") UUID visitId,@RequestParam("participantVisitId") UUID participantVisitId,Model model) {
        ParticipantOfvisitRoom participantOfvisitRoom = participantOfvisitRoomRepository.findById(participantVisitId).orElse(null);
        model.addAttribute("participantOfvisitRoom", participantOfvisitRoom);
        model.addAttribute("visitRoomId",visitId);
        return "Receptionist_espace/update-Additional-service-for-participant-of-visit-room";
    }

    @PostMapping("save-update-Additional-service-for-participant-of-visit-room")
    String saveUpdateAdditionalServiceForParticipantOfVisitRoom(@RequestParam("visitRoomId") UUID visitId,@RequestParam("participantVisitId") UUID participantVisitId,@RequestParam("price") double price,@RequestParam("service") String service) {
        ParticipantOfvisitRoom participantOfvisitRoom = participantOfvisitRoomRepository.findById(participantVisitId).orElse(null);
        assert participantOfvisitRoom != null;
        participantOfvisitRoom.setService_suplementaire(service);
        participantOfvisitRoom.setService_suplementaire_price(price);
        participantOfvisitRoomRepository.save(participantOfvisitRoom);

        return "redirect:/reception/visit-participant-profile?visitId=" + visitId+"&participantId="+participantOfvisitRoom.getId();
    }

    @PostMapping("delete-Additional-service-for-participant-of-visit-room")
    String deleteAdditionalServiceForParticipantOfVisitRoom(@RequestParam("visitRoomId") UUID visitId,@RequestParam("participantVisitId") UUID participantVisitId) {
        ParticipantOfvisitRoom participantOfvisitRoom = participantOfvisitRoomRepository.findById(participantVisitId).orElse(null);
        assert participantOfvisitRoom != null;
        participantOfvisitRoom.setService_suplementaire(null);
        participantOfvisitRoom.setService_suplementaire_price(0);
        participantOfvisitRoomRepository.save(participantOfvisitRoom);

        return "redirect:/reception/visit-participant-profile?visitId=" + visitId+"&participantId="+participantOfvisitRoom.getId();
    }


    //============================CHANGE PASSWORD=================================

    @GetMapping("update-password")
    String UpdatePassword(@RequestParam(name = "message", required = false) String message ,Model model) {
        if (message == null) {message="";}
        model.addAttribute("message", message);
        return "Receptionist_espace/update-password-form";
    }
    @PostMapping("new-password")
    String newPassword(@RequestParam("password") String oldPassword ,Model model) {
        Receptionist receptionist=receptionistService.getProfile();
        if (passwordEncoder.matches(oldPassword, receptionist.getPassword())) {
            // Si le mot de passe est correct, on peut changer le mot de passe
            return "Receptionist_espace/new-password-form";
        } else {
            // Mot de passe actuel incorrect
            return "redirect:/reception/update-password?message=Mot de passe actuel incorrect";
        }

    }

    @PostMapping("save-new-password")
    String SaveNewPassword(@RequestParam("newPassword") String newPassword ,Model model) {
        Receptionist receptionist=receptionistService.getProfile();
        receptionist.setPassword(passwordEncoder.encode(newPassword));
        receptionistRepository.save(receptionist);
        return "Receptionist_espace/password-changed-successfully";
    }

    //////////////////////////////////////////zt

    @GetMapping("list-snacks")
    public String snackStocke(Model model) {
        List<SnacksAndBoissons> snacksAndBoissonsList = managerServiceImp.getAllSnacks();
        // Convertir les images en Base64
        for (SnacksAndBoissons snack : snacksAndBoissonsList) {
            if (snack.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(snack.getImage());
                snack.setBase64Image(base64Image);
            }
        }
        model.addAttribute("snacks", snacksAndBoissonsList);
        return "Receptionist_espace/list-snacks";
    }

    //====================Resubscribe==========================
    @GetMapping("Resubscribe")
    String getQrCodeScanner() {
        return "Receptionist_espace/Resubscribe";
    }

    @GetMapping("Resubscribe-of-subscriber")
    public String newVisit(@RequestParam("result") String result, Model model) {
        Subscriber subscriber = subscriberRepository.findById(UUID.fromString(result)).orElse(null);
        model.addAttribute("subscriber", subscriber);
        List<SubscriptionType> subscriptionTypes = subscriptionTypeRepository.findAll();
        model.addAttribute("subscriptionTypes", subscriptionTypes);
        return "Receptionist_espace/Resubscribe-of-subscriber-form";
    }

    @PostMapping("save-Resubscribe-of-subscriber")
    public String saveResubscribeOfSubscriber(@ModelAttribute Subscriber subscriber, @RequestParam("subscriptionType_id") UUID subscriptionType_id, Model model) {
        managerService.saveResubscribeOfSubscriber(subscriber, subscriptionType_id);
        return "redirect:/reception/Resubscription-completed-successfully?subscriberId=" + subscriber.getId();

    }

    @GetMapping("Resubscription-completed-successfully")
    public String ResubscriptionCompletedSuccessfully(@RequestParam("subscriberId") UUID subscriberId, Model model) {
        Subscriber subscriber1 = subscriberRepository.findById(subscriberId).orElse(null);
        model.addAttribute("subscriber", subscriber1);
        return "Receptionist_espace/Resubscription-completed-successfully";

    }
    //====================visit of team=============================
    @GetMapping("visit-of-team")
    String VisitOfTeam() {
        return "Receptionist_espace/visit-of-team";
    }

    @GetMapping("new-visit-of-team")
    public String newVisitOfTeam(@RequestParam("result") String result) {
        return receptionistService.newVisitOfTeam(result);
    }

    @GetMapping("new-visit-of-team-profile")
    public String newVisitOfTeamProfile(@RequestParam("userId") String result) {
        return receptionistService.newVisitOfTeamProfile(result);
    }

    @GetMapping("visit-of-team-profile")
    public String visitOfTeamProfile(@RequestParam("visitId") UUID visitId ,Model model) {
        VisitOfTeam visitOfTeam= visitOfTeamRepository.findById(visitId).orElse(null);
        assert visitOfTeam != null;
        model.addAttribute("visit", visitOfTeam);
        List<SnacksAndBoissonsOfVisit> snacksAndBoissonsList = visitOfTeam.getSnacksAndBoissonsOfVisits();
        // Convertir les images en Base64

        for (SnacksAndBoissonsOfVisit snack : snacksAndBoissonsList) {

            if (snack.getSnacksAndBoissons().getImage() != null) {

                String base64Image = Base64.getEncoder().encodeToString(snack.getSnacksAndBoissons().getImage());

                snack.getSnacksAndBoissons().setBase64Image(base64Image);
            }
        }
        model.addAttribute("snacks", snacksAndBoissonsList);
        return "Receptionist_espace/visit-of-team-profile";
    }

    @PostMapping("add-snacks-to-visit-of-team")
    public String visitOfTeamSnacks(@RequestParam("visitId") UUID visitId, Model model) {

        model.addAttribute("snaks", new SnackForm());

        model.addAttribute("visitOfTeam_id", visitId);

        List<SnacksAndBoissons> snacksAndBoissonsList = snacksAndBoissonsRepository.findAll();
        // Convertir les images en Base64
        for (SnacksAndBoissons snack : snacksAndBoissonsList) {

            if (snack.getImage() != null) {

                String base64Image = Base64.getEncoder().encodeToString(snack.getImage());

                snack.setBase64Image(base64Image);
            }
        }

        model.addAttribute("snacksAndBoissan", snacksAndBoissonsList);

        return "/Receptionist_espace/add-snacks-to-visit-of-team";

    }

    @PostMapping("save-snacks-to-visit-of-team")
    public String saveSnacksToVisitOfTeam(@RequestParam("visitId") UUID visitId, @ModelAttribute SnackForm snackForm) {

        receptionistService.saveSnacksToVisitOfTeam(visitId,snackForm);

        return "redirect:/reception/visit-of-team-profile?visitId=" + visitId;
    }


}
