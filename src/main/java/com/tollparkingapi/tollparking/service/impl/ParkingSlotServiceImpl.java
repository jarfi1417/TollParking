package com.tollparkingapi.tollparking.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tollparkingapi.tollparking.entity.EngineType;
import com.tollparkingapi.tollparking.entity.ParkingSlot;
import com.tollparkingapi.tollparking.entity.TollParking;
import com.tollparkingapi.tollparking.exception.CarNotFoundException;
import com.tollparkingapi.tollparking.exception.NoParkingSlotAvailableException;
import com.tollparkingapi.tollparking.service.ParkingSlotService;

/**
 * Implementation class of ParkingSlotService
 * @author Jeremy.ARFI
 */
@Service
public class ParkingSlotServiceImpl implements ParkingSlotService {

    /**
     * Allows to reset a given {@link ParkingSlot}
     * @param parkingSlot the {@link ParkingSlot} to reset
     */
    private void resetParkingSlot(ParkingSlot parkingSlot) {
        parkingSlot.setFree(true);
        parkingSlot.setOccupationEndDate(null);
        parkingSlot.setOccupationStartDate(null);
        parkingSlot.setParkedCar(null);
    }

    @Override
    public List<ParkingSlot> getAllAvailableParkingSlots() {
        return TollParking.getInstance().getParkingSlots()
                .stream()
                .filter(ParkingSlot::isFree)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParkingSlot> getAllAvailableParkingSlotsForGivenCarType(EngineType carType) {
        return TollParking.getInstance().getParkingSlots()
                .stream()
                .filter(ParkingSlot::isFree)
                .filter(parkingSlot -> parkingSlot.getParkingSlotType() == carType || parkingSlot.getParkingSlotType() == EngineType.STANDARD)
                .collect(Collectors.toList());
    }

    @Override
    public ParkingSlot getParkingSlotFromParkedCar(String carId) throws CarNotFoundException {
        ParkingSlot parkingSlotFromParkedCar = TollParking.getInstance().getParkingSlots()
                .stream()
                .filter(parkingSlot -> !parkingSlot.isFree())
                .filter(parkingSlot -> parkingSlot.getParkedCar().getId().equalsIgnoreCase(carId))
                .findAny().orElse(null);

        if (parkingSlotFromParkedCar == null) {
            throw new CarNotFoundException("The given car identifier has not been found");
        }

        return parkingSlotFromParkedCar;
    }

    @Override
    public void resetParkingSlot(Long parkingSlotId) throws NoParkingSlotAvailableException {
        Optional<ParkingSlot> parkingSlotToReset = TollParking.getInstance().getParkingSlots()
                .stream()
                .filter(parkingSlot -> parkingSlot.getId().equals(parkingSlotId))
                .findAny();

        if (parkingSlotToReset.isPresent()) {
            resetParkingSlot(parkingSlotToReset.get());
        } else {
            throw new NoParkingSlotAvailableException("No parking slot available for reset");
        }
    }

}
