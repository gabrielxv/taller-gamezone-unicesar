package com.gamezone.exception;

/** Indicates that a requested resource does not exist. */
public class ResourceNotFoundException extends GameZoneException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
