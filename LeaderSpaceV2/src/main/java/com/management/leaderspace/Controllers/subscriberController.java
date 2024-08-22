package com.management.leaderspace.Controllers;

import com.management.leaderspace.Services.Subscriber.SubscriberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping("/subscriber")
@AllArgsConstructor
@Transactional
public class subscriberController {

    SubscriberService subscriberService;

    @PostMapping("visit")
    public String visit(@RequestParam(name = "date_debut", required = false) LocalDate date_debut,
                        @RequestParam(name = "date_fin", required = false) LocalDate date_fin,
                        Model model) {
        if (date_debut==null || date_fin==null) {
            model.addAttribute("visits", subscriberService.getAllVisits());
        }else {
            model.addAttribute("visits", subscriberService.getVisitsByDate(date_debut,date_fin));
        }


        return "/Subscriber_espace/visit";
    }
}
