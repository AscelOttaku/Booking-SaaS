package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.Service;
import kg.attractor.bookingsaas.projection.UserBookServiceProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Page<Service> findAllByBusinessId(Long businessId, Pageable pageable);

    @Query("select s from Service s " +
            "join s.business b " +
            "where b.title like :businessTitle")
    Page<Service> findAllByBusinessByTitle(String businessTitle, Pageable pageable);

    Optional<Integer> findServiceDurationById(Long id);

    @Query("select s.durationInMinutes from Service s " +
            "join Schedule sc on sc.service.id = s.id " +
            "where sc.id = :scheduleId")
    Optional<Integer> findServiceDurationByScheduleId(long scheduleId);

    @Query("select s as service, u as user, b as book " +
            "from User u " +
            "join Book b on b.user.id = u.id " +
            "join b.schedule sc " +
            "join sc.service s " +
            "where s.id = :serviceId")
    Page<UserBookServiceProjection> findClientsByServiceId(Long serviceId, Pageable pageable);
}