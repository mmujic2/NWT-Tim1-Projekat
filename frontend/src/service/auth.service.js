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
            if(response.status == 200) {
                TokenService.setUser(response.data);

            } 

            return response;
        })
    } catch(e) {
      console.log(e)
    }
  }

  logout() {
    TokenService.removeUser();
  }



  getCurrentUser() {
    return TokenService.getUser();
  }
}

export default new AuthService();