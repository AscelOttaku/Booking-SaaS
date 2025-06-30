package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);

    @Query("select count(u) > 0 from User u where u.phone = :phoneNumber")
    boolean existByPhoneNumber(String phoneNumber);

    Optional<User> findUserByPhone(String phoneNumber);

    Optional<User> findUserById(Long id);

    @Query("select u from User u " +
            "join Book b on u.id = b.user.id " +
            "join Schedule s on b.schedule.id = s.id " +
            "join Service sv on s.service.id = sv.id " +
            "join Business bs on sv.business.id = bs.id " +
            "where bs.id = :businessId")
    List<User> findUsersByBusinessId(Long businessId);

    @Query("select u from User u " +
            "join Book b on u.id = b.user.id " +
            "join Schedule s on b.schedule.id = s.id " +
            "join Service sv on s.service.id = sv.id " +
            "join Business bs on sv.business.id = bs.id " +
            "where bs.title = :businessTitle")
    Page<User> findUserByBusinessTitle(String businessTitle, Pageable pageable);

    @Query("select u from User u " +
            "join Book b on u.id = b.user.id " +
            "join Schedule s on b.schedule.id = s.id " +
            "join Service sv on s.service.id = sv.id " +
            "where sv.id = :serviceId")
    Page<User> findUsersByServiceId(Long serviceId, Pageable pageable);
}
