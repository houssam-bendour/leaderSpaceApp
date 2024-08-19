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
public class SnacksAndBoissons {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private double selling_price;

    private double purchase_price;

    private Long quantity;

    private Integer requiredQuantity = null;

    private String type;

    @Lob
    @Column(length=100000)
    private byte[] image;

    @Transient
    private String base64Image;

    @OneToMany(mappedBy = "snacksAndBoissons", cascade = CascadeType.ALL)
    private List<SnacksAndBoissonsHistory> snacksAndBoissonsHistory;


}
