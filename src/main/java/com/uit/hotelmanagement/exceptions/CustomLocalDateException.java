package com.uit.hotelmanagement.exceptions;

public class CustomLocalDateException extends RuntimeException{
    public CustomLocalDateException (String message) {
        super(message);
    } // check in after check out
}
