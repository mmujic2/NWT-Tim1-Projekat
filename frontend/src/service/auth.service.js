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

        return response.data;
      });
  }

  register(req) {
    return api
        .post("/auth/register",body)
        .then(response=> {
            if(response.status == 200) {
                TokenService.setUser(response.data);

                return response.data;
            }
        })
  }

  logout() {
    TokenService.removeUser();
  }



  getCurrentUser() {
    return TokenService.getUser();
  }
}

export default new AuthService();