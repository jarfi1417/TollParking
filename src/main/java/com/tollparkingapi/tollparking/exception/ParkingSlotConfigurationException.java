package com.tollparkingapi.tollparking.exception;

/**
 * Exception class when there is parking slot configuration exception
 * @author Jeremy.ARFI
 */
public class ParkingSlotConfigurationException extends TollParkingException {

    /**
     * Constructor
     * @param message the exception message
     */
    public ParkingSlotConfigurationException(String message) {
        super(message);
    }
}
