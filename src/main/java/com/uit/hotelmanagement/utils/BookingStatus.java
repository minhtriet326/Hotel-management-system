package com.uit.hotelmanagement.utils;

public enum BookingStatus {
    CANCELLED, // -> room AVAILABLE
    CONFIRMED, // -> room RESERVED
    CHECKED_IN, // -> room OCCUPIED
    CHECKED_OUT // -> room DIRTY
}
