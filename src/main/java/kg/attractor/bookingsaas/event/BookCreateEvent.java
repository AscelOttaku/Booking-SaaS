package kg.attractor.bookingsaas.event;

public record BookCreateEvent(Long bookId, String userEmail) {
}
