package com.management.leaderspace.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesignationOfDevis {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //    @ManyToOne//(cascade = CascadeType.ALL)
//    @JoinColumn(name = "designation_id")
//    private Designation designation;
    @ManyToOne
    @JoinColumn(name = "subscribtion_id")
    private SubscriptionType subscriptionType;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceType serviceType;

    @ManyToOne
    @JoinColumn(name = "devis_id")
    private Devis devis;

    @ManyToOne
    @JoinColumn(name = "facture_id")
    private Facture facture;
    private int quantity;
}
