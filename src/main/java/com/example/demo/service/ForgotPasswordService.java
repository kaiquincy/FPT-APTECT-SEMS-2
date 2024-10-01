package com.example.demo.service;

import com.example.demo.entity.PasswordResetToken;
import com.example.demo.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Optional;


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

        String verificationCode = generateVerificationCode();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setVerificationCode(verificationCode);
        resetToken.setEmail(email);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        tokenRepository.save(resetToken);

        sendVerificationCode(email, verificationCode);

        return verificationCode;
    }

    public String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 90000) + 10000);
    }

    public void sendVerificationCode(String email, String code) {
        emailService.sendEmail(email, "Your Verification Code", "Your verification code is: " + code);
    }
    @Transactional
    public void resetPassword(String email, String code, String newPassword) {
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByEmailAndVerificationCode(email, code);

        if (optionalToken.isEmpty()) {
            System.out.println("Invalid verification code for email: " + email + " with code: " + code);
            throw new IllegalArgumentException("Invalid verification code!");
        }


        userService.updatePassword(email, newPassword);

        tokenRepository.deleteByEmail(email);
    }

}
