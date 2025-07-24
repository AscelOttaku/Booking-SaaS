package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.*;
import kg.attractor.bookingsaas.dto.mapper.impl.ScheduleMapper;
import kg.attractor.bookingsaas.models.Book;
import kg.attractor.bookingsaas.models.Schedule;
import kg.attractor.bookingsaas.repository.ScheduleRepository;
import kg.attractor.bookingsaas.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService, ScheduleValidator {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final ServiceValidator serviceValidator;
    private final ServiceDurationProvider serviceDurationProvider;
    private final HolidaysService holidaysService;
    private final BreakValidatorService breakValidatorService;

    @Override
    public DailyScheduleDto createDailySchedule(DailyScheduleDto dailyScheduleDto) {
        serviceValidator.checkServiceBelongsToAuthUser(dailyScheduleDto.getServiceId());

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

        serviceValidator.checkServiceBelongsToAuthUser(weeklyScheduleDto.getServiceId());
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

        serviceValidator.checkServiceBelongsToAuthUser(dailyScheduleDto.getServiceId());

        var existingSchedule = scheduleRepository.findById(dailyScheduleDto.getId())
                .orElseThrow(() -> new NoSuchElementException("Schedule with ID " + dailyScheduleDto.getId() + " does not exist"));

        scheduleMapper.updateFrom(dailyScheduleDto, existingSchedule);
        return scheduleMapper.mapToDto(existingSchedule);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public WeeklyScheduleDto updateWeeklySchedule(WeeklyScheduleDto weeklyScheduleDto) {
        serviceValidator.checkServiceBelongsToAuthUser(weeklyScheduleDto.getServiceId());

        weeklyScheduleDto.getDailySchedules().forEach(schedule -> {
            schedule.setServiceId(weeklyScheduleDto.getServiceId());
            serviceValidator.checkServiceBelongsToAuthUser(schedule.getServiceId());
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

        serviceValidator.checkServiceBelongsToAuthUser(serviceId);
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
    public boolean checkForWorkTimeConflicts(Long scheduleId, LocalTime startedAtTime, LocalTime finishedAtTime) {
        Assert.isTrue(scheduleId != null && scheduleId > 0, "Schedule ID must not be null or negative");
        Assert.isTrue(startedAtTime != null && finishedAtTime != null, "Start and finish times must not be null");
        Assert.isTrue(startedAtTime.isBefore(finishedAtTime), "Start time must be before finish time");

        var existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NoSuchElementException("Schedule with ID " + scheduleId + " does not exist"));

        return existingSchedule.getStartTime().isAfter(finishedAtTime) || existingSchedule.getEndTime().isBefore(startedAtTime);
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
        serviceValidator.checkServiceBelongsToAuthUser(schedule.getService().getId());
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

    @Override
    public ScheduleAvailableSlots findScheduleAvailableSlotsForBooking(Long scheduleId, String date) {
        Assert.notNull(scheduleId, "scheduleId must not be null");

        var schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NoSuchElementException("Schedule with ID " + scheduleId + " does not exist"));

        LocalDate workDate = LocalDate.parse(date);

        // Validate for holidays date
        boolean isHoliday = holidaysService.getHolidaysByYearFromDb(workDate.getYear())
                .stream()
                .anyMatch(holiday -> holiday.getDate().equals(workDate));

        if (isHoliday) {
            throw new IllegalArgumentException("The date conflicts with a holiday.");
        }

        // Get books for the current date
        List<Book> currentDateBooks = schedule.getBooks()
                .stream()
                .filter(book -> book.getStartedAt().toLocalDate().equals(workDate))
                .toList();

        LocalTime startTime = schedule.getStartTime();
        LocalTime endTime = schedule.getEndTime();

        List<AvailableSlotForBooking> availableSlots = new ArrayList<>();

        LocalDateTime startWorkTime = LocalDateTime.of(workDate, startTime);
        LocalDateTime endWorkTime = LocalDateTime.of(workDate, endTime);

        // Get the break duration for the schedule
        int breakDuration = findDurationBetweenBooksByScheduleId(scheduleId);

        // Get available slots based on existing books
        for (Book book : currentDateBooks) {
            LocalDateTime startBook = book.getStartedAt();
            LocalDateTime endBook = book.getFinishedAt();

            if (startWorkTime.isBefore(startBook)) {
                boolean isBreakValid = breakValidatorService.isBreakPeriodValid(scheduleId, startWorkTime.toLocalTime(), startBook.toLocalTime());
                if (isBreakValid)
                    availableSlots.add(new AvailableSlotForBooking(startWorkTime, startBook));
            }

            if (endBook.isAfter(startWorkTime))
                startWorkTime = endBook.plusMinutes(breakDuration);
        }

        if (startWorkTime.isBefore(endWorkTime)) {
            boolean isBreakValid = breakValidatorService.isBreakPeriodValid(scheduleId, startWorkTime.toLocalTime(), endWorkTime.toLocalTime());
            if (isBreakValid)
                availableSlots.add(new AvailableSlotForBooking(startWorkTime, endWorkTime));
        }

        // Validate available slots against service duration
        int serviceDuration = serviceDurationProvider.findServiceDurationByScheduleId(scheduleId);
        var validatedAvailableSlotsForBooking = availableSlots.stream()
                .filter(slot ->
                        Duration.between(slot.getStartTime(), slot.getEndTime()).toMinutes() >= serviceDuration)
                .toList();

        return ScheduleAvailableSlots.builder()
                .dailyScheduleDto(scheduleMapper.mapToDto(schedule))
                .availableSlotForBooking(validatedAvailableSlotsForBooking)
                .build();
    }
}
