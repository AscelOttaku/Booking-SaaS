package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllBooksByServicesId(Long serviceId);

    @Query("select b from Book b " +
            "join b.services s " +
            "where s.business.id = :businessId")
    List<Book> findAllBooksByBusinessId(Long businessId);
}
