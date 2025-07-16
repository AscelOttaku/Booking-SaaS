package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    @Query("SELECT h FROM Holiday h WHERE YEAR(h.date) = :year")
    List<Holiday> findByYear(int year);

    boolean existsByName(String value);

    Optional<Holiday> findByName(String name);
}
