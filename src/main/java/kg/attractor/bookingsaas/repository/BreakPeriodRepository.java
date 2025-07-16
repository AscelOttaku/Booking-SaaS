package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.BreakPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreakPeriodRepository extends JpaRepository<BreakPeriod, Long> {
}
