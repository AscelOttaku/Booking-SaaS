package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.BusinessDto;
import kg.attractor.bookingsaas.dto.ServiceDto;
import kg.attractor.bookingsaas.dto.mapper.OutputUserMapperImpl;
import kg.attractor.bookingsaas.dto.mapper.impl.BusinessMapper;
import kg.attractor.bookingsaas.dto.mapper.impl.ServiceMapper;
import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.models.*;
import kg.attractor.bookingsaas.repository.BusinessRepository;
import kg.attractor.bookingsaas.service.AuthorizedUserService;
import kg.attractor.bookingsaas.service.ServiceValidator;
import kg.attractor.bookingsaas.util.TestEntityFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BusinessServiceImplTest {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private AuthorizedUserService authorizedUserService;

    @Spy
    private BusinessMapper businessMapper = new BusinessMapper(new ServiceMapper(), new OutputUserMapperImpl());

    @Spy
    private ServiceValidator serviceValidator;

    @InjectMocks
    private BusinessServiceImpl businessServiceImpl;

    private User user;
    private Business business;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(RoleEnum.CLIENT);

        user = new User();
        user.setId(1L);
        user.setRole(role);

        business = new Business();
        business.setId(1L);
        business.setTitle("Test Business");
        business.setUser(user);
    }

    @AfterEach
    void checkMocks() {
        Mockito.verifyNoMoreInteractions(businessRepository, authorizedUserService, businessMapper, serviceValidator);
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

    @Test
    void testBusinessCreat() {
        // Given
        BusinessDto business1 = BusinessDto.builder()
                .title("Business One")
                .description("Description for Business One")
                .services(TestEntityFactory.createServiceDtos1And2())
                .build();

        Mockito.when(authorizedUserService.getAuthUser())
                .thenReturn(user);
        Mockito.when(businessRepository.save(Mockito.any(Business.class)))
                .thenAnswer(invocation -> {
                    Business savedBusiness = invocation.getArgument(0);
                    savedBusiness.setId(1L);
                    return savedBusiness;
                });

        // When
        BusinessDto createdBusiness1 = businessServiceImpl.createBusiness(business1);

        // Then
        Assertions.assertThat(createdBusiness1)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", "Business One")
                .hasFieldOrPropertyWithValue("description", "Description for Business One");

        Assertions.assertThat(createdBusiness1.getServices())
                .isNotEmpty()
                .hasSize(2)
                .extracting("serviceName")
                .contains("Service 1", "Service 2");

        Mockito.verify(businessMapper).toEntity(business1);
        Mockito.verify(authorizedUserService).getAuthUser();
        Mockito.verify(businessRepository).save(Mockito.any(Business.class));
        Mockito.verify(businessMapper).toDto(Mockito.any(Business.class));
        Mockito.verify(serviceValidator).validateServices(Mockito.anyList());
    }

    @Test
    void testUpdateBusiness() {
        // Given
        ServiceDto serviceDto1 = ServiceDto.builder()
                .serviceName("Service 1")
                .durationInMinutes(30)
                .price(BigDecimal.valueOf(100.0))
                .build();

        ServiceDto serviceDto3 = ServiceDto.builder()
                .serviceName("Service 3")
                .durationInMinutes(60)
                .price(BigDecimal.valueOf(200.0))
                .build();

        List<ServiceDto> serviceDtos = TestEntityFactory.createServiceDtos1And2();
        serviceDtos.add(serviceDto1);
        serviceDtos.add(serviceDto3);

        BusinessDto businessDto = BusinessDto.builder()
                .id(1L)
                .title("Updated Business")
                .description("Updated Description")
                .services(serviceDtos)
                .build();

        Business business1 = businessMapper.toEntity(businessDto);
        business1.setUser(user);

        Mockito.when(businessRepository.findById(businessDto.getId()))
                .thenReturn(Optional.of(business1));
        Mockito.when(authorizedUserService.getAuthorizedUserId()).thenReturn(user.getId());
        Mockito.when(authorizedUserService.getAuthUser()).thenReturn(user);
        Mockito.when(businessRepository.save(Mockito.any(Business.class)))
                .thenAnswer(invocation -> invocation.<Business>getArgument(0));

        // When
        BusinessDto updatedBusiness = businessServiceImpl.updateBusiness(businessDto);

        // Then
        Assertions.assertThat(updatedBusiness)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", "Updated Business")
                .hasFieldOrPropertyWithValue("description", "Updated Description");

        Assertions.assertThat(updatedBusiness.getServices())
                .isNotEmpty()
                .hasSize(3)
                .extracting("serviceName")
                .contains("Service 1", "Service 2", "Service 3");

        Mockito.verify(businessMapper).toEntity(businessDto);
        Mockito.verify(businessRepository, Mockito.times(2)).findById(businessDto.getId());
        Mockito.verify(businessMapper).updateEntityFromDto(Mockito.any(BusinessDto.class), Mockito.any(Business.class));
        Mockito.verify(authorizedUserService).getAuthorizedUserId();
        Mockito.verify(authorizedUserService).getAuthUser();
        Mockito.verify(serviceValidator).validateServices(Mockito.anyList());
        Mockito.verify(businessRepository).save(Mockito.any(Business.class));
        Mockito.verify(businessMapper).toDto(Mockito.any(Business.class));
    }
}