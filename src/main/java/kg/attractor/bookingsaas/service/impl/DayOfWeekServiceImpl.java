package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.DayOfWeekDto;
import kg.attractor.bookingsaas.dto.mapper.DayOfWeekMapper;
import kg.attractor.bookingsaas.repository.DayOfWeekRepository;
import kg.attractor.bookingsaas.service.DayOfWeekService;
import kg.attractor.bookingsaas.service.DayOfWeekValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DayOfWeekServiceImpl implements DayOfWeekService, DayOfWeekValidator {
    private final DayOfWeekRepository dayOfWeekRepository;
    private final DayOfWeekMapper dayOfWeekMapper;

    @Override
    public DayOfWeekDto findDayOfWeekById(Long id) {
        return dayOfWeekRepository.findById(id)
                .map(dayOfWeekMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Day of week not found with id: " + id));
    }

    @Override
    public void checkDayOfWeekExists(Long id) {
        if (!dayOfWeekRepository.existsById(id)) {
            throw new NoSuchElementException("Day of week not found with id: " + id);
        }
    }
}
