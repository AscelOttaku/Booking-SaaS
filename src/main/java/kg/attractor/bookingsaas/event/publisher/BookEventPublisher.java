package kg.attractor.bookingsaas.event.publisher;

import kg.attractor.bookingsaas.event.BookCanceledEvent;
import kg.attractor.bookingsaas.event.BookCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookEventPublisher {
    private final ApplicationEventPublisher publisher;

    @Async
    public void publishBookCreateEvent(BookCreateEvent bookCreateEvent) {
        publisher.publishEvent(bookCreateEvent);
    }

    @Async
    public void publishBookCancelEvent(BookCanceledEvent bookCanceledEvent) {
        publisher.publishEvent(bookCanceledEvent);
    }
}
