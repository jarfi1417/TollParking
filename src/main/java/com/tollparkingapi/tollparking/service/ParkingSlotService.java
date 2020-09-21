package com.tollparkingapi.tollparking.service;

import java.util.List;

import com.tollparkingapi.tollparking.entity.EngineType;
import com.tollparkingapi.tollparking.entity.ParkingSlot;
import com.tollparkingapi.tollparking.exception.CarNotFoundException;
import com.tollparkingapi.tollparking.exception.NoParkingSlotAvailableException;

/**
 * Parking Slot Service
 * @author Jeremy.ARFI
 */
public interface ParkingSlotService {

    /**
     * Allows to get a list of all the available {@link ParkingSlot}
     * @return a list of all available {@link ParkingSlot}
     */
    public List<ParkingSlot> getAllAvailableParkingSlots();

    /**
     * Allows to get a list of all the available {@link ParkingSlot} for the
     * given car type :
     * <ul>
     * <li>If the car type is STANDARD then the parking slots available would be
     * STANDARD</li>
     * <li>If the car type is ELECTRIC_20KW then the parking slots available
     * would be STANDARD and ELECTRIC_20KW</li>
     * <li>If the car type is ELECTRIC_50KW then the parking slots available
     * would be STANDARD and ELECTRIC_50KW</li>
     * </ul>
     * @param carType the car {@link EngineType}
     * @return a list of all the available {@link ParkingSlot} for the given car
     * type
     */
    public List<ParkingSlot> getAllAvailableParkingSlotsForGivenCarType(EngineType carType);

    /**
     * Allows to get the specific {@link ParkingSlot} for the given car
     * identifier
     * @param carId the car identifier
     * @return the specific {@link ParkingSlot} for the given car identifier
     * @throws CarNotFoundException if there is no car found
     */
    public ParkingSlot getParkingSlotFromParkedCar(String carId) throws CarNotFoundException;

    /**
     * Allows to reset the {@link ParkingSlot} to the initial values for the
     * given parking slot identifier
     * @param parkingSlotId the parking slot identifier
     * @throws NoParkingSlotAvailableException if the parking slot is not
     * available
     */
    public void resetParkingSlot(Long parkingSlotId) throws NoParkingSlotAvailableException;
}
