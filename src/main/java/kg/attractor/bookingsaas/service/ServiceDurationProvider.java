package kg.attractor.bookingsaas.service;

public interface ServiceDurationProvider {
    int findServiceDurationByScheduleId(Long serviceId);
}
