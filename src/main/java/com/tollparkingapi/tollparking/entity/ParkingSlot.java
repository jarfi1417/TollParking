package com.tollparkingapi.tollparking.entity;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Entity class that represents a parking slot
 * @author Jeremy.ARFI
 */
@Data
public class ParkingSlot {

    private Long id;
    private EngineType parkingSlotType;
    private boolean isFree = true;
    private LocalDateTime occupationStartDate;
    private LocalDateTime occupationEndDate;
    private Car parkedCar;

    /**
     * Specific constructor for {@link ParkingSlot}
     * @param id the parking slot identifier
     * @param parkingSlotType the parking slot engine type
     */
    public ParkingSlot(Long id, EngineType parkingSlotType) {
        this.id = id;
        this.parkingSlotType = parkingSlotType;
    }
}
