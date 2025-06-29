package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.DailyScheduleDto;
import kg.attractor.bookingsaas.dto.ScheduleTimeDto;
import kg.attractor.bookingsaas.dto.WeeklyScheduleDto;
import kg.attractor.bookingsaas.dto.mapper.impl.ScheduleMapper;
import kg.attractor.bookingsaas.models.Schedule;
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
        serviceService.checkServiceBelongsToAuthUser(dailyScheduleDto.getServiceId());

        var result = scheduleRepository.notExistByDayOfWeekIdAndServiceId(dailyScheduleDto.getDayOfWeekId(), dailyScheduleDto.getServiceId());
        if (!result)
            throw new IllegalArgumentException("schedule already exists ");

        Schedule schedule = scheduleMapper.mapToEntity(dailyScheduleDto);
        var savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.mapToDto(savedSchedule);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public WeeklyScheduleDto createWeeklySchedule(WeeklyScheduleDto weeklyScheduleDto) {
        Assert.notNull(weeklyScheduleDto, "weeklyScheduleDto must not be null");
        Assert.notNull(weeklyScheduleDto.getServiceId(), "Service ID must not be null");

        serviceService.checkServiceBelongsToAuthUser(weeklyScheduleDto.getServiceId());
        List<Long> existingDays = scheduleRepository.findDayOfWeekIdsByServiceIdAndDayOfWeekIds(
                weeklyScheduleDto.getServiceId(),
                weeklyScheduleDto.getDailySchedules().stream()
                        .map(DailyScheduleDto::getDayOfWeekId)
                        .collect(Collectors.toList())
        );

        if (!existingDays.isEmpty()) {
            throw new IllegalArgumentException("Schedules for the following days already exist: " + existingDays);
        }

        weeklyScheduleDto.setDailySchedules(weeklyScheduleDto.getDailySchedules()
                .stream()
                .map(schedule -> {
                    schedule.setServiceId(weeklyScheduleDto.getServiceId());
                    var entity = scheduleMapper.mapToEntity(schedule);
                    return scheduleMapper.mapToDto(scheduleRepository.save(entity));
                })
                .toList()
        );

        return weeklyScheduleDto;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public DailyScheduleDto updateDailySchedule(DailyScheduleDto dailyScheduleDto) {
        Assert.notNull(dailyScheduleDto, "dailyScheduleDto must not be null");
        Assert.notNull(dailyScheduleDto.getId(), "ID must not be null");

        serviceService.checkServiceBelongsToAuthUser(dailyScheduleDto.getServiceId());

        var existingSchedule = scheduleRepository.findById(dailyScheduleDto.getId())
                .orElseThrow(() -> new NoSuchElementException("Schedule with ID " + dailyScheduleDto.getId() + " does not exist"));

        scheduleMapper.updateFrom(dailyScheduleDto, existingSchedule);
        return scheduleMapper.mapToDto(existingSchedule);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public WeeklyScheduleDto updateWeeklySchedule(WeeklyScheduleDto weeklyScheduleDto) {
        serviceService.checkServiceBelongsToAuthUser(weeklyScheduleDto.getServiceId());

        weeklyScheduleDto.getDailySchedules().forEach(schedule -> {
            schedule.setServiceId(weeklyScheduleDto.getServiceId());
            serviceService.checkServiceBelongsToAuthUser(schedule.getServiceId());
            var existingSchedule = scheduleRepository.findById(schedule.getId())
                    .orElseThrow(() -> new NoSuchElementException("Schedule with ID " + schedule.getId() + " does not exist"));
            scheduleMapper.updateFrom(schedule, existingSchedule);
        });

        return WeeklyScheduleDto.builder()
                .serviceId(weeklyScheduleDto.getServiceId())
                .dailySchedules(findAllByServiceId(weeklyScheduleDto.getServiceId()))
                .build();
    }

    public List<DailyScheduleDto> findAllByServiceId(Long serviceId) {
        Assert.notNull(serviceId, "serviceId must not be null");

        serviceService.checkServiceBelongsToAuthUser(serviceId);
        return scheduleRepository.findAllByServiceId(serviceId)
                .stream()
                .map(scheduleMapper::mapToDto)
                .collect(Collectors.toList());
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

    @Override
    public ScheduleTimeDto findScheduleTimeById(Long id) {
        Assert.notNull(id, "ID must not be null");

        return scheduleRepository.findScheduleTimeById(id);
    }

    @Override
    public void deleteDailyScheduleById(Long id) {
        Assert.notNull(id, "ID must not be null");

        var schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Schedule with ID " + id + " does not exist"));
        serviceService.checkServiceBelongsToAuthUser(schedule.getService().getId());
        scheduleRepository.deleteById(id);
    }

    @Override
    public int findDurationBetweenBooksByScheduleId(Long scheduleId) {
        Assert.notNull(scheduleId, "scheduleId must not be null");

        if (!scheduleRepository.existsById(scheduleId)) {
            throw new NoSuchElementException("Schedule with ID " + scheduleId + " does not exist");
        }

        return scheduleRepository.findScheduleDurationById(scheduleId);
    }
}
