package com.employeetax.api.exception;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super("Invalid data: " + message);
    }
}
