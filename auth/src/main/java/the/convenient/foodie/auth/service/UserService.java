package the.convenient.foodie.auth.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.auth.dao.UserRepository;
import the.convenient.foodie.auth.dto.UserResponse;
import the.convenient.foodie.auth.entity.User;
import the.convenient.foodie.auth.enums.Role;
import the.convenient.foodie.auth.util.DuplicateEntryException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<UserResponse> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getAllManagers() {
        return userRepository
                .getAllManagers()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getAllCouriers() {
        return userRepository
                .getAllCouriers()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public String deleteUser(Integer id){
        var us = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User with id " + id + " does not exist!"));
        userRepository.deleteById(id);
        return "User with id " + id + " is successfully deleted!";
    }

}
