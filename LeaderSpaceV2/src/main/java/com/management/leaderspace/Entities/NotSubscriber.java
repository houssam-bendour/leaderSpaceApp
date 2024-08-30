package com.management.leaderspace.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotSubscriber {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int cartNumber;

}
