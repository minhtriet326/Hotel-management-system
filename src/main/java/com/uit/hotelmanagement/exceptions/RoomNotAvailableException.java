package com.uit.hotelmanagement.exceptions;

public class RoomNotAvailableException extends RuntimeException{
    public RoomNotAvailableException(String message) {
        super(message);
    }
}
