package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    @Query("SELECT b FROM Business b WHERE b.title LIKE %?1%")
    List<Business> findByNameContaining(String name);

    boolean existsByTitle(String title);
}
