package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {

    Utilisateur findByEmail(String email);
    Utilisateur findByResetToken(String resetToken);
}
