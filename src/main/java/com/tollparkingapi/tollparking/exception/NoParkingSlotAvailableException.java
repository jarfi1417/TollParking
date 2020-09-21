package com.tollparkingapi.tollparking.exception;

/**
 * Exception class when there is no parking slot available
 * @author Jeremy.ARFI
 */
public class NoParkingSlotAvailableException extends TollParkingException {

    /**
     * Constructor
     * @param message the exception message
     */
    public NoParkingSlotAvailableException(String message) {
        super(message);
    }

}
