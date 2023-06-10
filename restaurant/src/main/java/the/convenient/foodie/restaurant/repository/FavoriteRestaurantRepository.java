package the.convenient.foodie.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.model.FavoriteRestaurant;
import the.convenient.foodie.restaurant.repository.custom.FavoriteRestaurantRepositoryCustom;

@Repository
public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant,Long>, FavoriteRestaurantRepositoryCustom {

    @Query("SELECT COUNT(fr) FROM FavoriteRestaurant  fr WHERE fr.restaurant.uuid=:uuid")
    public Long countNumberOfFavorites(String uuid);
}
