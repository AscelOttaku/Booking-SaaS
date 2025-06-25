package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.BookDto;
import kg.attractor.bookingsaas.dto.mapper.impl.BookMapper;
import kg.attractor.bookingsaas.repository.BookRepository;
import kg.attractor.bookingsaas.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookDto> findAllBooksByServiceId(Long serviceId) {
        Assert.notNull(serviceId, "serviceId must not be null");
        return bookRepository.findAllBooksByServicesId(serviceId)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findAllBooksByBusinessId(Long businesId) {
        Assert.notNull(businesId, "businesId must not be null");
        return bookRepository.findAllBooksByBusinessId(businesId)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
