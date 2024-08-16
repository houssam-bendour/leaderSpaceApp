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
public class Devis {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String Devis_N;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    private String client_name;

    public String client_ID;

    private double Total_HT;

    private double Total_TVA;

    private double Total_TTC;

    private int Price_validity_period;

    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DesignationOfDevis> designations = new ArrayList<>();



}