package com.gamezone.exception;

/** Indicates that an input value is invalid. */
public class InvalidDataException extends GameZoneException {
    public InvalidDataException(String message) {
        super(message);
    }
}
