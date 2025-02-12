package com.usermanager.system20.dto;


import com.usermanager.system20.entity.UserEntity;

public record UserDTO(String name, Short age, String email) {

    public UserDTO(UserEntity user) {
        this(user.getName(), user.getAge(), user.getEmail());
    }
}
