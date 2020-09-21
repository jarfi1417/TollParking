package com.tollparkingapi.tollparking.exception;

/**
 * Exception class when the engine type is wrong
 * @author Jeremy.ARFI
 */
public class WrongEngineTypeException extends Exception {

    /**
     * Constructor
     * @param message the exception message
     */
    public WrongEngineTypeException(String message) {
        super(message);
    }
}
