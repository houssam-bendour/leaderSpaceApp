package com.management.leaderspace.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Designation {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;
    private String service_request;
    private double TTC;
//    @OneToMany(mappedBy = "designation", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<DesignationOfDevis> designationOfDevis = new ArrayList<>();

    public Designation(String serviceRequest, double TTC) {
        this.service_request = serviceRequest;
        this.TTC = TTC;
    }
//    public double getTVA() {
//        return ;
//    }
    public double getHT() {
        return this.TTC/1.2;
    }
}
