package com.gamezone.exception;

/** Root exception for GameZone application errors. */
public class GameZoneException extends Exception {
    public GameZoneException(String message) {
        super(message);
    }

    public GameZoneException(String message, Throwable cause) {
        super(message, cause);
    }
}
