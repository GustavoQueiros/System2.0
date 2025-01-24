package com.usermanager.system20.service;

import com.usermanager.system20.entity.UserEntity;
import com.usermanager.system20.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);//crete the exception User Already exist
    }

    public List<UserEntity> listUser() {
        return userRepository.findAll();//Exception UserListEmpty
    }

    public Optional<UserEntity> updateUser(UserEntity user, Long id) {
        Optional<UserEntity> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return Optional.empty();
        }                                       //Exception password and email
        UserEntity updatedUser = existingUser.get();

        updatedUser.setName(user.getName());
        updatedUser.setAge(user.getAge());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());

        userRepository.save(updatedUser);

        return Optional.of(updatedUser);
    }

    public Optional<UserEntity> deleteUser(Long id) {
        Optional<UserEntity> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return Optional.empty();
        }
                                        //exception user do not exist
        userRepository.deleteById(id);

        return existingUser;
    }

}
