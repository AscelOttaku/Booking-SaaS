package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.dto.booked.BookHistoryDto;
import kg.attractor.bookingsaas.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b " +
            "join b.schedule s " +
            "join s.service sr " +
            "where sr.id = :serviceId")
    Page<Book> findAllBooksByServicesId(Long serviceId, Pageable pageable);

    @Query("select b from Book b " +
            "join b.schedule s " +
            "join s.service sr " +
            "where sr.business.title = :businessTitle")
    Page<Book> findAllBooksByBusinessTitle(String businessTitle, Pageable pageable);

    @Query("select new kg.attractor.bookingsaas.dto.booked.BookHistoryDto(" +
            "b.id, " +
            "new kg.attractor.bookingsaas.dto.user.OutputUserDto(" +
            "u.firstName, u.middleName, u.lastName, u.phone, u.email, u.logo, u.role.roleName" +
            ")," +
            "s.serviceName, bs.title, b.startedAt, b.finishedAt" +
            ") from Book b " +
            "join b.schedule sc " +
            "join b.user u " +
            "join u.role r " +
            "join sc.service s " +
            "join s.business bs " +
            "where b.finishedAt is not null and b.finishedAt < CURRENT_TIMESTAMP")
    Page<BookHistoryDto> findAllUsersBookedHistory(Pageable pageable);

    @Query("select count(b) from Book b " +
            "join b.schedule s " +
            "where b.schedule.id = :scheduleId " +
            "and b.startedAt < :finishedAt " +
            "and b.finishedAt > :startedAt ")
    long findBooksWithConflictTimesByScheduleId(Long scheduleId, LocalDateTime startedAt, LocalDateTime finishedAt);

    @Query("select count (bp) > 0 from BreakPeriod bp " +
            "join bp.settings.schedule s " +
            "where s.id = :scheduleId " +
            "and bp.start < :finishedAt " +
            "and bp.end > :startedAt")
    boolean checkForBreakConflicts(Long scheduleId, LocalTime startedAt, LocalTime finishedAt);

    @Query("select new kg.attractor.bookingsaas.dto.booked.BookHistoryDto(" +
            "b.id, " +
            "new kg.attractor.bookingsaas.dto.user.OutputUserDto(" +
            "u.firstName, u.middleName, u.lastName, u.phone, u.email, u.logo, u.role.roleName" +
            ")," +
            "s.serviceName, bs.title, b.startedAt, b.finishedAt" +
            ") from Book b " +
            "join b.schedule sc " +
            "join b.user u " +
            "join u.role r " +
            "join sc.service s " +
            "join s.business bs " +
            "where u.id = :authUserId and b.finishedAt is not null and b.finishedAt < CURRENT_TIMESTAMP")
    Optional<BookHistoryDto> findUserHistoryById(Long authUserId);
}