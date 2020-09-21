package com.tollparkingapi.tollparking.entity;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Entity class that represents a toll parking bill
 * @author Jeremy.ARFI
 */
@Data
public class Bill {

    private ParkingSlot billedParkingSlot;
    private double totalPriceToPay;
    private LocalDateTime occupationStartDate;
    private LocalDateTime occupationEndDate;
}
