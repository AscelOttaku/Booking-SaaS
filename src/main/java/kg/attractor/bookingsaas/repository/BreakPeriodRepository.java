package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.BreakPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface BreakPeriodRepository extends JpaRepository<BreakPeriod, Long> {

    @Query("select bp from BreakPeriod bp " +
            "join bp.settings s " +
            "join s.schedule sch " +
            "where sch.id = :scheduleId")
    Optional<BreakPeriod> findByScheduleId(Long scheduleId);

    @Query("select count (bp) > 0 from BreakPeriod bp " +
            "join bp.settings.schedule s " +
            "where s.id = :scheduleId " +
            "and bp.start < :finishedAt " +
            "and bp.end > :startedAt")
    boolean checkForBreakConflicts(Long scheduleId, LocalTime startedAt, LocalTime finishedAt);
}
