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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE" , discriminatorType = DiscriminatorType.STRING)
public abstract class Utilisateur {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String CIN;

    private String first_name;

    private String last_name;

    @Column(unique = true , nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String password;

    private String CNSS_number;

    private String resetToken;

    @Lob
    @Column(length=100000)
    private byte[] image;

    @Transient
    private String base64Image;

    public String getDiscriminatorValue() {
        DiscriminatorValue annotation = this.getClass().getAnnotation(DiscriminatorValue.class);
        return (annotation != null) ? annotation.value() : null;
    }
}
