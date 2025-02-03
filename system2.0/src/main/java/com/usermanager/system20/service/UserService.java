package com.usermanager.system20.service;

import com.usermanager.system20.entity.UserEntity;
import com.usermanager.system20.exceptions.UserAlreadyExistsException;
import com.usermanager.system20.exceptions.UserListEmptyException;
import com.usermanager.system20.exceptions.UserNotFoundException;
import com.usermanager.system20.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserEntity createUser(UserEntity user) throws UserAlreadyExistsException {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with email: " + user.getEmail() + " already exists.");
        }

        return userRepository.save(user);
    }

    public List<UserEntity> listUser() throws UserListEmptyException {

        List<UserEntity> usersList = userRepository.findAll();

        if (usersList.isEmpty()) {
            throw new UserListEmptyException("No users found in the database");
        }

        return usersList;

    }

    public Optional<UserEntity> updateUser(UserEntity user, Long id) throws UserNotFoundException {
        Optional<UserEntity> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found.");
        }

        UserEntity updatedUser = existingUser.get();

        updatedUser.setName(user.getName());
        updatedUser.setAge(user.getAge());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());

        userRepository.save(updatedUser);

        return Optional.of(updatedUser);
    }

    public Optional<UserEntity> deleteUser(Long id) throws UserNotFoundException {
        Optional<UserEntity> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found.");
        }
        userRepository.deleteById(id);

        return existingUser;
    }

}
