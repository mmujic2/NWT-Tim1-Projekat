package the.convenient.foodie.menu.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import the.convenient.foodie.menu.dto.MenuDto;
import the.convenient.foodie.menu.model.Menu;
import the.convenient.foodie.menu.repository.custom.MenuRepositoryCustom;

import java.util.List;

public class MenuRepositoryImpl implements MenuRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Menu> getMenusForRestaurant(String restaurantUUID) {
        var hql = "SELECT m from Menu m WHERE m.restaurant_uuid = :restaurantUUID";
        var query = entityManager.createQuery(hql, Menu.class).setParameter("restaurantUUID",restaurantUUID);
        return query.getResultList();
    }

    @Override
    public List<MenuDto> getMenusForRestaurantShort(String restaurantUUID) {
        var hql = "SELECT new the.convenient.foodie.menu.dto.MenuDto(m.id,m.restaurant_uuid,m.name,m.active) from Menu m WHERE m.restaurant_uuid = :restaurantUUID";
        var query = entityManager.createQuery(hql, MenuDto.class).setParameter("restaurantUUID",restaurantUUID);
        return query.getResultList();
    }

    @Override
    public List<Menu> getActiveMenusForRestaurant(String restaurantUUID) {
        var hql = "SELECT m from Menu m WHERE m.restaurant_uuid = :restaurantUUID AND  m.active = TRUE ";
        var query = entityManager.createQuery(hql, Menu.class).setParameter("restaurantUUID",restaurantUUID);
        return query.getResultList();
    }

}
