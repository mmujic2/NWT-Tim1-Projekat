import api from "./api";
import restaurantService from "./restaurant.service";

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
  getAllRestaurantMenus() {
    const restaurantUUID = restaurantService.getCurrentRestaurantUUID();
    try {
      return api
        .get("/menu/restaurant-menus/uuid/" + restaurantUUID)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  deleteMenu(id) {
    try {
      return api.delete("/menu/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }
}

export default new MenuService();
