package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

}
