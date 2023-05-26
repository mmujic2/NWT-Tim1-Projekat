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



}

export default new UserService();