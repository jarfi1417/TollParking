package com.tollparkingapi.tollparking.exception;

/**
 * Exception class when a car has not bee found
 * @author Jeremy.ARFI
 */
public class CarNotFoundException extends TollParkingException {

    /**
     * Constructor
     * @param message the exception message
     */
    public CarNotFoundException(String message) {
        super(message);
    }
}
