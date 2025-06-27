package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Page<Service> findAllByBusinessId(Long businessId, Pageable pageable);

    @Query("select s from Service s " +
            "join s.business b " +
            "where b.title like :businessTitle")
    Page<Service> findAllByBusinessByTitle(String businessTitle, Pageable pageable);
}
