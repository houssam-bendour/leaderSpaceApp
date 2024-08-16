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
public class VisitOfRoom {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Temporal(TemporalType.DATE)
    private LocalDate day;

    @Temporal(TemporalType.TIME)
    private LocalTime StartTime;

    @Temporal(TemporalType.TIME)
    private LocalTime EndTime;

    private double service_room_price=0;

    private String service_suplementaire;

    private double service_suplementaire_price =0;

    @OneToMany(mappedBy = "visitOfRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipantOfvisitRoom> participant;

    @OneToMany(mappedBy = "visitOfRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    List<SnacksAndBoissonsOfVisit> snacksAndBoissonsOfVisitRoom;


}