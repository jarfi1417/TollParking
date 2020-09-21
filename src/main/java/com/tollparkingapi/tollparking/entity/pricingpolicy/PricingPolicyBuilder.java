package com.tollparkingapi.tollparking.entity.pricingpolicy;

/**
 * Interface that represents the pricing policy builder
 * @author Jeremy.ARFI
 */
public interface PricingPolicyBuilder {

    /**
     * Allows to set the price per hour amount
     * @param pricePerHour price per hour
     */
    public void setPricePerHour(double pricePerHour);

    /**
     * Allows to set the fixed fee amount
     * @param fixedFee fixed fee
     */
    public void setFixedFee(double fixedFee);

    /**
     * Allows to get the {@link PricingPolicy}
     * @return the {@link PricingPolicy}
     */
    public PricingPolicy getPricingPolicy();

    /**
     * Allows to get the computed total amount to pay
     * @param nbHoursSpent the number of hours spent in the toll parking
     * @return the computed total amount to pay
     */
    public double getComputedTotalAmountToPay(int nbHoursSpent);
}
