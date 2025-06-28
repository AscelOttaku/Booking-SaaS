package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.dto.ScheduleTimeDto;
import kg.attractor.bookingsaas.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("select case when count(s) = 0 then true else false end from Schedule s " +
            "where s.dayOfWeek.id = :dayOfWeekId and s.service.id = :serviceId")
    boolean notExistByDayOfWeekIdAndServiceId(Long dayOfWeekId, Long serviceId);

    @Query("select s.id from Schedule s " +
            "where s.service.id = :serviceId and s.dayOfWeek.id in :dayOfWeekIds")
    List<Long> findDayOfWeekIdsByServiceIdAndDayOfWeekIds(Long serviceId, List<Long> dayOfWeekIds);

    @Query("select s.maxBookingSize from Schedule s " +
            "where s.id = :scheduleId")
    long findMaxBookingSizeByScheduleId(Long scheduleId);

    @Query("select new kg.attractor.bookingsaas.dto.ScheduleTimeDto(s.startTime, s.endTime, s.id) " +
            "from Schedule s where s.id = :id")
    ScheduleTimeDto findScheduleTimeById(Long id);
}
