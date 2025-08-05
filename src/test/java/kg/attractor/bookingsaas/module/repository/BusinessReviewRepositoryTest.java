package kg.attractor.bookingsaas.module.repository;

import kg.attractor.bookingsaas.dto.BusinessReviewDto;
import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.models.*;
import kg.attractor.bookingsaas.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class BusinessReviewRepositoryTest {

    @Autowired
    private BusinessReviewRepository businessReviewRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
    void setInitial() {
        // Create and save Roles
        Role role = new Role();
        role.setRoleName(RoleEnum.CLIENT);
        Role createdRole = roleRepository.save(role);

        // Create and save Users
        List<User> users = IntStream.range(0, 3)
                .mapToObj(i -> {
                    User user = new User();
                    user.setFirstName("User" + i);
                    user.setLastName("Last" + i);
                    user.setMiddleName("Middle" + i);
                    user.setEmail("user_test" + i + "@gmail.com");
                    user.setPhone("000000000" + i);
                    user.setPassword("password");
                    user.setRole(createdRole);
                    user.setBirthday(LocalDate.now().minusYears(20 + i));
                    user.setLogo("logo" + i + ".png");
                    return userRepository.save(user);
                })
                .toList();

        // Create and save City
        City city = new City();
        city.setName("Test City");
        City createdCity = cityRepository.save(city);

        //Create business category
        BusinessCategory businessCategory = new BusinessCategory();
        businessCategory.setName("Test Category");

        // Create and save Business
        List<Business> businesses = IntStream.range(0, 3)
                .mapToObj(i -> {
                    Business business = new Business();
                    business.setUser(users.get(i));
                    business.setTitle("Business " + i);
                    business.setDescription("Description for business " + i);
                    business.setBusinessAddress("Address " + i);
                    business.setBusinessPhone("123-456-789" + i);
                    business.setBusinessEmail("business" + i + "@gmail.com");
                    business.setCity(createdCity);
                    business.setBusinessCategory(businessCategory);
                    return businessRepository.save(business);
                })
                .toList();

        // Create and save BusinessReview
        IntStream.range(0, 3)
                .forEach(i -> {
                    BusinessReview businessReview = new BusinessReview();
                    businessReview.setBusiness(businesses.get(i));
                    businessReview.setUser(users.get(i));
                    businessReview.setReviewText("Review text for business " + i);
                    businessReview.setRating(4.0 + i * 0.5);
                    businessReview = businessReviewRepository.save(businessReview);
                    log.info("Saved BusinessReview name: {} Rating: {}", businessReview.getBusiness().getTitle(), businessReview.getRating());
                });
    }

    @Test
    void findAllReviews() {
        // When
        Page<BusinessReviewDto> reviews = businessReviewRepository.findAllReviews(PageRequest.of(0, 10));
        reviews.forEach(review -> log.info("Business Name: {}, Average Rating: {}", review.getBusinessName(), Math.round(review.getAverageRating() * 10.0) / 10.0));

        // Then
        Assertions.assertThat(reviews).isNotNull();
        Assertions.assertThat(reviews.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(reviews)
                .extracting(BusinessReviewDto::getBusinessName)
                .containsExactlyInAnyOrder("Business 0", "Business 1", "Business 2");
//        Assertions.assertThat(reviews)
//                .extracting(BusinessReviewDto::getAverageRating)
//                .containsExactlyInAnyOrder(4.0, 4.5, 5.0);
    }
}