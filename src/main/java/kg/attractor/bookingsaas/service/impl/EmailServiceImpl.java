package kg.attractor.bookingsaas.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kg.attractor.bookingsaas.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final String emailFrom;

    public EmailServiceImpl(JavaMailSender mailSender, @Value("${spring.mail.username}") String emailFrom) {
        this.mailSender = mailSender;
        this.emailFrom = emailFrom;
    }

    @Override
    public void sendCreatedBookedEmai(String toEmail, String link) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailFrom);
        helper.setTo(toEmail);

        String subject = "Congratulations! Your booking is confirmed";
        String content = "<p>Hello,</p>"
                + "<p>Congratulations! Your booking has been successfully created.</p>"
                + "<p>You can view your booking details using the link below:</p>"
                + "<p><a href=\"" + link + "\">View my booking</a></p>"
                + "<br>"
                + "<p>If you have any questions, feel free to contact us.</p>"
                + "<p>Thank you for choosing our service!</p>";

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public void sendCanceledBookedEmail(String toEmail, String link) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailFrom);
        helper.setTo(toEmail);

        String subject = "Your booking has been canceled";
        String content = "<p>Hello,</p>"
                + "<p>We regret to inform you that your booking has been canceled.</p>"
                + "<p>You can view the details of the cancellation using the link below:</p>"
                + "<p><a href=\"" + link + "\">View cancellation details</a></p>"
                + "<br>"
                + "<p>If you have any questions, feel free to contact us.</p>"
                + "<p>Thank you for your understanding!</p>";

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String link) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailFrom);
        helper.setTo(toEmail);

        String subject = "Here is the link to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
