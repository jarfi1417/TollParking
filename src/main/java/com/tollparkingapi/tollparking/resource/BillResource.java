package com.tollparkingapi.tollparking.resource;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Resource class that represents a toll parking bill
 * @author Jeremy.ARFI
 */
@Data
public class BillResource {

    private ParkingSlotResource billedParkingSlot;
    private double totalPriceToPay;
    private LocalDateTime occupationStartDate;
    private LocalDateTime occupationEndDate;
}
