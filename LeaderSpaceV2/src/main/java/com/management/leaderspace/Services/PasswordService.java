package com.management.leaderspace.Services;

public interface PasswordService {
     boolean sendPasswordResetLink(String email);

     boolean validatePasswordResetToken(String token);

     boolean resetPassword(String token, String newPassword);
}
