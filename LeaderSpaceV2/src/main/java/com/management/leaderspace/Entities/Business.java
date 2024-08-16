package com.management.leaderspace.Entities;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("BUSINESS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Business extends Utilisateur{
private String ICE;
private String company_name;
}
