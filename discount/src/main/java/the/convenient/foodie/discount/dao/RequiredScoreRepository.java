package the.convenient.foodie.discount.dao;

import org.springframework.data.repository.CrudRepository;
import the.convenient.foodie.discount.entity.RequiredScore;

public interface RequiredScoreRepository extends CrudRepository<RequiredScore, Integer> {
}
