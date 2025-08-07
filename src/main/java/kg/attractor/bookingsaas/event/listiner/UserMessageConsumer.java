package kg.attractor.bookingsaas.event.listiner;

import jakarta.mail.MessagingException;
import kg.attractor.bookingsaas.dto.ResetPasswordRequest;
import kg.attractor.bookingsaas.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMessageConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = {"${spring.rabbitmq.queue.name.reset_password}"})
    public void handlePasswordResetLinkEvent(ResetPasswordRequest resetPasswordRequest) {
        try {
            emailService.sendPasswordResetEmail(resetPasswordRequest.getEmail(), resetPasswordRequest.getLink());
        } catch (MessagingException e) {
            log.error("Error sending password reset email to {}: {}", resetPasswordRequest.getEmail(), e.getMessage());
        }
    }
}
