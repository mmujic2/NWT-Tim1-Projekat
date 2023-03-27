package the.convenient.foodie.restaurant.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.entity.OpeningHours;

@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHours,Long> {
}
