package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.DayOfWeekEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayOfWeekRepository extends JpaRepository<DayOfWeekEntity, Long> {
}
