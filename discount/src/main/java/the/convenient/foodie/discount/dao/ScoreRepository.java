package the.convenient.foodie.discount.dao;

import org.springframework.data.repository.CrudRepository;
import the.convenient.foodie.discount.entity.Score;

public interface ScoreRepository extends CrudRepository<Score, Integer> {
}
