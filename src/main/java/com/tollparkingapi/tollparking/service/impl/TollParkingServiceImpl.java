package com.tollparkingapi.tollparking.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tollparkingapi.tollparking.entity.Bill;
import com.tollparkingapi.tollparking.entity.Car;
import com.tollparkingapi.tollparking.entity.EngineType;
import com.tollparkingapi.tollparking.entity.ParkingSlot;
import com.tollparkingapi.tollparking.entity.TollParking;
import com.tollparkingapi.tollparking.entity.pricingpolicy.PricingPolicyBuilder;
import com.tollparkingapi.tollparking.entity.pricingpolicy.PricingPolicyPerHourBuilder;
import com.tollparkingapi.tollparking.entity.pricingpolicy.PricingPolicyPerHourWithFixedFeeBuilder;
import com.tollparkingapi.tollparking.entity.pricingpolicy.PricingPolicyType;
import com.tollparkingapi.tollparking.exception.CarNotFoundException;
import com.tollparkingapi.tollparking.exception.NoParkingSlotAvailableException;
import com.tollparkingapi.tollparking.exception.ParkingSlotConfigurationException;
import com.tollparkingapi.tollparking.exception.WrongEngineTypeException;
import com.tollparkingapi.tollparking.exception.WrongPricingPolicyException;
import com.tollparkingapi.tollparking.form.CarForm;
import com.tollparkingapi.tollparking.form.TollParkingForm;
import com.tollparkingapi.tollparking.mapper.TollParkingMapper;
import com.tollparkingapi.tollparking.service.ParkingSlotService;
import com.tollparkingapi.tollparking.service.TollParkingService;

/**
 * Implementation class of TollParkingServiceImpl
 * @author Jeremy.ARFI
 */
@Service
public class TollParkingServiceImpl implements TollParkingService {

    @Autowired
    private ParkingSlotService parkingSlotService;

    @Autowired
    private TollParkingMapper tollParkingMapper;

    /**
     * Allows to set the pricing policy according to the given data
     * @param pricePerHour the price per hour amount
     * @param fixedFee the fixed fee amount
     * @param pricingPolicyType the pricing policy type
     * @throws WrongPricingPolicyException if the pricing policy type is wrong
     */
    private void setPricingPolicy(double pricePerHour, double fixedFee, String pricingPolicyType) throws WrongPricingPolicyException {
        TollParking tollParking = TollParking.getInstance();
        if (PricingPolicyType.PER_HOUR.name().equals(pricingPolicyType)) {
            tollParking.setPricingPolicyBuilder(new PricingPolicyPerHourBuilder());
            tollParking.getPricingPolicyBuilder().setPricePerHour(pricePerHour);

        } else if (PricingPolicyType.PER_HOUR_WITH_FIXED_FEE.name().equals(pricingPolicyType)) {
            tollParking.setPricingPolicyBuilder(new PricingPolicyPerHourWithFixedFeeBuilder());
            PricingPolicyBuilder pricingPolicyBuilder = tollParking.getPricingPolicyBuilder();
            pricingPolicyBuilder.setPricePerHour(pricePerHour);
            pricingPolicyBuilder.setFixedFee(fixedFee);

        } else {
            throw new WrongPricingPolicyException("The given pricing policy type doesn't exist: " + pricingPolicyType);
        }
    }

    /**
     * Allows to init the toll parking with the given data
     * @param nbStandardParkingSlots number of standard parking slots
     * @param nb20kwElectricParkingSlots number of 20 KW electric parking slots
     * @param nb50kwElectricParkingSlots number of 50 KW electric parking slots
     * @throws ParkingSlotConfigurationException if there is an error during
     * parking slot configuration
     */
    private void initTollParkingSlots(int nbStandardParkingSlots, int nb20kwElectricParkingSlots,
            int nb50kwElectricParkingSlots) throws ParkingSlotConfigurationException {

        if ((nbStandardParkingSlots < 0 || nb20kwElectricParkingSlots < 0 || nb50kwElectricParkingSlots < 0)
                || (nbStandardParkingSlots == 0 && nb20kwElectricParkingSlots == 0 && nb50kwElectricParkingSlots == 0)) {
            throw new ParkingSlotConfigurationException(
                    "An error occured with the given number of standard, 20 KW electric and/or 50 KW electric parking slots");
        }

        List<ParkingSlot> slots = new ArrayList<>();
        for (int i = 1; i <= nbStandardParkingSlots; i++) {
            slots.add(new ParkingSlot(new Long(i), EngineType.STANDARD));
        }
        for (int j = 1; j <= nb20kwElectricParkingSlots; j++) {
            slots.add(new ParkingSlot(new Long(j), EngineType.ELECTRIC_20KW));
        }
        for (int k = 1; k <= nb50kwElectricParkingSlots; k++) {
            slots.add(new ParkingSlot(new Long(k), EngineType.ELECTRIC_50KW));
        }

        TollParking.getInstance().setParkingSlots(slots);
    }

    @Override
    public void initTollParking(TollParkingForm parkingCreationForm) throws WrongPricingPolicyException, ParkingSlotConfigurationException {
        initTollParkingSlots(
                parkingCreationForm.getNbStandardParkingSlots(),
                parkingCreationForm.getNb20KWElectricParkingSlots(),
                parkingCreationForm.getNb50KWElectricParkingSlots());
        setPricingPolicy(parkingCreationForm.getPricePerHour(),
                parkingCreationForm.getFixedFee(),
                parkingCreationForm.getPricingPolicyType());
    }

    @Override
    public int getTollParkingSize() {
        return TollParking.getInstance().getParkingSlots().size();
    }

    @Override
    public ParkingSlot enterParking(CarForm carForm) throws NoParkingSlotAvailableException, WrongEngineTypeException {
        Car car = null;
        try {
            car = tollParkingMapper.asCar(carForm);
        } catch (Exception e) {
            throw new WrongEngineTypeException("The given car type is wrong: " + carForm.getCarType());
        }

        List<ParkingSlot> slots = parkingSlotService.getAllAvailableParkingSlotsForGivenCarType(car.getCarType());

        if (CollectionUtils.isEmpty(slots)) {
            throw new NoParkingSlotAvailableException("There is no parking slots available");
        } else {
            ParkingSlot parkingSlot = slots.get(1);
            parkingSlot.setFree(false);
            parkingSlot.setOccupationStartDate(LocalDateTime.now());
            parkingSlot.setParkedCar(car);
            return parkingSlot;
        }
    }

    @Override
    public Bill leaveParking(String carId) throws CarNotFoundException, NoParkingSlotAvailableException {
        ParkingSlot parkingSlot = parkingSlotService.getParkingSlotFromParkedCar(carId);
        parkingSlot.setOccupationEndDate(LocalDateTime.now());

        int nbHoursSpent = (int) Duration.between(parkingSlot.getOccupationStartDate(), LocalDateTime.now()).toHours();

        double price = TollParking.getInstance().getPricingPolicyBuilder().getComputedTotalAmountToPay(nbHoursSpent);

        Bill bill = new Bill();
        bill.setTotalPriceToPay(price);
        bill.setBilledParkingSlot(parkingSlot);
        bill.setOccupationStartDate(parkingSlot.getOccupationStartDate());
        bill.setOccupationEndDate(parkingSlot.getOccupationEndDate());

        parkingSlotService.resetParkingSlot(parkingSlot.getId());

        return bill;
    }
}
