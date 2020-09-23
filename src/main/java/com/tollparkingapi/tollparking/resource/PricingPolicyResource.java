package com.tollparkingapi.tollparking.resource;

import lombok.Data;

/**
 * Resource that represents a pricing policy
 * @author Jeremy.ARFI
 */
@Data
public class PricingPolicyResource {

    private double pricePerHour;
    private double fixedFee;
}
