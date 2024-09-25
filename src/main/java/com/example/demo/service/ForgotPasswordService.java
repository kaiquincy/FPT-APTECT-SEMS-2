package com.example.demo.service;

import com.example.demo.entity.PasswordResetToken;
import com.example.demo.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    public String createPasswordResetToken(String email) {
        if (!userService.emailExists(email)) {
            throw new IllegalArgumentException("Email does not exist!");
        }

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); 
        tokenRepository.save(resetToken);

        return token;
    }

    public String generateVerificationCode() {
        return String.valueOf((int)(Math.random() * 90000) + 10000);
    }

    public void sendVerificationCode(String email, String code) {
        emailService.sendEmail(email, "Your Verification Code", "Your verification code is: " + code);
    }

    public boolean isVerificationCodeValid(String email, String code) {
        Optional<PasswordResetToken> verificationToken = tokenRepository.findByToken(code);
        if (verificationToken.isPresent()) {
            PasswordResetToken token = verificationToken.get();
            return token.getEmail().equals(email) && 
                   token.getExpiryDate().isAfter(LocalDateTime.now());
        }
        return false;
    }

    public void resetPassword(String email, String code, String newPassword) {
        if (!isVerificationCodeValid(email, code)) {
            throw new IllegalArgumentException("Invalid or expired verification code!");
        }

        userService.updatePassword(email, newPassword);
    }
}
