package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.Service;
import kg.attractor.bookingsaas.projection.UserBookServiceProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    @Query("select s from Service s " +
            "join s.business b " +
            "where b.title like :businessTitle")
    Page<Service> findAllByBusinessTitle(String businessTitle, Pageable pageable);

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

    @Query(value = "select s.* from services s " +
            "join bussines bs on bs.id = s.bussines_id " +
            "join schedule sc on sc.bussines_service_id = s.id " +
            "join books b on b.schedule_id = sc.id " +
            "where bs.title = :businessTitle " +
            "group by s.id " +
            "order by count(b.id) desc " +
            "limit 1", nativeQuery = true)
    Optional<Service> findMostPopularServiceByBusinessTitle(String businessTitle);

    List<Service> findServicesByBusinessTitle(String businessTitle);
}