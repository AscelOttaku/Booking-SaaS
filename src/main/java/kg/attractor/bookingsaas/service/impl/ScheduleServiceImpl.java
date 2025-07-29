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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
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
    private final BreakPeriodService breakPeriodService;

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
    public int findMaxBookingSizeByScheduleId(Long scheduleId) {
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

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate workDate = LocalDate.parse(date, dateTimeFormatter);

        checkHoliday(workDate);

        List<Book> currentDateBooks = getOccupiedBooksForDate(schedule, workDate);

        LocalTime startTime = schedule.getStartTime();
        LocalTime endTime = schedule.getEndTime();

        List<AvailableSlotForBooking> availableSlots = new ArrayList<>();

        LocalDateTime startWorkTime = LocalDateTime.of(workDate, startTime);
        LocalDateTime endWorkTime = LocalDateTime.of(workDate, endTime);

        // Get the break duration for the schedule
        int durationBetweenBookingsInMinutes = findDurationBetweenBooksByScheduleId(scheduleId);

        // Get the all break periods for the schedule
        var breakPeriodDtos = breakPeriodService.findBreakPeriodByScheduleId(scheduleId);

        // Get available slots based on existing books
        for (Book book : currentDateBooks) {
            LocalDateTime startBook = book.getStartedAt().minusMinutes(durationBetweenBookingsInMinutes);
            LocalDateTime endBook = book.getFinishedAt().plusMinutes(durationBetweenBookingsInMinutes);

            if (startWorkTime.isBefore(startBook)) {
                // Exclude break periods from the available slot
                var availableSlotsForBookingExcludingBreaks = excludeBreakPeriods(
                        startWorkTime, startBook, breakPeriodDtos, workDate
                );
                availableSlots.addAll(availableSlotsForBookingExcludingBreaks);
            }

            if (endBook.isAfter(startWorkTime))
                startWorkTime = endBook;
        }

        if (startWorkTime.isBefore(endWorkTime)) {
            var availableSlotsForBookingExcludingBreaks = excludeBreakPeriods(
                    startWorkTime, endWorkTime, breakPeriodDtos, workDate
            );
            availableSlots.addAll(availableSlotsForBookingExcludingBreaks);
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

    private static List<Book> getOccupiedBooksForDate(Schedule schedule, LocalDate workDate) {
        return schedule.getBooks()
                .stream()
                .filter(book -> book.getStartedAt().toLocalDate().equals(workDate))
                .sorted(Comparator.comparing(Book::getStartedAt))
                .toList();
    }

    private void checkHoliday(LocalDate workDate) {
        boolean isHoliday = holidaysService.getHolidaysByYearFromDb(workDate.getYear())
                .stream()
                .anyMatch(holiday -> holiday.getDate().equals(workDate));

        if (isHoliday) {
            throw new IllegalArgumentException("The date conflicts with a holiday.");
        }
    }

    private List<AvailableSlotForBooking> excludeBreakPeriods(
            LocalDateTime slotStart, LocalDateTime slotEnd,
            List<BreakPeriodDto> breakPeriods,
            LocalDate workDate
    ) {
        LocalDateTime currentStart = slotStart;

        List<AvailableSlotForBooking> availableSlots = new ArrayList<>();

        for (BreakPeriodDto breakPeriodDto : breakPeriods) {
            LocalDateTime breakStart = LocalDateTime.of(workDate, breakPeriodDto.getStart());
            LocalDateTime breakEnd = LocalDateTime.of(workDate, breakPeriodDto.getEnd());

            if (breakStart.isAfter(slotEnd) || breakStart.isEqual(slotEnd))
                break;

            if (currentStart.isBefore(breakStart))
                availableSlots.add(new AvailableSlotForBooking(currentStart, breakStart));

            if (currentStart.isBefore(breakEnd))
                currentStart = breakEnd;
        }

        if (currentStart.isBefore(slotEnd)) {
            availableSlots.add(new AvailableSlotForBooking(currentStart, slotEnd));
        }
        return availableSlots;
    }
}
