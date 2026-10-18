package com.gamezone.exception;

/** Indicates that a persistence operation failed. */
public class PersistenceException extends GameZoneException {
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
