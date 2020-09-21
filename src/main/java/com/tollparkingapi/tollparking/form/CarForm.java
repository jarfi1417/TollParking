package com.tollparkingapi.tollparking.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Form that represents a car
 * @author Jeremy.ARFI
 */
@Data
public class CarForm {

    @NotBlank(message = "the car identifier is mandatory")
    private String id;

    @NotBlank(message = "the car type is mandatory")
    private String carType;
}
