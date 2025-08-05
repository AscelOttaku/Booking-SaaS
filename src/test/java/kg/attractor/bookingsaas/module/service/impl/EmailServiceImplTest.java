package kg.attractor.bookingsaas.module.service.impl;

import jakarta.mail.internet.MimeMessage;
import kg.attractor.bookingsaas.service.EmailService;
import kg.attractor.bookingsaas.service.impl.EmailServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {
    private JavaMailSenderImpl mailSender;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("zhanybek20065732@gmail.com");
        mailSender.setPassword("xmqh zdnf kjld qdkh");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        mailSender = Mockito.spy(mailSender);
        emailService = new EmailServiceImpl(mailSender, "zhanybek20065732@gmail.com");
    }

    @Test
    void sendCreatedBookedEmail() {
        // Given
        String email = "zhanybek20065732@gmail.com";
        String link = "Test link";

        // When
        Assertions.assertThatCode(() -> emailService.sendCreatedBookedEmai(email, link))
                .doesNotThrowAnyException();

        // Then
        Mockito.verify(mailSender, Mockito.times(1)).send(Mockito.any(MimeMessage.class));
    }

    @Test
    void sendCanceledBookedEmail() {
        // Given
        String email = "zhanybek20065732@gmail.com";
        String link = "Test link";

        // When
        Assertions.assertThatCode(() -> emailService.sendCanceledBookedEmail(email, link))
                .doesNotThrowAnyException();

        // Then
        Mockito.verify(mailSender, Mockito.times(1)).send(Mockito.any(MimeMessage.class));
    }
}