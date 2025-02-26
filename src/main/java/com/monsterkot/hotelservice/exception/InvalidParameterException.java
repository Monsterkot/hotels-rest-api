package com.monsterkot.hotelservice.exception;

public class InvalidParameterException extends IllegalArgumentException {
    public InvalidParameterException(String param) {
        super("Invalid parameter: " + "'" + param  + "'");
    }
}
