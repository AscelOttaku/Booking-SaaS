package kg.attractor.bookingsaas.event.publisher;

import kg.attractor.bookingsaas.event.BookCanceledEvent;
import kg.attractor.bookingsaas.event.BookCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookEventPublisher {
    @Value("${spring.rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${spring.rabbitmq.routing.key.book_creation}")
    private String bookCreationRoutingKey;

    @Value("${spring.rabbitmq.routing.key.book_cancellation}")
    private String bookCancellationRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public void publishBookCreateEvent(BookCreateEvent bookCreateEvent) {
        if (bookCreateEvent == null) {
            throw new IllegalArgumentException("BookCreateEvent cannot be null");
        }
        rabbitTemplate.convertAndSend(exchangeName, bookCreationRoutingKey, bookCreateEvent);
    }

    public void publishBookCancelEvent(BookCanceledEvent bookCanceledEvent) {
        if (bookCanceledEvent == null) {
            throw new IllegalArgumentException("BookCanceledEvent cannot be null");
        }
        rabbitTemplate.convertAndSend(exchangeName, bookCancellationRoutingKey, bookCanceledEvent);
    }
}
