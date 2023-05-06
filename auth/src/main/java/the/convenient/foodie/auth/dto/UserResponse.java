package the.convenient.foodie.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import the.convenient.foodie.auth.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;

    private String uuid;
    private String firstname;
    private String lastname;
    private String email;

    private String username;

    private String address;

    private String mapCoordinates;

    private String phoneNumber;

    private String role;

    public UserResponse(User user) {
        id = user.getId();
        uuid = user.getUuid();
        email = user.getEmail();
        username = user.getUsername();
        firstname = user.getFirstname();
        lastname = user.getLastname();
        address = user.getAddress();
        mapCoordinates = user.getMapCoordinates();
        phoneNumber = user.getPhoneNumber();
        role = user.getRole().name();
    }
}
