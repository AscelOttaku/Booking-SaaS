package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.dto.booked.BookHistoryDto;
import kg.attractor.bookingsaas.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllBooksByServicesId(Long serviceId);

    @Query("select b from Book b " +
            "join b.services s " +
            "where s.business.id = :businessId")
    List<Book> findAllBooksByBusinessId(Long businessId);

    @Query("select new kg.attractor.bookingsaas.dto.booked.BookHistoryDto(" +
            "b.id, s.serviceName, bs.title, b.startedAt, b.finishedAt" +
            ") from Book b " +
            "join b.services s " +
            "join s.business bs" +
            " where b.user.id = :userId and b.finishedAt is not null and b.finishedAt < now()")
    Page<BookHistoryDto> findAllUsersBookedHistory(Long userId, Pageable pageable);
}
