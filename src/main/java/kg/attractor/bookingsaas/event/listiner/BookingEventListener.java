package kg.attractor.bookingsaas.event.listiner;

import jakarta.mail.MessagingException;
import kg.attractor.bookingsaas.event.BookCanceledEvent;
import kg.attractor.bookingsaas.event.BookCreateEvent;
import kg.attractor.bookingsaas.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventListener {
    private final EmailService emailService;

    @RabbitListener(queues = {"${spring.rabbitmq.queue.name.book_creation}"})
    public void handleBookCreatedEvent(BookCreateEvent bookCreateEvent) {
        String link = String.format("https://booking-saas-3.onrender.com/api/booked/info?bookId=%d", bookCreateEvent.bookId());
        try {
            emailService.sendCreatedBookedEmai(bookCreateEvent.userEmail(), link);
        } catch (MessagingException e) {
            log.error("Error sending creation email for book ID: {}, Error info: {}", bookCreateEvent.bookId(), e.getMessage());
        }
    }

    @RabbitListener(queues = {"${spring.rabbitmq.queue.name.book_cancellation}"})
    public void handleBookCanceledEvent(BookCanceledEvent bookCanceledEvent) {
        String link = String.format("https://booking-saas-3.onrender.com/api/booked/info?bookId=%d", bookCanceledEvent.bookId());
        try {
            emailService.sendCanceledBookedEmail(bookCanceledEvent.userEmail(), link);
        } catch (MessagingException e) {
            log.error("Error sending cancellation email for book ID: {}, Error info: {}", bookCanceledEvent.bookId(), e.getMessage());
        }
    }
}
