package the.convenient.foodie.discount.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.discount.entity.Score;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {
}
