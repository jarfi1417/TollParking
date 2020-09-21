package com.tollparkingapi.tollparking.resource;

import com.tollparkingapi.tollparking.entity.EngineType;

import lombok.Data;

/**
 * Resource that represents a car
 * @author Jeremy.ARFI
 */
@Data
public class CarResource {

    private String id;
    private EngineType carType;
}
