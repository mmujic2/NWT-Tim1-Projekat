package the.convenient.foodie.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import the.convenient.foodie.auth.dto.UserResponse;
import the.convenient.foodie.auth.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path="/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/managers")
    public ResponseEntity<List<UserResponse>> getAllManagers() {
        return ResponseEntity.ok(userService.getAllManagers());
    }

    //@PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/couriers")
    public ResponseEntity<List<UserResponse>> getAllCouriers() {
        return ResponseEntity.ok(userService.getAllCouriers());
    }
}
