package kg.attractor.bookingsaas.repository;

import jakarta.validation.constraints.NotNull;
import kg.attractor.bookingsaas.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "select * from role where role.role_name ilike :role", nativeQuery = true)
    Role findByRoleName(@NotNull String role);
}
