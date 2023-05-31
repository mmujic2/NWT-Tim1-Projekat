import api from "./api";

class MenuService {
  getActiveRestaurantMenus(restaurantUUID) {
    try {
      return api
        .get("/menu/restaurant-menus/active/" + restaurantUUID)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }
}

export default new MenuService();
