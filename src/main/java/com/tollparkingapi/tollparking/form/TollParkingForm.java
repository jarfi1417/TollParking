package com.tollparkingapi.tollparking.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Form that represents a toll parking.
 * @author Jeremy.ARFI
 */
@Data
public class TollParkingForm {

    @Size(min = 0)
    private int nbStandardParkingSlots;
    @Size(min = 0)
    private int nb20KWElectricParkingSlots;
    @Size(min = 0)
    private int nb50KWElectricParkingSlots;

    @NotBlank(message = "the pricing Policy Type is mandatory")
    private String pricingPolicyType;

    private double pricePerHour;
    private double fixedFee;
}
