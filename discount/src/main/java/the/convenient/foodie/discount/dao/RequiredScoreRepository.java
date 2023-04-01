package the.convenient.foodie.discount.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.discount.model.RequiredScore;

@Repository
public interface RequiredScoreRepository extends JpaRepository<RequiredScore, Integer> {
}
