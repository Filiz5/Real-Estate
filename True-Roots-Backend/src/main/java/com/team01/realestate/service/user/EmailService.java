package com.team01.realestate.service.user;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String resetCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply.trueroots@gmail.com");
        message.setTo(toEmail); // BURADA dinamik olarak gönderiliyor
        message.setSubject("Password Reset Request");
        message.setText("You can reset your password using the following code:\n\n" +
                resetCode +
                "\n\nIf you didn't request this, please ignore this email.");
        mailSender.send(message);
    }

    public void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply.trueroots@gmail.com");
        message.setTo(toEmail); // BURADA dinamik olarak gönderiliyor
        message.setSubject(subject);
        message.setText(text + "\n\nFor more information, please contact us at: " +
                "info@trueroots@gmail.com" +
                "\n\nor" +
                "\n\nvisit our website: http:localhost:3000/contact"
        );
        mailSender.send(message);
    }

    public void sendEmailForPasswordChange(String toEmail, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply.trueroots@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Account Created Successfully");
        message.setText("You created your account with the following email address: " + toEmail + "\n\n" +
                "Please change your password.\n\n" +
                "Password: " + text + "\n\n" +
                "Email change via Url: http://localhost:3000/dashboard/change-password");
        mailSender.send(message);
    }

}

