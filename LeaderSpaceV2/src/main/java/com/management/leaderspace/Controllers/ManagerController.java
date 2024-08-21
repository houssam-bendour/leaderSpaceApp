package com.management.leaderspace.Controllers;

import com.management.leaderspace.Entities.*;
import com.management.leaderspace.Repositories.*;
import com.management.leaderspace.Services.Manager.ManagerService;
import com.management.leaderspace.Services.Manager.ManagerServiceImp;
import com.management.leaderspace.model.CaisseService;
import com.management.leaderspace.model.DesignationForm;
import com.management.leaderspace.model.NumberToWordsService;
import com.management.leaderspace.model.QrCodeGenerator;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/manager")
@AllArgsConstructor
@Transactional
public class ManagerController {
    private final SnacksAndBoissonsOfVisitRepository snacksAndBoissonsOfVisitRepository;
    private final SnacksAndBoissonsRepository snacksAndBoissonsRepository;
    private final SnacksAndBoissonsHistoryRepository snacksAndBoissonsHistoryRepository;
    private final ManagerRepository managerRepository;
    private final ReceptionistRepository receptionistRepository;
    private final VisitRepository visitRepository;
    private final VisitOfRoomRepository visitOfRoomRepository;
    private final VisitOfDeskRepository visitOfDeskRepository;
    private SubscriptionTypeRepository subscriptionTypeRepository;
    private final SubscriberRepository subscriberRepository;
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private ManagerService managerService;
    private NotSubscriberRepository notSubscriberRepository;
    private ServiceTypeRepository serviceTypeRepository;
    private DevisRepository devisRepository;
    private final DesignationRepository designationRepository;
    private final FactureRepository factureRepository;
    private final ContratRepository contratRepository;
    private final ManagerServiceImp managerServiceImp;
    private final DomiciliationFactureRepository domiciliationFactureRepository;
    PasswordEncoder passwordEncoder;
    private VisitOfTeamRepository visitOfTeamRepository;
    private CaisseRepository caisseRepository;

    @GetMapping("/add-snack")
    public String showAddSnackForm() {
        return "/Manager_espace/add-snack-form";
    }

    @PostMapping("/save-snack")
    public String addSnack(@RequestParam String name,
                           @RequestParam(required = false) Long quantity,
                           @RequestParam double purchasePrice,
                           @RequestParam double sellingPrice,
                           @RequestParam(required = false) Integer requiredQuantity,
                           @RequestParam String type,
                           @RequestParam MultipartFile imageFile,
                           Model model) {
        try {
            SnacksAndBoissons snack = managerServiceImp.saveSnackWithImage(name, sellingPrice, purchasePrice, requiredQuantity, type, imageFile, quantity);
            model.addAttribute("snack", snack);
            model.addAttribute("success", "Snack ajouté avec succès");
        } catch (IOException e) {
            model.addAttribute("error", "Erreur lors de l'ajout du snack");
        }
        return "redirect:/manager/list-snacks";
    }

    @GetMapping("list-snacks")
    public String listSnacks(Model model) {
        List<SnacksAndBoissons> snacksAndBoissonsList = managerServiceImp.getAllSnacks();
        // Convertir les images en Base64
        for (SnacksAndBoissons snack : snacksAndBoissonsList) {
            if (snack.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(snack.getImage());
                snack.setBase64Image(base64Image);
            }
        }
        model.addAttribute("snacks", snacksAndBoissonsList);
        return "/Manager_espace/list-snacks";
    }

    @GetMapping("new-quantity-snack")
    public String newQuantityOfSnack(@RequestParam("snack_id") UUID snack_id, Model model) {
        SnacksAndBoissons snack = snacksAndBoissonsRepository.findById(snack_id).orElse(null);
        model.addAttribute("snack", snack);
        return "/Manager_espace/new-quantity-snack";
    }

    @PostMapping("save-new-quantity-snack")
    public String saveNewQuantitySnack(@ModelAttribute SnacksAndBoissons snack) {

        SnacksAndBoissons oldSnack = snacksAndBoissonsRepository.findById(snack.getId()).orElse(null);
        assert oldSnack != null;
        long q = snack.getQuantity();
        snack.setQuantity(oldSnack.getQuantity() + snack.getQuantity());
        snacksAndBoissonsRepository.save(snack);
        SnacksAndBoissonsHistory snacksAndBoissonsHistory = new SnacksAndBoissonsHistory();
        snacksAndBoissonsHistory.setQuantity(q);
        snacksAndBoissonsHistory.setSelling_price(snack.getSelling_price());
        snacksAndBoissonsHistory.setPurchase_price(snack.getPurchase_price());
        snacksAndBoissonsHistory.setSnacksAndBoissons(snack);

        ZonedDateTime nowInMorocco = ZonedDateTime.now(ZoneId.of("Africa/Casablanca"));
        LocalDate currentDate = nowInMorocco.toLocalDate();
        LocalTime currentTime = nowInMorocco.toLocalTime();

        snacksAndBoissonsHistory.setPurchase_date(currentDate);
        snacksAndBoissonsHistory.setPurchase_time(currentTime);

        snacksAndBoissonsHistoryRepository.save(snacksAndBoissonsHistory);
        return "redirect:/manager/list-snacks";
    }

    @GetMapping("update-snack")
    public String updateSnack(@RequestParam UUID snack_id, Model model) {
        SnacksAndBoissons snack = snacksAndBoissonsRepository.findById(snack_id).orElse(null);
        model.addAttribute("snack", snack);
        return "/Manager_espace/update-snack";
    }

    @PostMapping("updated-snack")
    public String updatedSnack(@ModelAttribute SnacksAndBoissons snack, Model model) {
        snacksAndBoissonsRepository.save(snack);
        return "redirect:/manager/list-snacks";
    }


    @GetMapping("list-NotSubscribers")
    public String listNotSubscribers(Model model) {
        List<NotSubscriber> notSubscribers = notSubscriberRepository.findAll();
        List<String> notSubscriberQrCode = new ArrayList<>();
        for (NotSubscriber notSubscriber : notSubscribers) {
            notSubscriberQrCode.add(QrCodeGenerator.generateQrCodeBase64(notSubscriber.getId().toString()));
        }
        model.addAttribute("QrCode", notSubscriberQrCode);
        return "/Manager_espace/list-NotSubscribers-QrCode";
    }


    @GetMapping("devis")
    public String devisForm(Model model) {
        List<SubscriptionType> subscriptionTypes = subscriptionTypeRepository.findAll();
        List<ServiceType> serviceTypes = serviceTypeRepository.findAll();
        Devis devis = new Devis();
        devis = devisRepository.save(devis);
        model.addAttribute("designationForm", new DesignationForm());
        model.addAttribute("devis", devis);
        model.addAttribute("subscriptionTypes", subscriptionTypes);
        model.addAttribute("serviceTypes", serviceTypes);
        return "Manager_espace/devis-form";
    }

    @GetMapping("facture")
    public String factureForm(Model model) {
        List<SubscriptionType> subscriptionTypes = subscriptionTypeRepository.findAll();
        Facture facture = new Facture();
        facture = factureRepository.save(facture);
        model.addAttribute("designationForm", new DesignationForm());
        model.addAttribute("facture", facture);
        model.addAttribute("subscriptionTypes", subscriptionTypes);
        return "Manager_espace/facture-form";
    }

    @PostMapping("save-services-to-facture")
    public String saveServicesToFacture(@RequestParam("factureId") UUID factureId, @ModelAttribute DesignationForm designationForm, Model model) {
        managerService.saveServicesToFacture(factureId, designationForm);
        Facture facture = factureRepository.findById(factureId).get();
        model.addAttribute("facture", facture);
        return "Manager_espace/facture-service-form";
    }

    @PostMapping("save-services-to-devis")
    public String saveServicesToDevis(@RequestParam("devisId") UUID devisId, @ModelAttribute DesignationForm designationForm, Model model) {
        managerService.saveServicesToDevis(devisId, designationForm);
        Devis devis = devisRepository.findById(devisId).get();
        model.addAttribute("devis", devis);
        return "Manager_espace/devis-service-form";
    }

//    @PostMapping("/new-devis-form")
//    public String DevisForm(@ModelAttribute Designation designation, Model model) {
//        Devis devis=devisRepository.findById(designation.getDevis().getId()).get();
//        model.addAttribute("devis", devis);
//        return "devis-service-form";
//    }
//    primary methode
//    @PostMapping("submit-devis")
//    public String SaveDevis(@ModelAttribute Devis devis, Model model) {
//        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");
//
//        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);
//
//        LocalDate localDate = moroccoDateTime.toLocalDate();
////        System.out.println("******************devis.getId()***********************");
////        System.out.println(devis.getId());
////        Devis existingDevis = devisRepository.findById(devis.getId()).orElse(null);
//
////        System.out.println("*****************************************");
////        System.out.println(existingDevis==null);
//        devis.setDate(localDate);
//        int Devis_N = devisRepository.findAll().size();
//        if (Devis_N <10)
//            devis.setDevis_N("0"+Devis_N);
//        else
//            devis.setDevis_N(String.valueOf(Devis_N));
//        double totalTTC=0;
//        double totalHT=0;
//        double totalTVA=0;
//        System.out.println("*****************************************");
//        System.out.println(devis.getDesignations().isEmpty());
//        for (DesignationOfDevis designationOfDevis : devis.getDesignations()) {
//            System.out.println(designationOfDevis.getQuantity()+"*"+designationOfDevis.getDesignation().getTTC());
//            System.out.println(designationOfDevis.getQuantity()+"*"+designationOfDevis.getDesignation().getHT());
//            System.out.println(designationOfDevis.getQuantity()+"*"+designationOfDevis.getDesignation().getTVA());
//            System.out.println("*****************************************");
//
//            totalTTC+=designationOfDevis.getQuantity()*designationOfDevis.getDesignation().getTTC();
//            totalHT+=designationOfDevis.getQuantity()*designationOfDevis.getDesignation().getHT();
//            totalTVA+=designationOfDevis.getQuantity()*designationOfDevis.getDesignation().getTVA();
//        }
////        existingDevis.setDesignations(devis.getDesignations());
//        devis.setTotal_HT(totalHT);
//        devis.setTotal_TTC(totalTTC);
//        devis.setTotal_TVA(totalTVA);
//        devisRepository.save(devis);
//
//        model.addAttribute("devis", devis);
//        return "Manager_espace/new-devis-pdf";
//    }

    @PostMapping("submit-devis")
    public String saveDevis(@ModelAttribute Devis devis, Model model) {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");
        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);
        LocalDate localDate = moroccoDateTime.toLocalDate();

        Devis existingDevis = devisRepository.findById(devis.getId()).orElse(null);

        if (existingDevis != null) {
            // Update the necessary fields
            existingDevis.setDate(localDate);
            int currentYear = localDate.getYear();
            int devisCount = devisRepository.findAll().size();
            if (devisCount < 10) {
                existingDevis.setDevis_N("0" + devisCount + "/" + currentYear);
            } else {
                existingDevis.setDevis_N(devisCount + "/" + currentYear);
            }
            existingDevis.setClient_ID(devis.getClient_ID());
            existingDevis.setClient_name(devis.getClient_name());
            existingDevis.setPrice_validity_period(devis.getPrice_validity_period());
            double totalTTC = 0;
            double totalHT = 0;

            for (DesignationOfDevis designationOfDevis : existingDevis.getDesignations()) {
                if (designationOfDevis.getServiceType() != null && designationOfDevis.getSubscriptionType() == null) {
                    totalTTC += designationOfDevis.getQuantity() * designationOfDevis.getServiceType().getPrice();
                    totalHT += designationOfDevis.getQuantity() * designationOfDevis.getServiceType().getHT();
                    // totalTVA += designationOfDevis.getQuantity() * designationOfDevis.getDesignation().getTVA();
                } else {
                    totalTTC += designationOfDevis.getQuantity() * designationOfDevis.getSubscriptionType().getPrice();
                    totalHT += designationOfDevis.getQuantity() * designationOfDevis.getSubscriptionType().getHT();

                }
            }
            existingDevis.setTotal_HT(Math.round(totalHT * 100.0) / 100.0);
            existingDevis.setTotal_TTC(totalTTC);
            existingDevis.setTotal_TVA(Math.round((totalHT * 0.2) * 100.0) / 100.0);

            // Save the updated Devis
            devisRepository.save(existingDevis);

            model.addAttribute("devis", existingDevis);
        }

        return "Manager_espace/new-devis-pdf";
    }

    @PostMapping("confirm-facture")
    public String confirmFacture(@ModelAttribute Facture facture, Model model) {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");
        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);
        LocalDate localDate = moroccoDateTime.toLocalDate();

        Facture existingFacture = factureRepository.findById(facture.getId()).orElse(null);

        if (existingFacture != null) {
            // Update the necessary fields
            existingFacture.setDate(localDate);
            int currentYear = localDate.getYear();
            int factureCount = factureRepository.findAll().size() + domiciliationFactureRepository.findAll().size();
            if (factureCount < 10) {
                existingFacture.setFacture_N("0" + factureCount + "/" + currentYear);
            } else {
                existingFacture.setFacture_N(factureCount + "/" + currentYear);
            }
            existingFacture.setClient_ID(facture.getClient_ID());
            existingFacture.setClient_name(facture.getClient_name());
            existingFacture.setPayment_method(facture.getPayment_method());
            double totalTTC = 0;
            double totalHT = 0;

            for (DesignationOfDevis designationOfDevis : existingFacture.getDesignations()) {
                totalTTC += designationOfDevis.getQuantity() * designationOfDevis.getSubscriptionType().getPrice();
                totalHT += designationOfDevis.getQuantity() * designationOfDevis.getSubscriptionType().getHT();
                //totalTVA += designationOfDevis.getQuantity() * designationOfDevis.getDesignation().getTVA();
            }

            existingFacture.setTotal_HT(Math.round(totalHT * 100.0) / 100.0);
            existingFacture.setTotal_TTC(totalTTC);
            existingFacture.setTotal_TVA(Math.round((totalHT * 0.2) * 100.0) / 100.0);
            existingFacture.setTotal_TTC_to_words(new NumberToWordsService().convertToWords((long) totalTTC));
            // Save the updated Devis
            factureRepository.save(existingFacture);

            model.addAttribute("facture", existingFacture);
        }

        return "Manager_espace/confirm-facture";
    }

    @PostMapping("submit-facture")
    public String saveFacture(@ModelAttribute Facture facture, Model model) {

        Facture existingFacture = factureRepository.findById(facture.getId()).orElse(null);

        if (existingFacture != null) {
            // Update the necessary fields
//            existingFacture.setDate(facture.getDate());
//            existingFacture.setFacture_N(facture.getFacture_N());
//            existingFacture.setClient_ID(facture.getClient_ID());
//            existingFacture.setClient_name(facture.getClient_name());
//            existingFacture.setPayment_method(facture.getPayment_method());
//            existingFacture.setTotal_HT(facture.getTotal_HT());
//            existingFacture.setTotal_TTC(facture.getTotal_TTC() );
//            existingFacture.setTotal_TVA(facture.getTotal_TVA());
            existingFacture.setTotal_TTC_to_words(facture.getTotal_TTC_to_words());
            factureRepository.save(existingFacture);
            model.addAttribute("facture", existingFacture);
        }

        return "Manager_espace/new-facture-pdf";
    }

    //    @PostMapping("submit-devis")
//    public String SaveDevis(@ModelAttribute Devis devis, Model model) {
//        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");
//        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);
//        LocalDate localDate = moroccoDateTime.toLocalDate();
//        devis.setDate(localDate);
//
//        // Check if the devis already exists
//        Optional<Devis> existingDevisOptional = devisRepository.findById(devis.getId());
//        Devis existingDevis = existingDevisOptional.orElse(null);
//
//        if (existingDevis != null) {
//            devis.setDevis_N(existingDevis.getDevis_N());
//            // Clear existing designations and add the new ones
//            existingDevis.getDesignations().clear();
//            existingDevis.getDesignations().addAll(devis.getDesignations());
//            devis = existingDevis;
//        } else {
//            int Devis_N = devisRepository.findAll().size() + 1;
//            devis.setDevis_N(Devis_N < 10 ? "0" + Devis_N : String.valueOf(Devis_N));
//        }
//
//        double totalTTC = 0;
//        double totalHT = 0;
//        double totalTVA = 0;
//        System.out.println("*****************************************");
//        System.out.println(devis.getDesignations().isEmpty());
//        for (DesignationOfDevis designationOfDevis : devis.getDesignations()) {
//            System.out.println(designationOfDevis.getQuantity() + "*" + designationOfDevis.getDesignation().getTTC());
//            System.out.println(designationOfDevis.getQuantity() + "*" + designationOfDevis.getDesignation().getHT());
//            System.out.println(designationOfDevis.getQuantity() + "*" + designationOfDevis.getDesignation().getTVA());
//            System.out.println("*****************************************");
//
//            totalTTC += designationOfDevis.getQuantity() * designationOfDevis.getDesignation().getTTC();
//            totalHT += designationOfDevis.getQuantity() * designationOfDevis.getDesignation().getHT();
//            totalTVA += designationOfDevis.getQuantity() * designationOfDevis.getDesignation().getTVA();
//        }
//        devis.setTotal_HT(totalHT);
//        devis.setTotal_TTC(totalTTC);
//        devis.setTotal_TVA(totalTVA);
//
//        // Save the devis and its designations
//        devisRepository.save(devis);
//
//        model.addAttribute("devis", devis);
//        return "Manager_espace/new-devis-pdf";
//    }
    @GetMapping("contrat")
    public String contratForm(Model model) {
        String Cooperative = "coopérative";
        String Association = "association";
        String Entreprise = "entreprise";
        model.addAttribute("Cooperative", Cooperative);
        model.addAttribute("Association", Association);
        model.addAttribute("Entreprise", Entreprise);
        model.addAttribute("contrat", new Contrat());
        return "Manager_espace/contrat-fr-form";
    }

    @PostMapping("submit-contrat-fr")
    public String saveContratFR(@ModelAttribute Contrat contrat, Model model) {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");
        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);
        LocalDate localDate = moroccoDateTime.toLocalDate();
        contrat.setDate(localDate);
        contrat.setLangue("fr");
        DomiciliationFacture domiciliationFacture = new DomiciliationFacture();
        domiciliationFacture.setDate(localDate);
        int currentYear = localDate.getYear();
        int factureCount = domiciliationFactureRepository.findByYear(currentYear).size() + factureRepository.findByYear(currentYear).size() + 1;
        if (factureCount < 10) {
            domiciliationFacture.setFacture_N("0" + factureCount + "/" + currentYear);
        } else {
            domiciliationFacture.setFacture_N(factureCount + "/" + currentYear);
        }
        if (contrat.getType_client().equals("Entreprise")) {
            domiciliationFacture.setClient_ID("ICE: " + contrat.getBusiness_id());
            domiciliationFacture.setClient_name("STE " + contrat.getBusiness_name());
        } else if (contrat.getType_client().equals("Association")) {
            domiciliationFacture.setClient_ID("Numéro d’enregistrement ordinal : " + contrat.getAssociation_number());
            domiciliationFacture.setClient_name("ASSOCIATION " + contrat.getBusiness_name());
        } else {
            domiciliationFacture.setClient_ID("Numéro d’enregistrement ordinal : " + contrat.getAssociation_number());
            domiciliationFacture.setClient_name("COOPERATIVE " + contrat.getBusiness_name());
        }
        domiciliationFacture.setTotal_HT(contrat.getMontant_de_location_chiffre() * contrat.getDuree_par_moi_chifre() / 1.2);
        domiciliationFacture.setTotal_TTC(contrat.getMontant_de_location_chiffre() * contrat.getDuree_par_moi_chifre());
        domiciliationFacture.setTotal_TVA(domiciliationFacture.getTotal_TTC() - domiciliationFacture.getTotal_HT());
        domiciliationFacture.setTotal_TTC_to_words(new NumberToWordsService().convertToWords((long) domiciliationFacture.getTotal_TTC()));
        domiciliationFactureRepository.save(domiciliationFacture);
        contrat.setDomiciliationFacture(domiciliationFacture);
        contratRepository.save(contrat);
        model.addAttribute("contrat", contrat);
        return "Manager_espace/new-contrat-fr-pdf";
    }

    @PostMapping("submit-contrat-ar")
    public String saveContratAR(@ModelAttribute Contrat contrat, Model model) {
        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");
        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);
        LocalDate localDate = moroccoDateTime.toLocalDate();
        contrat.setDate(localDate);
        contrat.setLangue("ar");
        DomiciliationFacture domiciliationFacture = new DomiciliationFacture();
        domiciliationFacture.setDate(localDate);
        int currentYear = localDate.getYear();
        int factureCount = domiciliationFactureRepository.findByYear(currentYear).size() + factureRepository.findByYear(currentYear).size() + 1;
        if (factureCount < 10) {
            domiciliationFacture.setFacture_N("0" + factureCount + "/" + currentYear);
        } else {
            domiciliationFacture.setFacture_N(factureCount + "/" + currentYear);
        }
        if (contrat.getType_client().equals("Entreprise")) {
            domiciliationFacture.setClient_ID("ICE: " + contrat.getBusiness_id());
            domiciliationFacture.setClient_name("STE :" + contrat.getBusiness_name());
        } else if (contrat.getType_client().equals("Association")) {
            domiciliationFacture.setClient_ID("Numéro d’enregistrement ordinal : " + contrat.getAssociation_number());
            domiciliationFacture.setClient_name("ASSOCIATION :" + contrat.getBusiness_name());
        } else {
            domiciliationFacture.setClient_ID("Numéro d’enregistrement ordinal : " + contrat.getAssociation_number());
            domiciliationFacture.setClient_name("COOPERATIVE :" + contrat.getBusiness_name());
        }
        domiciliationFacture.setTotal_HT(contrat.getMontant_de_location_chiffre() * contrat.getDuree_par_moi_chifre() / 1.2);
        domiciliationFacture.setTotal_TTC(contrat.getMontant_de_location_chiffre() * contrat.getDuree_par_moi_chifre());
        domiciliationFacture.setTotal_TVA(domiciliationFacture.getTotal_TTC() - domiciliationFacture.getTotal_HT());
        domiciliationFacture.setTotal_TTC_to_words(new NumberToWordsService().convertToWords((long) domiciliationFacture.getTotal_TTC()));
        domiciliationFactureRepository.save(domiciliationFacture);
        contrat.setDomiciliationFacture(domiciliationFacture);
        contratRepository.save(contrat);
        model.addAttribute("contrat", contrat);
        return "Manager_espace/new-contrat-ar-pdf";
    }

    @PostMapping("confirm-domiciliation-facture")
    public String confirmDomiciliationFacture(@RequestParam("factureId") UUID factureId, Model model) {
        DomiciliationFacture facture = domiciliationFactureRepository.findById(factureId).orElse(null);
        model.addAttribute("facture", facture);
        return "Manager_espace/confirm-domiciliation-facture";
    }

    @PostMapping("domiciliationFacture")
    public String domiciliationFacture(@RequestParam("factureId") UUID factureId, @RequestParam("total_TTC_to_words") String total, Model model) {
        DomiciliationFacture facture = domiciliationFactureRepository.findById(factureId).orElse(null);
        assert facture != null;
        facture.setTotal_TTC_to_words(total);
        domiciliationFactureRepository.save(facture);
        model.addAttribute("facture", facture);
        return "Manager_espace/domiciliation-facture";
    }

    @PostMapping("annulation-du-contrat")
    public String annulationContrat(@RequestParam("contratId") UUID contratId) {
        Contrat contrat = contratRepository.findById(contratId).get();
        contratRepository.deleteById(contratId);
        domiciliationFactureRepository.deleteById(contrat.getDomiciliationFacture().getId());
        return "redirect:/";
    }

    @PostMapping("annulation-du-devis")
    public String AnnulationDevis(@RequestParam("devisId") UUID devisId) {
        devisRepository.deleteById(devisId);
        return "redirect:/";
    }

    @PostMapping("annulation-du-facture")
    public String AnnulationFacture(@RequestParam("factureId") UUID factureId) {
        factureRepository.deleteById(factureId);
        return "redirect:/";
    }

    @GetMapping("turnover")
    public String turnover(@RequestParam(name = "date_debut", required = false) String ch_date_debut,
                           @RequestParam(name = "date_fin", required = false) String ch_date_fin,
                           @RequestParam(name = "turnover", required = false) String turnover, Model model) {

        List<Visit> listAllVisitsBetweenStartDayAndEndTime = new ArrayList<>();
        List<SubscriptionHistory> allSubscriptionsByDateDebutBetweenStartDayAndEndDay = new ArrayList<>();
        Map<UUID, Double> mapTotalPriceOfSnacksAndBoissonsByVisit = new HashMap<>();
        List<VisitOfRoom> allVisitsOfRoomByDate= new ArrayList<>();
        Map<UUID,Double> sommeOfSnacksAndBoissonsOfRoom = new HashMap<>();
        Map<UUID,Map<UUID,Double>> mapSommeOfSnacksAndBoissonsForParticipantOfVisitRoom = new HashMap<>();
        Map<UUID,Double> mapCalculeSommeForVisitOfRoom = new HashMap<>();
        List<VisitOfDesk> allVisitsOfDesdByDate= new ArrayList<>();
        Map<UUID,Double> sommeOfsnacksAndBoissonsByVisit = new HashMap<>();
        List<Contrat> allContractsByDate = new ArrayList<>();
        double totaleMontantOfContractsByDates;
        double sommeServiePriceSupplementaireOfVisits;
        double sommeServiceSuplimentaireOfVisitRoom;
        double sommeServiceSupplimentaiePriceOfDisk;
        //////////////////////////////////////////////
        List<VisitOfTeam> allVisitsOfTeamByDate = new ArrayList<>();
        Map<UUID,Double> sommeSnacksAndBoissonsByVisitForTeam = new HashMap<>();
        double sommeServiceSupplimentairePriceOfTeam;
        double sommeSnacksAndBoissonsForVisitsTeam;

        Map<UUID,Double> sommeSnacksAndBoissonsByVisitOfTeam = new HashMap<>();
        if (ch_date_debut != null && ch_date_fin != null) {
            if (ch_date_debut.isEmpty() || ch_date_fin.isEmpty()) {
                return "redirect:/manager/turnover";
            } else {
                if(turnover.equals("chart")){
                    return "redirect:/manager/charts?date_debut="+ch_date_debut+"&date_fin="+ch_date_fin;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dateDebut = LocalDate.parse(ch_date_debut, formatter);
                LocalDate dateFin = LocalDate.parse(ch_date_fin, formatter);

                listAllVisitsBetweenStartDayAndEndTime = managerServiceImp.getAllVisitsBetweenStartDayAndEndTime(dateDebut, dateFin);
                allSubscriptionsByDateDebutBetweenStartDayAndEndDay = managerServiceImp.getAllSubscriptionsByDateDebutBetweenStartDayAndEndDay(dateDebut, dateFin);
                mapTotalPriceOfSnacksAndBoissonsByVisit = managerServiceImp.getTotalPriceOfSnacksAndBoissons(listAllVisitsBetweenStartDayAndEndTime);
                allVisitsOfRoomByDate = managerServiceImp.findVisitOfRoomByDate(dateDebut,dateFin);
                sommeOfSnacksAndBoissonsOfRoom = managerServiceImp.sommeOfSnacksAndBoissonsOfRoom(allVisitsOfRoomByDate);
                mapSommeOfSnacksAndBoissonsForParticipantOfVisitRoom = managerServiceImp.sommeOfSnacksAndBoissonsForParticipantOfVisitRoom(allVisitsOfRoomByDate);
                mapCalculeSommeForVisitOfRoom = managerServiceImp.calculeSommeForVisitOfRoom(allVisitsOfRoomByDate,sommeOfSnacksAndBoissonsOfRoom,mapSommeOfSnacksAndBoissonsForParticipantOfVisitRoom);
                ///////////////////////////////////////////////////////////////
                allVisitsOfDesdByDate  = managerServiceImp.findVisitOfDeskByDate(dateDebut,dateFin);
                sommeOfsnacksAndBoissonsByVisit = managerServiceImp.sommeOfsnacksAndBoissonsByVisit(allVisitsOfDesdByDate);
                //////////////////////////////////////
                allContractsByDate = contratRepository.allContractByDate(dateDebut,dateFin);
                totaleMontantOfContractsByDates = contratRepository.totaleMontantOfContractByDates(dateDebut,dateFin);
                //////////////////////////////////////////////////////
                sommeServiePriceSupplementaireOfVisits = visitRepository.sommeServiePriceSupplementaireOfVisits(dateDebut,dateFin);
                sommeServiceSuplimentaireOfVisitRoom= visitOfRoomRepository.sommeServiceSuplimentaireOfVisitRoom(dateDebut,dateFin);
                sommeServiceSupplimentaiePriceOfDisk = visitOfDeskRepository.sommeServiceSupplimentaiePriceOfDisk(dateDebut,dateFin);

                ///////////////////////////////////////////////////
                allVisitsOfTeamByDate = visitOfTeamRepository.allVisitOfTeamByDate(dateDebut,dateFin);
                sommeSnacksAndBoissonsByVisitForTeam = managerService.sommeOfSnacksAndBoissonsByVisitFomTeam(allVisitsOfTeamByDate);
                sommeServiceSupplimentairePriceOfTeam = visitOfTeamRepository.sommeServiceSupplimentaiePriceForTeams(dateDebut,dateFin);
                sommeSnacksAndBoissonsForVisitsTeam=managerService.sommeSnacksAndBoissonsForVisitsTeam(sommeSnacksAndBoissonsByVisitOfTeam);


            }
            model.addAttribute("listAllVisitBetweenStartDayAndEndTime", listAllVisitsBetweenStartDayAndEndTime);

            model.addAttribute("mapTotalPriceOfSnacksAndBoissonsByVisit", mapTotalPriceOfSnacksAndBoissonsByVisit);

            model.addAttribute("totalPriceByVisits", managerServiceImp.calculeTotale(listAllVisitsBetweenStartDayAndEndTime, mapTotalPriceOfSnacksAndBoissonsByVisit));

            model.addAttribute("allSubscriptionsByDateDebutBetweenStartDayAndEndDay", allSubscriptionsByDateDebutBetweenStartDayAndEndDay);

            model.addAttribute("totaleOfSubscriptions", managerServiceImp.calculeTotaleOfSubscriptions(allSubscriptionsByDateDebutBetweenStartDayAndEndDay));

            model.addAttribute("allVisitsOfRoomByDate",allVisitsOfRoomByDate);

            model.addAttribute("sommeOfSnacksAndBoissonsOfRoom",sommeOfSnacksAndBoissonsOfRoom);

            model.addAttribute("mapSommeOfSnacksAndBoissonsForParticipantOfVisitRoom",mapSommeOfSnacksAndBoissonsForParticipantOfVisitRoom);

            model.addAttribute("mapCalculeSommeForVisitOfRoom", mapCalculeSommeForVisitOfRoom);

            model.addAttribute("totaleOfAllVisitsOfRoom",managerServiceImp.calculeTotaleOfAllVisitsOfRoom(mapCalculeSommeForVisitOfRoom));
            ////////////////////////////////////////////
            model.addAttribute("allvisitsOfDeskByDate",allVisitsOfDesdByDate);

            model.addAttribute("sommeOfsnacksAndBoissonsByVisit",sommeOfsnacksAndBoissonsByVisit);

            model.addAttribute("totaleOfVisitsOfDesk",managerServiceImp.totaleOfVisitsOfDesk(allVisitsOfDesdByDate,sommeOfsnacksAndBoissonsByVisit));
            /////////////////////////////////////////////////////////////
            model.addAttribute("sommeConsommationsNormaleVisits",managerServiceImp.calculeSommeOfConsommations(mapTotalPriceOfSnacksAndBoissonsByVisit));
            model.addAttribute("sommeConsommationsForRoom",managerServiceImp.calculeSommeOfConsommations(sommeOfSnacksAndBoissonsOfRoom));
            model.addAttribute("sommeConsommationsForDesk",managerServiceImp.calculeSommeOfConsommations(sommeOfsnacksAndBoissonsByVisit));
            model.addAttribute("sommeConsommationsForRoomParticipants",managerServiceImp.calculeSommeConsommationForRoomParticipants(mapSommeOfSnacksAndBoissonsForParticipantOfVisitRoom));

            model.addAttribute("sommeServicePriceOfNormaleVisits",managerServiceImp.calculeSommeOfServicePriceForNormaleVisits(listAllVisitsBetweenStartDayAndEndTime));
            model.addAttribute("sommeServicePriceForRoomVisits",managerServiceImp.calculeSommeOfServicePriceForRoom(allVisitsOfRoomByDate));
            model.addAttribute("sommeServicePriceForDeskVisits",managerServiceImp.calculeSommeOfServicePriceForDesk(allVisitsOfDesdByDate));
            //////////////////////////////////////////////////////
            model.addAttribute("allContractsByDate",allContractsByDate);
            model.addAttribute("totaleMontantOfContracts",totaleMontantOfContractsByDates);
            ////////////////////////////////////////////////////////
            model.addAttribute("sommeServiePriceSupplementaireOfVisits",sommeServiePriceSupplementaireOfVisits);
            model.addAttribute("sommeServiceSuplimentaireOfVisitRoom",sommeServiceSuplimentaireOfVisitRoom);
            model.addAttribute("sommeServiceSupplimentaiePriceOfDisk",sommeServiceSupplimentaiePriceOfDisk);
            //////////////////////////////////////////////////////////
            model.addAttribute("allVisitsOfTeamByDate",allVisitsOfTeamByDate);
            model.addAttribute("sommeSnacksAndBoissonsByVisitForTeam",sommeSnacksAndBoissonsByVisitForTeam);
            model.addAttribute("sommeServiceSupplimentairePriceOfTeam",sommeServiceSupplimentairePriceOfTeam);
            model.addAttribute("sommeSnacksAndBoissonsForVisitsTeam",sommeSnacksAndBoissonsForVisitsTeam);
            model.addAttribute("totaleVisitsForTeams",sommeServiceSupplimentairePriceOfTeam+sommeSnacksAndBoissonsForVisitsTeam);

        }
        return "Manager_espace/turnover";
    }
    /*
     *
     *
     * */

    @GetMapping("charts")
    public String charts(@RequestParam(name = "date_debut") LocalDate dateDebut,
                         @RequestParam(name = "date_fin") LocalDate dateFin, Model model){

        Map<LocalDate, Double> totaleVisitsNormaleCharts = managerService.totaleVisitsNormaleCharts(dateDebut,dateFin);
        Map<LocalDate, Double> totaleVisitsRoomCharts = managerService.totaleVisitsRoomCharts(dateDebut, dateFin);
        Map<LocalDate, Double> totaleVisitsDeskCharts = managerService.totaleVisitOfDesk(dateDebut, dateFin);
        Map<LocalDate, Double> totaleSubscriptionsCharts = managerServiceImp.totaleSubscriptions(dateDebut, dateFin);
        Map<LocalDate,Double> totaleContractsCherts = managerService.totaleContractsCherts(dateDebut,dateFin);
        Map<LocalDate,Double> totaleVisitsTeamsChartByDates = managerService.totaleVisitsTeamChartByDates(dateDebut,dateFin);
        Map<LocalDate, Double> totaleTurnoverForCharts = managerService.totaleTurnoverForCharts(
                new HashMap<>(totaleVisitsNormaleCharts), totaleVisitsRoomCharts, totaleVisitsDeskCharts,totaleVisitsTeamsChartByDates, totaleSubscriptionsCharts,totaleContractsCherts);

        List<String> labelsNormale = totaleVisitsNormaleCharts.keySet().stream()
                .sorted()
                .map(LocalDate::toString)
                .collect(Collectors.toList());
        List<Double> dataNormale = labelsNormale.stream()
                .map(date -> totaleVisitsNormaleCharts.get(LocalDate.parse(date)))
                .collect(Collectors.toList());

        // Créer les labels et les données pour les visites de la salle
        List<String> labelsRoom = totaleVisitsRoomCharts.keySet().stream()
                .sorted()
                .map(LocalDate::toString)
                .collect(Collectors.toList());
        List<Double> dataRoom = labelsRoom.stream()
                .map(date->totaleVisitsRoomCharts.get(LocalDate.parse(date)))
                .collect(Collectors.toList());

        // Créer les labels et les données pour les visites de bureau
        List<String> labelsDesk = totaleVisitsDeskCharts.keySet().stream()
                .sorted()
                .map(LocalDate::toString)
                .collect(Collectors.toList());
        List<Double> dataDesk = labelsDesk.stream()
                .map(date->totaleVisitsDeskCharts.get(LocalDate.parse(date)))
                .collect(Collectors.toList());

        // Créer les labels et les données pour les abonnements
        List<String> labelsSubscriptions = totaleSubscriptionsCharts.keySet().stream()
                .sorted()
                .map(LocalDate::toString)
                .collect(Collectors.toList());
        List<Double> dataSubscriptions = labelsSubscriptions.stream()
                .map(date->totaleSubscriptionsCharts.get(LocalDate.parse(date)))
                .collect(Collectors.toList());

        // Créer les labels et les données pour le chiffre d'affaires total
        List<String> labelsTurnover = totaleTurnoverForCharts.keySet().stream()
                .sorted()
                .map(LocalDate::toString)
                .collect(Collectors.toList());
        List<Double> dataTurnover = labelsTurnover.stream()
                .map(date->totaleTurnoverForCharts.get(LocalDate.parse(date)))
                .collect(Collectors.toList());

        List<String> labelsContracts = totaleContractsCherts.keySet().stream()
                .sorted()
                .map(LocalDate::toString)
                .collect(Collectors.toList());

        List<Double> dataContracts = labelsContracts.stream()
                .map(date->totaleContractsCherts.get(LocalDate.parse(date)))
                .collect(Collectors.toList());

        List<String> labelsTeam = totaleVisitsTeamsChartByDates.keySet().stream()
                .sorted()
                        .map(LocalDate::toString)
                                .collect(Collectors.toList());

        List<Double> dataTeam = labelsTeam.stream()
                .map(date->totaleVisitsTeamsChartByDates.get(LocalDate.parse(date)))
                        .collect(Collectors.toList());

        // Ajouter les labels et les données au modèle
        model.addAttribute("labelsNormale", labelsNormale);
        model.addAttribute("dataNormale", dataNormale);
        model.addAttribute("labelsRoom", labelsRoom);
        model.addAttribute("dataRoom", dataRoom);
        model.addAttribute("labelsDesk", labelsDesk);
        model.addAttribute("dataDesk", dataDesk);
        model.addAttribute("labelsSubscriptions", labelsSubscriptions);
        model.addAttribute("dataSubscriptions", dataSubscriptions);
        model.addAttribute("labelsTurnover", labelsTurnover);
        model.addAttribute("dataTurnover", dataTurnover);
        model.addAttribute("labelsContracts", labelsContracts);
        model.addAttribute("dataContracts", dataContracts);
        model.addAttribute("labelsTeam",labelsTeam);
        model.addAttribute("dataTeam", dataTeam);
        model.addAttribute("totaleTurnoverCherts",managerService.totaleTurnoverCherts(totaleTurnoverForCharts));

        return "Manager_espace/turnover-charts";
    }

    /*
     *
     *
     * */

    //==============================SERVICE CRUD============================
    @GetMapping("list-services")
    String getAllServices(Model model) {
        List<ServiceType> services = serviceTypeRepository.findAll();
        model.addAttribute("services", services);
        return "Manager_espace/services";
    }

    @GetMapping("add-service")
    String addService(Model model) {
        model.addAttribute("service", new ServiceType());
        return "Manager_espace/add-service-form";
    }

    @PostMapping("save-service")
    public String saveService(@ModelAttribute ServiceType serviceType) {
        serviceTypeRepository.save(serviceType);
        return "redirect:/manager/list-services";
    }

    @PostMapping("update-service")
    String updateService(@RequestParam("serviceId") UUID serviceId, Model model) {
        ServiceType serviceType = serviceTypeRepository.findById(serviceId).orElse(null);

        model.addAttribute("service", serviceType);
        return "Manager_espace/update-service-form";
    }

    @PostMapping("save-update-service")
    public String saveUpdateService(@ModelAttribute ServiceType serviceType) {
        serviceTypeRepository.save(serviceType);
        return "redirect:/manager/list-services";
    }

    @PostMapping("delete-service")
    String deleteService(@RequestParam("serviceId") UUID serviceId) {
        serviceTypeRepository.deleteById(serviceId);
        return "redirect:/manager/list-services";
    }

    //==============================SUBSCRIPTION CRUD============================
    @GetMapping("list-subscriptions")
    String getAllSubscriptions(Model model) {
        List<SubscriptionType> subscriptions = subscriptionTypeRepository.findAll();
        model.addAttribute("subscriptions", subscriptions);
        return "Manager_espace/subscriptions";
    }

    @GetMapping("add-subscription")
    String addSubscription(Model model) {
        model.addAttribute("subscription", new SubscriptionType());
        return "Manager_espace/add-subscription-form";
    }

    @PostMapping("save-subscription")
    public String saveService(@ModelAttribute SubscriptionType subscriptionType) {
        subscriptionTypeRepository.save(subscriptionType);
        return "redirect:/manager/list-subscriptions";
    }

    @PostMapping("update-subscription")
    String updateSubscription(@RequestParam("subscriptionId") UUID subscriptionId, Model model) {
        SubscriptionType subscriptionType = subscriptionTypeRepository.findById(subscriptionId).orElse(null);

        model.addAttribute("subscription", subscriptionType);
        return "Manager_espace/update-subscription-form";
    }

    @PostMapping("save-update-subscription")
    public String saveUpdateSubscription(@ModelAttribute SubscriptionType subscriptionType) {
        subscriptionTypeRepository.save(subscriptionType);
        return "redirect:/manager/list-subscriptions";
    }

    @PostMapping("delete-subscription")
    String deleteSubscription(@RequestParam("subscriptionId") UUID subscriptionId) {
        subscriptionTypeRepository.deleteById(subscriptionId);
        return "redirect:/manager/list-subscriptions";
    }

    //==============================SUBSCRIBER CRUD============================
    @GetMapping("list-subscribers")
    String getAllSubscribers(Model model) {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        model.addAttribute("subscribers", subscribers);
        return "Manager_espace/subscribers";
    }

    @GetMapping("add-subscriber")
    String addSubscriber(Model model) {
        model.addAttribute("subscriber", new Subscriber());
        List<SubscriptionType> subscriptionTypes = subscriptionTypeRepository.findAll();
        model.addAttribute("subscriptionTypes", subscriptionTypes);
        return "Manager_espace/add-subscriber-form";
    }

    @PostMapping("save-subscriber")
    public String saveSubscriber(@ModelAttribute Subscriber subscriber, @RequestParam("subscriptionType_id") UUID subscriptionType_id) {
        managerService.saveSubscriber(subscriber, subscriptionType_id);
        CaisseService caisseService = new CaisseService(caisseRepository);
        Caisse caisse = new Caisse();
        SubscriptionType subscriptionType = subscriptionTypeRepository.findById(subscriptionType_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscription type ID: " + subscriptionType_id));

        double pricePerUnit = subscriptionType.getPrice();
        long quantity = subscriber.getQuantity();
        double total = quantity * pricePerUnit;

        ZoneId moroccoZoneId = ZoneId.of("Africa/Casablanca");
        ZonedDateTime moroccoDateTime = ZonedDateTime.now(moroccoZoneId);

        LocalTime localTime = moroccoDateTime.toLocalTime();

        caisse.setDate(moroccoDateTime.toLocalDate());
        caisse.setTime(localTime);
        caisse.setSomme(total);
        caisse.setTotale_caisse(caisseService.calculerTotalCaisse(total, 0));

        caisseRepository.save(caisse);
        return "redirect:/manager/list-subscribers";
    }

    @PostMapping("update-subscriber")
    String updateSubscriber(@RequestParam("subscriberId") UUID subscriberId, Model model) {
        Subscriber subscriber = subscriberRepository.findById(subscriberId).orElse(null);
        List<SubscriptionType> subscriptionTypes = subscriptionTypeRepository.findAll();
        model.addAttribute("subscriptionTypes", subscriptionTypes);
        model.addAttribute("subscriber", subscriber);
        return "Manager_espace/update-subscriber-form";
    }

    @PostMapping("save-update-subscriber")
    public String saveUpdateSubscriber( @ModelAttribute Subscriber subscriber) {
        managerService.saveUpdateSubscriber(subscriber);
        return "redirect:/manager/list-subscribers";
    }

    @PostMapping("delete-subscriber")
    String deleteSubscriber(@RequestParam("subscriberId") UUID subscriberId) {
        subscriberRepository.deleteById(subscriberId);
        return "redirect:/manager/list-subscribers";
    }

    //==============================VISIT CRUD============================

    @GetMapping("visit")
    public String VisitsToday(Model model) {

        List<Visit> visitOfSubscribers = managerService.getVisitsOfSubscribers();

        model.addAttribute("visitOfSubscribers", visitOfSubscribers);

        List<Visit> visitOfNotSubscribers = managerService.getVisitsOfNotSubscribers();

        model.addAttribute("visitOfNotSubscribers", visitOfNotSubscribers);

        List<VisitOfRoom> visitOfRooms = managerService.getVisitsOfRoom();

        model.addAttribute("visitOfRooms", visitOfRooms);

        List<VisitOfDesk> visitOfDesks = managerService.getVisitsOfDesk();

        model.addAttribute("visitOfDesks", visitOfDesks);

        ZonedDateTime nowInMorocco = ZonedDateTime.now(ZoneId.of("Africa/Casablanca"));
        model.addAttribute("nowInMorocco", nowInMorocco);

        System.out.println("nowInMorocco========"+nowInMorocco);

        return "/Manager_espace/visit";

    }

    @PostMapping("generat-qr-code")
    public String generatQrCode(@RequestParam("numbreQR") int numbreQR) {
        NotSubscriber notSubscriber = new NotSubscriber();
        for (int i = 0; i < numbreQR; i++) {
            notSubscriberRepository.save(notSubscriber);
            notSubscriber = new NotSubscriber();
        }
        return "redirect:/manager/list-NotSubscribers";
    }

    //============================CHANGE PASSWORD=================================

    @GetMapping("update-password")
    String UpdatePassword(@RequestParam(name = "message", required = false) String message, Model model) {
        if (message == null) {
            message = "";
        }
        model.addAttribute("message", message);
        return "Manager_espace/update-password-form";
    }

    @PostMapping("new-password")
    String newPassword(@RequestParam("password") String oldPassword, Model model) {
        Manager receptionist = managerService.getProfile();
        if (passwordEncoder.matches(oldPassword, receptionist.getPassword())) {
            // Si le mot de passe est correct, on peut changer le mot de passe
            return "Manager_espace/new-password-form";
        } else {
            // Mot de passe actuel incorrect
            return "redirect:/manager/update-password?message=Mot de passe actuel incorrect";
        }

    }

    @PostMapping("save-new-password")
    String SaveNewPassword(@RequestParam("newPassword") String newPassword, Model model) {
        Manager receptionist = managerService.getProfile();
        receptionist.setPassword(passwordEncoder.encode(newPassword));
        managerRepository.save(receptionist);
        return "Receptionist_espace/password-changed-successfully";

    }

    //==============================RECEPTIONIST CRUD============================
    @GetMapping("list-receptionist")
    String getAllReceptionists(Model model) {
        List<Receptionist> receptionists = receptionistRepository.findAll();
        model.addAttribute("receptionists", receptionists);
        return "Manager_espace/receptionists";
    }

    @GetMapping("add-receptionist")
    String addReceptionist(Model model) {
        model.addAttribute("receptionist", new Receptionist());
        return "Manager_espace/add-receptionist-form";
    }

    //ajouter une methode d'envoyer msg par mail ou wtsp
    @PostMapping("save-receptionist")
    public String saveReceptionist(@ModelAttribute Receptionist receptionist) {
        receptionist.setPassword(passwordEncoder.encode(receptionist.getPassword()));
        receptionistRepository.save(receptionist);
        return "redirect:/manager/list-receptionist";
    }

    @PostMapping("update-receptionist")
    String updateReceptionist(@RequestParam("receptionistId") UUID receptionistId, Model model) {
        Receptionist receptionist = receptionistRepository.findById(receptionistId).orElse(null);
        model.addAttribute("receptionist", receptionist);
        return "Manager_espace/update-receptionist-form";
    }

    @PostMapping("save-update-receptionist")
    public String saveUpdateReceptionist(@ModelAttribute Receptionist receptionist) {
        Receptionist existReceptionist=receptionistRepository.findById(receptionist.getId()).get();
        if (receptionist.getPassword() != null && !receptionist.getPassword().isEmpty()) {
            existReceptionist.setPassword(passwordEncoder.encode(receptionist.getPassword()));
        }
        System.out.println(receptionist.getPassword().isEmpty());
        existReceptionist.setCIN(receptionist.getCIN());
        existReceptionist.setFirst_name(receptionist.getFirst_name());
        existReceptionist.setLast_name(receptionist.getLast_name());
        existReceptionist.setEmail(receptionist.getEmail());
        existReceptionist.setCNSS_number(receptionist.getCNSS_number());
        receptionistRepository.save(existReceptionist);
        return "redirect:/manager/list-receptionist";
    }

    @PostMapping("delete-receptionist")
    String deleteReceptionist(@RequestParam("receptionistId") UUID receptionist) {
        receptionistRepository.deleteById(receptionist);
        return "redirect:/manager/list-receptionist";
    }
    //==============================Devise CRUD============================
    @GetMapping("list-devis")
    String getAllDevis(Model model) {
        List<Devis> devis = devisRepository.findAll();
        model.addAttribute("devis", devis);
        return "Manager_espace/devis";
    }
    @GetMapping("devis-pdf")
    String getDevis(@RequestParam("divisId")UUID divisId ,Model model) {
        Devis devis = devisRepository.findById(divisId).get();
        model.addAttribute("devis", devis);
        return "Manager_espace/new-devis-pdf";
    }

    //==============================Facture CRUD============================
    @GetMapping("list-facture")
    String getAllFactures(Model model) {
        List<Facture> factures = factureRepository.findAll();
        model.addAttribute("factures", factures);
        return "Manager_espace/facture";
    }
    @GetMapping("facture-pdf")
    String getFacture(@RequestParam("factureId")UUID factureId ,Model model) {
        Facture facture = factureRepository.findById(factureId).get();
        model.addAttribute("facture", facture);
        return "Manager_espace/new-facture-pdf";
    }

    //==============================Domiciliation CRUD============================
    @GetMapping("list-contrat")
    String getAllContrats(Model model) {
        List<Contrat> contrats = contratRepository.findAll();
        model.addAttribute("contrats", contrats);
        return "Manager_espace/contrat";
    }
    @GetMapping("contrat-pdf")
    String getContrt(@RequestParam("contratId")UUID contratId ,Model model) {
        Contrat contrat = contratRepository.findById(contratId).get();
        model.addAttribute("contrat", contrat);
        if(contrat.getLangue().equals("fr"))
            return "Manager_espace/new-contrat-fr-pdf";
        return "Manager_espace/new-contrat-ar-pdf";
    }

    @GetMapping("caisse")
    String getCaisse(Model model){
        List<Caisse> caisse = caisseRepository.findTopByOrderByDateTimeDesc();
        //caisse.sort(Comparator.comparing(Caisse::getTime).reversed());
        model.addAttribute("caisse", caisse);
        return "Manager_espace/caisse";
    }

    @GetMapping("delete-reservation-of-desk")
    public String deleteReservationOfDesk(@RequestParam UUID reservation_id){
        visitOfDeskRepository.deleteById(reservation_id);
        return "redirect:/manager/visit";
    }

}


