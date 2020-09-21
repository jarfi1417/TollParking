package com.tollparkingapi.tollparking.resource;

import java.time.LocalDateTime;

import com.tollparkingapi.tollparking.entity.EngineType;

import lombok.Data;

/**
 * Resource class that represents a parking slot
 * @author Jeremy.ARFI
 */
@Data
public class ParkingSlotResource {

    private Long id;
    private EngineType parkingSlotType;
    private boolean isFree;
    private LocalDateTime occupationStartDate;
    private LocalDateTime occupationEndDate;
    private CarResource parkedCar;
}
