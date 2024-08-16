package com.management.leaderspace.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnacksAndBoissonsHistory {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long quantity;

    private double selling_price;

    private double purchase_price;

    private LocalDate purchase_date;

    private LocalTime purchase_time;

    @ManyToOne
    private SnacksAndBoissons snacksAndBoissons;

}