package kg.attractor.bookingsaas.dto.mapper;

import kg.attractor.bookingsaas.dto.booked.BookHistoryDto;
import kg.attractor.bookingsaas.models.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookHistoryMapper {
    @Mapping(target = "serviceName", source = "services.serviceName")
    @Mapping(target = "businessName", source = "services.business.title")
    BookHistoryDto toDto(Book book);
}
