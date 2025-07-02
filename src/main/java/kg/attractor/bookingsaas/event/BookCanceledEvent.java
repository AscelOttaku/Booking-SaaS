package kg.attractor.bookingsaas.event;

import java.time.LocalDateTime;

public record BookCanceledEvent(Long bookId, String userEmail, LocalDateTime cancelTime) {
}
