import api from './api'

class RestaurantService {
    getAllRestaurants() {
        try {
          return api
              .get("/restaurant/all")
              .then(response=> {
                return response;
              })
          } catch(e) {
            console.log(e)
          }
        
      }

      getUserFavorites() {
        try {
          return api
              .get("/restaurant/favorites")
              .then(response=> {
                return response;
              })
          } catch(e) {
            console.log(e)
          }
        
      }

}

export default new RestaurantService();