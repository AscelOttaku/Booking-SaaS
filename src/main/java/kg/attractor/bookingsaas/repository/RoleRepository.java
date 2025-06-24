package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "select * from role where role.role_name ilike :role", nativeQuery = true)
    Optional<Role> findByRoleName(String role);
}
