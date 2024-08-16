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
public class ParticipantOfvisitRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private NotSubscriber notSubscriber;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    List<SnacksAndBoissonsOfVisit> snacksAndBoissonsOfVisits;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    VisitOfRoom visitOfRoom;

}
