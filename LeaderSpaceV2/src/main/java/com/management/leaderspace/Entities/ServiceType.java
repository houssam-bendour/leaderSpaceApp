package com.management.leaderspace.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceType {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private Long duration; // par heur

    private int Number_of_Free_Drinks;

    private double price;

//    @OneToMany(mappedBy = "serviceType", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Visit> visits;

//    @OneToMany(mappedBy = "serviceType", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<SubscriptionType> Subscriptions;
public double getHT() {
    return this.price/1.2;
}
}
