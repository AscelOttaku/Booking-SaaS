package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.BusinessDto;
import kg.attractor.bookingsaas.dto.mapper.impl.BusinessMapper;
import kg.attractor.bookingsaas.models.*;
import kg.attractor.bookingsaas.repository.BusinessRepository;
import kg.attractor.bookingsaas.service.AuthorizedUserService;
import kg.attractor.bookingsaas.util.TestEntityFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BusinessServiceImplTest {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private AuthorizedUserService authorizedUserService;

    @Mock
    private BusinessMapper businessMapper;

    @InjectMocks
    private BusinessServiceImpl businessServiceImpl;

    private User user;
    private Business business;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        business = new Business();
        business.setId(1L);
        business.setTitle("Test Business");
        business.setUser(user);
    }

    @AfterEach
    void checkMocks() {
        Mockito.verifyNoMoreInteractions(businessRepository, authorizedUserService, businessMapper);
    }

    @Test
    void checkIsBusinessBelongsToAuthUser() {
        // Given
        Long businessId = 1L;

        Mockito.when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));
        Mockito.when(authorizedUserService.getAuthorizedUserId()).thenReturn(user.getId());

        // When
        businessServiceImpl.checkIsBusinessBelongsToAuthUser(businessId);

        // Then
        Mockito.verify(businessRepository).findById(businessId);
        Mockito.verify(authorizedUserService).getAuthorizedUserId();
    }

    @Test
    void checkIfBusinessBelongsToAuthUserThrowsExceptionWhenBusinessNotFound() {
        // Given
        Long businessId = 2L;

        Mockito.when(businessRepository.findById(businessId)).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThatThrownBy(() -> businessServiceImpl.checkIsBusinessBelongsToAuthUser(businessId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Business not found by ID: " + businessId);

        Mockito.verify(businessRepository).findById(businessId);
    }

    @Test
    void testCheckIsBusinessBelongsToAuthUser() {
        // Given
        String businessTitle = business.getTitle();

        Mockito.when(businessRepository.findByTitle(businessTitle)).thenReturn(Optional.of(business));
        Mockito.when(authorizedUserService.getAuthorizedUserId()).thenReturn(user.getId());

        // When
        businessServiceImpl.checkIsBusinessBelongsToAuthUser(businessTitle);

        // Then
        Mockito.verify(businessRepository).findByTitle(businessTitle);
        Mockito.verify(authorizedUserService).getAuthorizedUserId();
    }

    @Test
    void checkIfBusinessExistByTitle() {
        // Given
        String businessTitle = business.getTitle();

        Mockito.when(businessRepository.existsByTitle(businessTitle)).thenReturn(true);

        // When
        businessServiceImpl.checkIfBusinessExistByTitle(businessTitle);

        // Then
        Mockito.verify(businessRepository).existsByTitle(businessTitle);
    }

    @Test
    void checkIfBusinessExistByTitleThrowsExceptionWhenNotFound() {
        // Given
        String businessTitle = "Nonexistent Business";

        Mockito.when(businessRepository.existsByTitle(businessTitle)).thenReturn(false);

        // When & Then
        Assertions.assertThatThrownBy(() -> businessServiceImpl.checkIfBusinessExistByTitle(businessTitle))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Business does not exist by title " + businessTitle);

        Mockito.verify(businessRepository).existsByTitle(businessTitle);
    }

    @Test
    void findMostPopularFiveBusinessesByBusinessTitleContaining() {
        // Given
        User owner1 = TestEntityFactory.createUser(1L, "owner1@example.com");
        User owner2 = TestEntityFactory.createUser(2L, "owner2@example.com");

        Business business1 = TestEntityFactory.createBusiness(1L, owner1, "Business One");
        Business business2 = TestEntityFactory.createBusiness(2L, owner2, "Business Two");

        Service service1 = TestEntityFactory.createService(1L, business1, "Service 1");
        Service service2 = TestEntityFactory.createService(2L, business2, "Service 2");

        Schedule schedule1 = TestEntityFactory.createSchedule(1L, service1);
        Schedule schedule2 = TestEntityFactory.createSchedule(2L, service2);

        User userA = TestEntityFactory.createUser(3L, "userA@example.com");
        User userB = TestEntityFactory.createUser(4L, "userB@example.com");
        User userC = TestEntityFactory.createUser(5L, "userC@example.com");

        Book book1 = TestEntityFactory.createBook(1L, schedule1, userA); // Business 1
        Book book2 = TestEntityFactory.createBook(2L, schedule1, userB); // Business 1
        Book book3 = TestEntityFactory.createBook(3L, schedule1, userC); // Business 1

        Book book4 = TestEntityFactory.createBook(4L, schedule2, userA); // Business 2
        Book book5 = TestEntityFactory.createBook(5L, schedule2, userB); // Business 2

        schedule1.getBooks().addAll(List.of(book1, book2, book3));
        schedule2.getBooks().addAll(List.of(book4, book5));

        service1.getSchedules().add(schedule1);
        service2.getSchedules().add(schedule2);

        business1.getServices().add(service1);
        business2.getServices().add(service2);

        Mockito.when(businessRepository.findByTitleContaining("Business"))
                .thenReturn(List.of(business1, business2));

        Mockito.when(businessMapper.toDto(Mockito.any(Business.class)))
                .thenAnswer(invocation -> {
                    Business business = invocation.getArgument(0);
                    return BusinessDto.builder()
                            .id(business.getId())
                            .title(business.getTitle())
                            .build();
                });

        // When
        List<BusinessDto> popularBusinesses = businessServiceImpl.findMostPopularFiveBusinessesByBusinessTitleContatining("Business");

        // Then
        Assertions.assertThat(popularBusinesses)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        Assertions.assertThat(popularBusinesses.get(0).getTitle()).isEqualTo("Business One");
        Assertions.assertThat(popularBusinesses.get(1).getTitle()).isEqualTo("Business Two");

        Mockito.verify(businessRepository).findByTitleContaining("Business");
        Mockito.verify(businessMapper, Mockito.times(2)).toDto(Mockito.any(Business.class));
    }
}