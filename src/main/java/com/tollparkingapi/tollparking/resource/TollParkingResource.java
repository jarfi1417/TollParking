package com.tollparkingapi.tollparking.resource;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Resource class that represents a toll parking
 * @author Jeremy.ARFI
 */
@Data
public class TollParkingResource {

    private List<ParkingSlotResource> parkingSlots = new ArrayList<>();

    private PricingPolicyResource pricingPolicy;
}
