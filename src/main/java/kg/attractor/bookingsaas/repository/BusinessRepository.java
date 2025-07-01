package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.Business;
import kg.attractor.bookingsaas.projection.UserBusinessServiceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    @Query("""
                select u as user,
                b as books,
                bs as business,
                s as services
                from Book b
                join b.user u
                join b.schedule sc
                join sc.service s
                join s.business bs
                where bs.id = :businessId
            """)
    List<UserBusinessServiceProjection> getUserBusinessServiceBookByBusinessId(Long businessId);

    @Query("SELECT b FROM Business b WHERE b.title LIKE %:name%")
    List<Business> findByNameContaining(String name);

    boolean existsByTitle(String title);

    Long countBusinessesByUserId(Long authorizedUserId);

    @Query("select u.name from Business b join b.businessUnderCategories u where b.id = :id")
    List<String> getBusinessUnderCategoryNames(@Param("id") Long id);

    Optional<Business> findByTitle(String businessTitle);
}
