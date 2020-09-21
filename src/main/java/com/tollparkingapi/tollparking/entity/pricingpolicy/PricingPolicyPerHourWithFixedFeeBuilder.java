package com.tollparkingapi.tollparking.entity.pricingpolicy;

import lombok.Data;

/**
 * Class that represents the pricing policy per hour with fixed fee builder
 * @author Jeremy.ARFI
 */
@Data
public class PricingPolicyPerHourWithFixedFeeBuilder implements PricingPolicyBuilder {

    private PricingPolicy pricingPolicy;

    /**
     * Pricing policy per hour with fixed fee builder constructor
     */
    public PricingPolicyPerHourWithFixedFeeBuilder() {
        this.pricingPolicy = new PricingPolicy();
    }

    @Override
    public void setPricePerHour(double pricePerHour) {
        pricingPolicy.setPricePerHour(pricePerHour);
    }

    @Override
    public void setFixedFee(double fixedFee) {
        pricingPolicy.setFixedFee(fixedFee);
    }

    @Override
    public PricingPolicy getPricingPolicy() {
        return this.pricingPolicy;
    }

    @Override
    public double getComputedTotalAmountToPay(int nbHoursSpent) {
        double totalPrice = (nbHoursSpent * pricingPolicy.getPricePerHour()) + pricingPolicy.getFixedFee();
        return totalPrice;
    }

}
