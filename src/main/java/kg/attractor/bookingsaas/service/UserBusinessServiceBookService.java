package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.UserBusinessServiceBookDto;

import java.util.List;

public interface UserBusinessServiceBookService {
    List<UserBusinessServiceBookDto> getUserBusinessServiceBook(Long businessId);
}
