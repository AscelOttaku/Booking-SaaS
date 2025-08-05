package kg.attractor.bookingsaas.module.repository;

import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.models.Role;
import kg.attractor.bookingsaas.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setRoleName(RoleEnum.CLIENT);
        roleRepository.save(role);
    }

    @Test
    void findByRoleName() {

        // When
        Optional<Role> role = roleRepository.findByRoleName(RoleEnum.CLIENT.name());

        // Then
        Assertions.assertThat(role)
                .isPresent()
                .get()
                .extracting(Role::getRoleName)
                .isEqualTo(RoleEnum.CLIENT);
    }
}