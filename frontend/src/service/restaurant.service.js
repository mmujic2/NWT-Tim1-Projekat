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

      searchRestaurants(filters) {
        let query="";
        if(filters.name) {
          if(query.length==0) {
            query+="?name=" + filters.name
          } else {
            query+="&name="+ filters.name
          }
          
        }

        if(filters.offeringDiscount) {
          if(query.length==0) {
            query+="?isOfferingDiscount=" + filters.offeringDiscount
          } else {
            query+="&isOfferingDiscount="+ filters.offeringDiscount
          }
          
        }

        if(filters.categoryIds) {
          filters.categoryIds.forEach(cid => {
            if(query.length==0) {
              query+="?categoryIds=" + cid
            } else {
              query+="&categoryIds="+ cid
            }
          })
          
          
        }

        if(query.length==0) {
          query+="?sortBy=" + filters.sortBy + "&ascending=" + filters.ascending
        } else {
          query+="&sortBy="+ filters.sortBy + "&ascending=" + filters.ascending
        }

        try {
          return api
              .get("/restaurant/search" + query)
              .then(response=> {
                return response;
              })
          } catch(e) {
            console.log(e)
          }
        
      }

      getCategories() {
        try {
          return api
              .get("/category/all")
              .then(response=> {
                return response;
              })
          } catch(e) {
            console.log(e)
          }
      }

}

export default new RestaurantService();