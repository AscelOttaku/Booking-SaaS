package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.BreakPeriodDto;
import kg.attractor.bookingsaas.dto.mapper.BreakPeriodMapper;
import kg.attractor.bookingsaas.repository.BreakPeriodRepository;
import kg.attractor.bookingsaas.service.BreakPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BreakPeriodServiceImpl implements BreakPeriodService {
    private final BreakPeriodRepository breakPeriodRepository;
    private final BreakPeriodMapper breakPeriodMapper;

    @Override
    public BreakPeriodDto getBreakPeriodById(Long id) {
        return breakPeriodRepository.findById(id)
                .map(breakPeriodMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Break period not found with id: " + id));
    }

    @Override
    public Optional<BreakPeriodDto> findBreakPeriodById(Long id) {
        return breakPeriodRepository.findById(id)
                .map(breakPeriodMapper::toDto);
    }
}
