package com.example.profilemanager.Exception;

import org.springframework.web.bind.annotation.ResponseStatus;

//Further exception handling will be placed underneath
//In this class
@ResponseStatus
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }

}