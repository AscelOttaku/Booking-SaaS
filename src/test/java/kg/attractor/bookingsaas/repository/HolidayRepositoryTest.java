package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.Holiday;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class HolidayRepositoryTest {

    @Autowired
    private HolidayRepository holidayRepository;

    @BeforeEach
    void setInitial() {
        IntStream.range(0, 3)
                .forEach(i -> {
                    Holiday holiday = new Holiday();
                    holiday.setName("Holiday " + (i + 1));
                    holiday.setDate(LocalDate.parse("2021:03:12", DateTimeFormatter.ofPattern("yyyy:MM:dd")));
                    holiday.setGlobal(true);
                    holiday.setLocalName("Holiday " + (i + 1));
                    holiday.setCountryCode("ZCD");
                    holidayRepository.save(holiday);
                });
    }

    @Test
    void findByYear() {
        // When
        List<Holiday> holidays = holidayRepository.findByYear(2021);

        // Then
        Assertions.assertThat(holidays)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);
        Assertions.assertThat(holidays)
                .extracting(Holiday::getName)
                .containsExactlyInAnyOrder("Holiday 1", "Holiday 2", "Holiday 3");
        Assertions.assertThat(holidays)
                .extracting(holiday -> holiday.getDate().getYear())
                .allMatch(year -> year == 2021);
    }

    @Test
    void existsByName() {
        // When
        boolean isExistByName = holidayRepository.existsByName("Holiday 1");

        // Then
        Assertions.assertThat(isExistByName)
                .isTrue();
    }

    @Test
    void findByName() {
        // When
        Optional<Holiday> holidays = holidayRepository.findByName("Holiday 1");

        // Then
        Assertions.assertThat(holidays)
                .isNotNull()
                .isPresent()
                .get()
                .extracting(Holiday::getName)
                .isEqualTo("Holiday 1");
    }
}