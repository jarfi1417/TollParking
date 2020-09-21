package com.tollparkingapi.tollparking.exception;

/**
 * Exception class when the pricing policy is wrong
 * @author Jeremy.ARFI
 */
public class WrongPricingPolicyException extends TollParkingException {

    /**
     * Constructor
     * @param message the exception message
     */
    public WrongPricingPolicyException(String message) {
        super(message);
    }

}
