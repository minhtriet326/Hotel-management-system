package com.uit.hotelmanagement.utils;

import com.uit.hotelmanagement.exceptions.CustomLocalDateException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

public class UtilityMethods {
    public static LocalDate setLocalDate(String inputDate) {
        List<DateTimeFormatter> formatters = new ArrayList<>();
        formatters.add(DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT));
        formatters.add(DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT));
        formatters.add(DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT));
        formatters.add(DateTimeFormatter.ofPattern("uuuu/MM/dd").withResolverStyle(ResolverStyle.STRICT));

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(inputDate, formatter);
            } catch (DateTimeParseException ignored) {

            }
        }

        return LocalDate.parse(inputDate);
        // DateTimeParseException
    }
    public static void check_inIsBeforeCheck_out(String checkInDate, String checkOutDate) {
        if (!setLocalDate(checkInDate).isBefore(setLocalDate(checkOutDate))) {
            throw new CustomLocalDateException("Check-in date must be before check-out date!");
        }
    }
}
