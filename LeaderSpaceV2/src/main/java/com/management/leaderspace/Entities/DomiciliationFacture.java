package com.management.leaderspace.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DomiciliationFacture {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String Facture_N;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    private String client_name;

    public String client_ID;

    private double Total_HT;

    private double Total_TVA;

    private double Total_TTC;

    private String Total_TTC_to_words;

//    private String Payment_method;

}
