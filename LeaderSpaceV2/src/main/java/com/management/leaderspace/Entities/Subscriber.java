package com.management.leaderspace.Entities;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("SUBSCRIBER")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Subscriber extends Utilisateur{

    @Temporal(TemporalType.DATE)
    private LocalDate date_debut;


    @Temporal(TemporalType.DATE)
    private LocalDate  date_fin;

    private boolean finished=false;

    private long quantity;

    private double price_actuel_d_abonnemet;

    private Long number_of_visits;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private SubscriptionType subscription_type;


    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscriptionHistory> subscription_history;


    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits;

}
