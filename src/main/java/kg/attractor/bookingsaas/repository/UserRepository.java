package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);

    @Query("select count(u) > 0 from User u where u.phone = :phoneNumber")
    boolean existByPhoneNumber(String phoneNumber);

    Optional<User> findUserByPhone(String phoneNumber);

    User findUserById(Long id);
}
