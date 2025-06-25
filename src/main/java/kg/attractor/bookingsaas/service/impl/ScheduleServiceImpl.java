package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.ScheduleDto;
import kg.attractor.bookingsaas.dto.mapper.impl.ScheduleMapper;
import kg.attractor.bookingsaas.repository.ScheduleRepository;
import kg.attractor.bookingsaas.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    public ScheduleDto createSchedule(ScheduleDto scheduleDto) {
        var schedule = scheduleMapper.mapToEntity(scheduleDto);
        var savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.mapToDto(savedSchedule);
    }
}
