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
public class SubscriptionType {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private Long duration; //du jours

    private Long flexibility_duration; //par mois

    private long hour_of_day;

    private int Number_of_Free_Drinks_of_day;

    private double price;


//    @ManyToOne
//    @JoinColumn(name = "service_id")
//    private ServiceType serviceType;

    @OneToMany(mappedBy = "subscription_type", cascade = CascadeType.ALL)
    private List<Subscriber> subscribers;

    @OneToMany(mappedBy = "subscriptionType", cascade = CascadeType.ALL)
    private List<SubscriptionHistory> subscription_history;


    public double getHT() {
        return this.price/1.2;
    }

}
