package com.usermanager.system20.exceptions;

public class UserListEmptyException extends Exception{

    public UserListEmptyException(String message){
        super(message == null || message.isEmpty() ? "No users found in database." : message);
    }

}
