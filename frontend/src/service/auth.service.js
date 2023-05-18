import api from "./api";
import TokenService from "./token.service";

class AuthService {
  login(username, password) {
    
    return api
      .post("/auth/login", {
        username,
        password
      })
      .then(response => {
        if (response.status===200) {
          TokenService.setUser(response.data);
        }

        return response;
      });
  }

  register(req) {
    try {
    return api
        .post("/auth/register",req)
        .then(response=> {
            if(response.status == 201) {
                TokenService.setUser(response.data);

            } 

            return response;
        })
    } catch(e) {
      console.log(e)
    }
  }

  logout() {
    try {
      return api
          .post("/auth/logout")
          .then(response=> {
            if(response.status==200)
              TokenService.removeUser();
  
              return response;
          })
      } catch(e) {
        console.log(e)
      }
    
  }



  getCurrentUser() {
    let user = TokenService.getUser();
    if(user!=null)
      return user.user
    return null
  }
}

export default new AuthService();