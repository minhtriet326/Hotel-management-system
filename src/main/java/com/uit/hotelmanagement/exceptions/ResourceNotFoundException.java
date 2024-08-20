package com.uit.hotelmanagement.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String object, String field, String value) {
        super(String.format("%s can't be found vie %s: %s", object, field, value));
    }
}
