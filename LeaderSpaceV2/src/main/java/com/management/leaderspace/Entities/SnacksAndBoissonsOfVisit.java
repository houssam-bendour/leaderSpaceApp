package com.management.leaderspace.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnacksAndBoissonsOfVisit {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "snack_Boisson_id")
    private SnacksAndBoissons snacksAndBoissons;

    @ManyToOne
    @JoinColumn(name = "visit_id")
    private Visit visit;

    private int quantity;

    private double selling_price;

    private double purchase_price;

    @ManyToOne
    @JoinColumn(name = "visit_participant_id")
    private ParticipantOfvisitRoom participant;

    @ManyToOne
    @JoinColumn(name = "visitOfDesk_id")
    private VisitOfDesk visitOfDesk;

    @ManyToOne
    @JoinColumn(name = "visitOfRoom_id")
    private VisitOfRoom visitOfRoom;

    @ManyToOne
    @JoinColumn(name = "visitOfTeam_id")
    private VisitOfTeam visitOfTeam;

}
