package com.usermanager.system20.controller;


import com.usermanager.system20.entity.UserEntity;
import com.usermanager.system20.exceptions.UserAlreadyExistsException;
import com.usermanager.system20.exceptions.UserListEmptyException;
import com.usermanager.system20.exceptions.UserNotFoundException;
import com.usermanager.system20.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserEntity user) {

        try {
            UserEntity createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error ocurred.");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserEntity> users = userService.listUser();
            return ResponseEntity.ok(users);
        } catch (UserListEmptyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error ocurred.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity user, @PathVariable Long id) {
        try {
            Optional<UserEntity> updatedUser = userService.updateUser(user, id);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error ocurred.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            Optional<UserEntity> deletedUser = userService.deleteUser(id);
            return ResponseEntity.ok(deletedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error ocurred.");
        }
    }

}
