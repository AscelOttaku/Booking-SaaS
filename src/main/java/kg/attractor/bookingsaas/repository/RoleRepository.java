package kg.attractor.bookingsaas.repository;

import jakarta.validation.constraints.NotNull;
import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "SELECT * FROM role WHERE role_name::text ILIKE :role", nativeQuery = true)
    Role findByRoleName(@Param("role") String role);

}
