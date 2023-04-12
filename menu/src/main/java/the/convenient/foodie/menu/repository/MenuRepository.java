package the.convenient.foodie.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.menu.model.Menu;
import the.convenient.foodie.menu.repository.custom.MenuRepositoryCustom;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom {
}
