package com.tollparkingapi.tollparking.entity.pricingpolicy;

import lombok.Data;

/**
 * Class that represents the pricing policy per hour builder
 * @author Jeremy.ARFI
 */
@Data
public class PricingPolicyPerHourBuilder implements PricingPolicyBuilder {

    private PricingPolicy pricingPolicy;

    /**
     * Pricing policy per hour builder constructor
     */
    public PricingPolicyPerHourBuilder() {
        this.pricingPolicy = new PricingPolicy();
    }

    @Override
    public void setPricePerHour(double pricePerHour) {
        pricingPolicy.setPricePerHour(pricePerHour);
    }

    @Override
    public void setFixedFee(double fixedFee) {
        pricingPolicy.setFixedFee(0);
    }

    @Override
    public PricingPolicy getPricingPolicy() {
        return this.pricingPolicy;
    }

    @Override
    public double getComputedTotalAmountToPay(int nbHoursSpent) {
        double totalPrice = nbHoursSpent * pricingPolicy.getPricePerHour();
        return totalPrice;
    }

}