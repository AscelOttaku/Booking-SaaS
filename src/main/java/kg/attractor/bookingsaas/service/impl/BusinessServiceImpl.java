package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.repository.BusinessRepository;
import kg.attractor.bookingsaas.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {
    private final BusinessRepository businessRepository;

    @Override
    public void isBusinessExistById(Long id) {
        if (!businessRepository.existsById(id))
            throw new NoSuchElementException("Business does not exist");
    }
}
