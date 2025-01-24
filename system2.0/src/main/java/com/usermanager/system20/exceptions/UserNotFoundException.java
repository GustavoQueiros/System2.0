package com.usermanager.system20.exceptions;

public class UserNotFoundException extends Exception{

    public UserNotFoundException() {
        super("User not found.");
    }
}
