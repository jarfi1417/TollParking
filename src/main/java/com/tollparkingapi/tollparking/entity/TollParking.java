package com.tollparkingapi.tollparking.entity;

import java.util.ArrayList;
import java.util.List;

import com.tollparkingapi.tollparking.entity.pricingpolicy.PricingPolicy;
import com.tollparkingapi.tollparking.entity.pricingpolicy.PricingPolicyBuilder;

import lombok.Data;

/**
 * Entity class that represents the toll parking
 * @author Jeremy.ARFI
 */
@Data
public class TollParking {

    private static final TollParking instance = new TollParking();

    private List<ParkingSlot> parkingSlots = new ArrayList<>();

    private PricingPolicyBuilder pricingPolicyBuilder;

    /**
     * Get the toll parking instance
     * @return the toll parking instance
     */
    public static final TollParking getInstance() {
        return instance;
    }

    /**
     * Get the toll parking {@link PricingPolicy}
     * @return the toll parking {@link PricingPolicy}
     */
    public PricingPolicy getPricingPolicy() {
        return this.pricingPolicyBuilder.getPricingPolicy();
    }
}
