package com.management.leaderspace.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionHistory {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    private double price;

    private long quantity;

    @ManyToOne
    private Subscriber subscriber;

    @ManyToOne
    private SubscriptionType subscriptionType;


}
