package com.gamezone.exception;

/** Indicates that a business rule has been violated. */
public class BusinessRuleException extends GameZoneException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
