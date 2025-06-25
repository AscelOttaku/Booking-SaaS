package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.Business;
import kg.attractor.bookingsaas.projection.UserBusinessServiceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    @Query("""
                select u as user,
                bs as business,
                s as services
                from Book b
                join b.user u
                join b.services s
                join s.business bs
                where bs.id = :businessId
            """)
    List<UserBusinessServiceProjection> getUserBusinessServiceBookByBusinessId(Long businessId);
}
