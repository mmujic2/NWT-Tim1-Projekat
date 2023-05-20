package the.convenient.foodie.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.auth.dao.UserRepository;
import the.convenient.foodie.auth.dto.AuthResponse;
import the.convenient.foodie.auth.dto.RegisterRequest;
import the.convenient.foodie.auth.dto.UserResponse;
import the.convenient.foodie.auth.dto.UserUpdateRequest;
import the.convenient.foodie.auth.entity.User;
import the.convenient.foodie.auth.enums.Role;
import the.convenient.foodie.auth.util.DuplicateEntryException;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;



    private  void checkIfUserUnique(String username, String email) {
        userRepository.findByUsername(username)
                .ifPresent(u-> {throw new DuplicateEntryException("Username " + username + " is already taken.");});
        userRepository.findByEmail(email)
                .ifPresent(u-> {throw new DuplicateEntryException("An account using that email address already exists.");});
    }
}
