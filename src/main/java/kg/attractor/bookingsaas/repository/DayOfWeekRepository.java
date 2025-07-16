package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayOfWeekRepository extends JpaRepository<DayOfWeek, Long> {
}
