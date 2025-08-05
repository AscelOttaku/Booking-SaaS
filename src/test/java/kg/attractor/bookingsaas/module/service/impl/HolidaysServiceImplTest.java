package kg.attractor.bookingsaas.module.service.impl;

import kg.attractor.bookingsaas.dto.HolidayDto;
import kg.attractor.bookingsaas.dto.mapper.HolidayMapperImpl;
import kg.attractor.bookingsaas.models.Holiday;
import kg.attractor.bookingsaas.repository.HolidayRepository;
import kg.attractor.bookingsaas.service.impl.HolidaysServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class HolidaysServiceImplTest {

    @Spy
    private WebClient webClient = WebClient.builder()
            .baseUrl("https://date.nager.at/api/v3/PublicHolidays")
            .build();

    @Mock
    private HolidayRepository holidayRepository;

    @Spy
    private HolidayMapperImpl holidayMapper;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private HolidaysServiceImpl holidaysService;

    @AfterEach
    void checkNoInteractions() {
        Mockito.verifyNoMoreInteractions(holidayRepository, holidayMapper, cacheManager);
    }

    @Test
    void getHolidaysByYearFromDb() {
        // Given
        int year = 2025;

        // When
        List<HolidayDto> holidayDtoList = holidaysService.getHolidaysByYearFromDb(year);

        // Then
        Assertions.assertThat(holidayDtoList)
                .isNotNull()
                .isNotEmpty()
                .allSatisfy(holiday -> {
                    Assertions.assertThat(holiday.getDate()).isNotNull();
                    Assertions.assertThat(holiday.getName()).isNotBlank();
                    Assertions.assertThat(holiday.getCountryCode()).isEqualTo("KZ");
                });

        Mockito.verify(webClient).get();
        Mockito.verify(holidayRepository).findByYear(year);
        Mockito.verify(holidayMapper, Mockito.atLeast(0)).mapToDto(Mockito.any(Holiday.class));
    }

    @Test
    void createHoliday() {
        // Given
        HolidayDto holidayDto = HolidayDto.builder()
                .date(LocalDate.of(2025, 1, 1))
                .localName("New Year (Kyrgyzstan)")
                .name("New Year (Kyrgyzstan)")
                .countryCode("KZ")
                .global(true)
                .build();

        Mockito.when(holidayRepository.save(Mockito.any(Holiday.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // When
        HolidayDto createdHoliday = holidaysService.createHoliday(holidayDto);

        // Then
        Assertions.assertThat(createdHoliday)
                .isNotNull()
                .extracting(HolidayDto::getName, HolidayDto::getCountryCode)
                .containsExactly("New Year (Kyrgyzstan)", "KZ");

        Mockito.verify(holidayRepository).save(Mockito.any(Holiday.class));
        Mockito.verify(holidayMapper).mapToEntity(Mockito.any(HolidayDto.class));
        Mockito.verify(holidayMapper).mapToDto(Mockito.any(Holiday.class));
    }

    @Test
    void updateHoliday() {
        // Given
        HolidayDto holidayDto = HolidayDto.builder()
                .id(1L)
                .date(LocalDate.of(2025, 1, 1))
                .localName("New Year (Kyrgyzstan) 2")
                .name("New Year (Kyrgyzstan)")
                .countryCode("KZ")
                .global(true)
                .build();

        Mockito.when(holidayRepository.save(Mockito.any(Holiday.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // When
        HolidayDto updatedHoliday = holidaysService.updateHoliday(holidayDto);

        // Then
        Assertions.assertThat(updatedHoliday)
                .isNotNull()
                .extracting(HolidayDto::getId, HolidayDto::getName, HolidayDto::getCountryCode)
                .containsExactly(1L, "New Year (Kyrgyzstan)", "KZ");

        Mockito.verify(holidayRepository).save(Mockito.any(Holiday.class));
        Mockito.verify(holidayMapper).mapToEntity(Mockito.any(HolidayDto.class));
        Mockito.verify(holidayMapper).mapToDto(Mockito.any(Holiday.class));
    }

    @Test
    void existsByName() {
        // Given
        String holidayName = "New Year (Kyrgyzstan)";

        Mockito.when(holidayRepository.existsByName(holidayName))
                .thenReturn(true);

        // When
        boolean exists = holidaysService.existsByName(holidayName);

        // Then
        Assertions.assertThat(exists)
                .isTrue();
    }

    @Test
    void deleteHolidayByName() {
        // Given
        String holidayName = "New Year (Kyrgyzstan)";

        Mockito.when(holidayRepository.findByName(holidayName))
                .thenReturn(Optional.of(new Holiday()));
        Mockito.doNothing().when(holidayRepository)
                .delete(Mockito.any(Holiday.class));

        // When
        holidaysService.deleteHolidayByName(holidayName);

        // Then
        Mockito.verify(holidayRepository).findByName(holidayName);
        Mockito.verify(holidayRepository).delete(Mockito.any(Holiday.class));
        Mockito.verify(cacheManager).getCache("holidays");
    }
}