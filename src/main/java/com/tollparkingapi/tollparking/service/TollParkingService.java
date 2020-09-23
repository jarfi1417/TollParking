package com.tollparkingapi.tollparking.service;

import com.tollparkingapi.tollparking.entity.Bill;
import com.tollparkingapi.tollparking.entity.ParkingSlot;
import com.tollparkingapi.tollparking.entity.TollParking;
import com.tollparkingapi.tollparking.exception.CarNotFoundException;
import com.tollparkingapi.tollparking.exception.NoParkingSlotAvailableException;
import com.tollparkingapi.tollparking.exception.ParkingSlotConfigurationException;
import com.tollparkingapi.tollparking.exception.WrongEngineTypeException;
import com.tollparkingapi.tollparking.exception.WrongPricingPolicyException;
import com.tollparkingapi.tollparking.form.CarForm;
import com.tollparkingapi.tollparking.form.TollParkingForm;

/**
 * Toll parking service
 * @author Jeremy.ARFI
 */
public interface TollParkingService {

    /**
     * Allows to init the tool parking with the given {@link TollParkingForm}
     * @param parkingCreationForm the {@link TollParkingForm} data
     * @return the toll parking data
     * @throws WrongPricingPolicyException if the pricing policy is wrong
     * @throws ParkingSlotConfigurationException if there is a parking slot
     * configuration error
     */
    public TollParking initTollParking(TollParkingForm parkingCreationForm) throws WrongPricingPolicyException, ParkingSlotConfigurationException;

    /**
     * Allows to get the toll parking total size
     * @return the toll parking total size
     */
    public int getTollParkingSize();

    /**
     * Allows to enter the toll parking
     * @param carForm the {@link CarForm} data
     * @return the {@link ParkingSlot} used by the car
     * @throws NoParkingSlotAvailableException if there is not parking slot
     * available
     * @throws WrongEngineTypeException if the engine type is wrong
     */
    public ParkingSlot enterParking(CarForm carForm) throws NoParkingSlotAvailableException, WrongEngineTypeException;

    /**
     * Allows to leave the parking and bill the customer
     * @param carId the car identifier
     * @return a new {@link Bill}
     * @throws CarNotFoundException if the car leaving the parking is not found
     * @throws NoParkingSlotAvailableException if the parking slot is not
     * available
     */
    public Bill leaveParking(String carId) throws CarNotFoundException, NoParkingSlotAvailableException;
}
