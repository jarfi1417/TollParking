package com.tollparkingapi.tollparking.entity;

import lombok.Data;

/**
 * Entity class that represents a car
 * @author Jeremy.ARFI
 */
@Data
public class Car {

    private String id;
    private EngineType carType;
}
