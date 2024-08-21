package com.management.leaderspace.model;

import com.management.leaderspace.Entities.Caisse;
import com.management.leaderspace.Repositories.CaisseRepository;

import java.util.List;


public class CaisseService {

    private final CaisseRepository caisseRepository;

    public CaisseService(CaisseRepository caisseRepository) {
        this.caisseRepository = caisseRepository;
    }

    public double calculerTotalCaisse(double total, double priceOfVisit) {

        List<Caisse> cs = caisseRepository.findTopByOrderByDateTimeDesc();
        Caisse csFirst = null;

        if (!cs.isEmpty()) {
            csFirst = cs.getFirst();
        }

        if (csFirst != null) {
            return total + priceOfVisit + csFirst.getTotale_caisse();
        } else {
            return total + priceOfVisit;
        }
    }
}

