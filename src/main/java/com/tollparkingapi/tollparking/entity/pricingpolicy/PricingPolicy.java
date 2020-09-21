package com.tollparkingapi.tollparking.entity.pricingpolicy;

import lombok.Data;

/**
 * Entity that represents a pricing policy
 * @author Jeremy.ARFI
 */
@Data
public class PricingPolicy {

    private double pricePerHour;
    private double fixedFee;
}
