package com.management.leaderspace.Entities;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ADMIN")
@Data

public class Admin extends Utilisateur{
}
