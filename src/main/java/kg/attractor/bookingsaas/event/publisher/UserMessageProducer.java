package kg.attractor.bookingsaas.event.publisher;

import kg.attractor.bookingsaas.dto.PasswordToken;
import kg.attractor.bookingsaas.dto.ResetPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class UserMessageProducer {
    @Value("${spring.rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${spring.rabbitmq.routing.key.reset_password}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendPasswordResetLink(ResetPasswordRequest resetPasswordRequest) {
        Assert.notNull(resetPasswordRequest, "PasswordToken cannot be null");
        if (resetPasswordRequest.getEmail() == null || resetPasswordRequest.getEmail().isBlank()
                || resetPasswordRequest.getLink() == null || resetPasswordRequest.getLink().isBlank()) {
            throw new IllegalArgumentException("Email and link must not be null or blank");
        }
        rabbitTemplate.convertAndSend(exchangeName, routingKey, resetPasswordRequest);
    }
}
