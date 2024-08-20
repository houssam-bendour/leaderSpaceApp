package com.management.leaderspace.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Caisse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Or handle UUID generation manually
    private UUID id;
    private Double somme;
    private Double totale_caisse;

    private LocalDate date;
    private LocalTime time;


}
