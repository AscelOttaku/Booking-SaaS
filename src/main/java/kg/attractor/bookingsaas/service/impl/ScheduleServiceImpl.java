package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.DailyScheduleDto;
import kg.attractor.bookingsaas.dto.WeeklyScheduleDto;
import kg.attractor.bookingsaas.dto.mapper.impl.ScheduleMapper;
import kg.attractor.bookingsaas.repository.ScheduleRepository;
import kg.attractor.bookingsaas.service.ScheduleService;
import kg.attractor.bookingsaas.service.ScheduleValidator;
import kg.attractor.bookingsaas.service.ServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService, ScheduleValidator {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final ServiceValidator serviceService;

    @Override
    public DailyScheduleDto createDailySchedule(DailyScheduleDto dailyScheduleDto) {
        serviceService.checkIfServiceExistsById(dailyScheduleDto.getServiceId());
        var schedule = scheduleMapper.mapToEntity(dailyScheduleDto);

        var result = scheduleRepository.existByDayOfWeekIdAndServiceId(dailyScheduleDto.getDayOfWeekId(), dailyScheduleDto.getServiceId());
        if (result)
            throw new IllegalArgumentException("schedule already exists ");

        var savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.mapToDto(savedSchedule);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public WeeklyScheduleDto createWeeklySchedule(WeeklyScheduleDto weeklyScheduleDto) {
        Assert.notNull(weeklyScheduleDto, "weeklyScheduleDto must not be null");
        Assert.notNull(weeklyScheduleDto.getServiceId(), "Service ID must not be null");

        serviceService.checkIfServiceExistsById(weeklyScheduleDto.getServiceId());
        List<Long> existingDays = scheduleRepository.findDayOfWeekIdsByServiceIdAndDayOfWeekIds(
                weeklyScheduleDto.getServiceId(),
                weeklyScheduleDto.getDailySchedules().stream()
                        .map(DailyScheduleDto::getDayOfWeekId)
                        .collect(Collectors.toList())
        );

        if (!existingDays.isEmpty()) {
            throw new IllegalArgumentException("Schedules for the following days already exist: " + existingDays);
        }

        weeklyScheduleDto.getDailySchedules()
                .forEach(schedule -> {
                    schedule.setServiceId(weeklyScheduleDto.getServiceId());
                    var entity = scheduleMapper.mapToEntity(schedule);
                    scheduleRepository.save(entity);
                });
        return weeklyScheduleDto;
    }

    @Override
    public Long findMaxBookingSizeByScheduleId(Long scheduleId) {
        return scheduleRepository.findMaxBookingSizeByScheduleId(scheduleId);
    }

    @Override
    public void checkScheduleExistsById(Long scheduleId) {
        Assert.notNull(scheduleId, "scheduleId must not be null");

        if (!scheduleRepository.existsById(scheduleId))
            throw new NoSuchElementException("Schedule with ID " + scheduleId + " does not exist");
    }
}
