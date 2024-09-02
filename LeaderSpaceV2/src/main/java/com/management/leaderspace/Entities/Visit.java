package com.management.leaderspace.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Visit {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Temporal(TemporalType.DATE)
    private LocalDate day;

    @Temporal(TemporalType.TIME)
    private LocalTime  StartTime;

    @Temporal(TemporalType.TIME)
    private LocalTime EndTime;

    private double service_price=0;

    private String service_suplementaire;

    private double service_suplementaire_price =0;

    private int freeDrinksNumber=0;
    private boolean isOld;

    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private Subscriber subscriber;

    @ManyToOne
    @JoinColumn(name = "not_subscriber_id")
    private NotSubscriber notSubscriber;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    List<SnacksAndBoissonsOfVisit> snacksAndBoissonsOfVisits;

    // getters and setters
    public boolean isOld() {
        return isOld;
    }

    public void setOld(boolean isOld) {
        this.isOld = isOld;
    }

    // Other existing methods...


//    @ManyToOne
//    @JoinColumn(name = "seviceType_id")
//    private ServiceType serviceType;

}
