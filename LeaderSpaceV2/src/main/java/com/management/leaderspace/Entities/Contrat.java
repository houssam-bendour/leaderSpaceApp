package com.management.leaderspace.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contrat {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String Business_name;

    private String association_number;

    private String Business_id;

    private String cin;

    private String  montant_de_location;

    private double  montant_de_location_chiffre;

    private String  type_client;

    private String the_person_who_represents_him;

    private String  duree;

    private String  langue;

    private int  duree_par_moi_chifre;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @OneToOne(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "facture_id")
    DomiciliationFacture domiciliationFacture;

}