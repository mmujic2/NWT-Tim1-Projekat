import api from "./api";

class UserService {


  getAllUsers() {
    
    try {
        return api
            .get("/user/all")
            .then(response=> {
              return response;
            })
        } catch(e) {
          console.log(e)
        }
  }

  addManager(manager){
    try{
      return api
            .post("/auth/register-manager",manager)
            .then(response=> {
              return response;
            })
        } catch(e) {
          console.log(e)
        }
  }

  addCourier(courier){
    try{
      return api
            .post("/auth/register-courier",courier)
            .then(response=> {
              return response;
            })
        } catch(e) {
          console.log(e)
        }
  }

  deleteUser(id){
    try{
      return api
            .delete("/user/"+id)
            .then(response=> {
              return response;
            })
        } catch(e) {
          console.log(e)
        }
  }

  deleteUserByUUID(uuid){
    try{
      return api
            .delete("/user/uuid/"+uuid)
            .then(response=> {
              return response;
            })
        } catch(e) {
          console.log(e)
        }
  }

  getAllManagers() {
    
    try {
        return api
            .get("/user/managers")
            .then(response=> {
              return response;
            })
        } catch(e) {
          console.log(e)
        }
  }

  getAllCouriers() {
    
    try {
        return api
            .get("/user/couriers")
            .then(response=> {
              return response;
            })
        } catch(e) {
          console.log(e)
        }
  }

  getDistanceToRestaurant(restaurant) {
    var user = JSON.parse(localStorage.getItem("user")).user;
    if(user == null || user.mapCoordinates == null) return -1;
    var c1 = restaurant.mapCoordinates.split(", ").map(x => parseFloat(x) / 180 * Math.PI);
    var c2 = user.mapCoordinates.split(", ").map(x => parseFloat(x) / 180 * Math.PI);
    var d = Math.acos(Math.sin(c1[0])*Math.sin(c2[0])+Math.cos(c1[0])*Math.cos(c2[0])*Math.cos(c2[1]-c1[1])) * 6371 * 10;
    return (Math.round(d) / 10);
  }
}

export default new UserService();