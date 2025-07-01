package kg.attractor.bookingsaas.dto.mapper.impl;

import kg.attractor.bookingsaas.dto.mapper.OutputUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBookServiceMapper {
    private final ServiceMapper serviceMapper;
    private final OutputUserMapper outputUserMapper;
}
