package com.usermanager.system20.exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String message) {
        super(message == null || message.isEmpty() ? "User not found" : message);
    }
}
