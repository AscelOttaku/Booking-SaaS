package kg.attractor.bookingsaas.api;

import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.booked.BookDto;
import kg.attractor.bookingsaas.dto.booked.BookHistoryDto;
import kg.attractor.bookingsaas.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/booked")
@RequiredArgsConstructor
public class BookApi {
    private final BookService bookService;

    @GetMapping("business/{businessId}")
    @ResponseStatus(HttpStatus.OK)
    public List<BookDto> findAllBooksByBusinessId(@PathVariable Long businessId) {
        return bookService.findAllBooksByBusinessId(businessId);
    }

    @GetMapping("services/{serviceId}")
    @ResponseStatus(HttpStatus.OK)
    public List<BookDto> findAllBooksByServiceId(@PathVariable Long serviceId) {
        return bookService.findAllBooksByBusinessId(serviceId);
    }

    @GetMapping("clients/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PageHolder<BookHistoryDto> findAlUsersBookedHistory(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return bookService.findAlUsersBookedHistory(userId, page, size);
    }
}
