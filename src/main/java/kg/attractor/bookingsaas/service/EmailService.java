package kg.attractor.bookingsaas.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendCreatedBookedEmai(String toEmail, String link) throws MessagingException;

    void sendCanceledBookedEmail(String toEmail, String link) throws MessagingException;

    void sendPasswordResetEmail(String email, String resetLink) throws MessagingException;
}
