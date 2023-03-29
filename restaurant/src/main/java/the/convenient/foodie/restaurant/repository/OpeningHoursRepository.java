package the.convenient.foodie.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.model.OpeningHours;

@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHours,Long> {
}
