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