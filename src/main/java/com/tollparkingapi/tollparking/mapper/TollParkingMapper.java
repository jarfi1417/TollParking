package com.tollparkingapi.tollparking.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.tollparkingapi.tollparking.entity.Bill;
import com.tollparkingapi.tollparking.entity.Car;
import com.tollparkingapi.tollparking.entity.ParkingSlot;
import com.tollparkingapi.tollparking.entity.pricingpolicy.PricingPolicy;
import com.tollparkingapi.tollparking.form.CarForm;
import com.tollparkingapi.tollparking.resource.BillResource;
import com.tollparkingapi.tollparking.resource.CarResource;
import com.tollparkingapi.tollparking.resource.ParkingSlotResource;
import com.tollparkingapi.tollparking.resource.PricingPolicyResource;

/**
 * Toll parking mapper
 * @author Jeremy.ARFI
 */
@Mapper(componentModel = "spring")
public interface TollParkingMapper {

    /**
     * Allows to map a {@link PricingPolicy} to a {@link PricingPolicyResource}
     * @param pricingPolicy the {@link PricingPolicy} to map
     * @return a new {@link PricingPolicyResource}
     */
    PricingPolicyResource asPricingPolicyResource(PricingPolicy pricingPolicy);

    /**
     * Allows to map a {@link CarForm} to a {@link Car}
     * @param carForm the {@link CarForm} to map
     * @return a new {@link Car}
     */
    Car asCar(CarForm carForm);

    /**
     * Allows to map a {@link Car car} to a {@link CarResource}
     * @param car the {@link Car car} to map
     * @return a new {@link CarResource}
     */
    CarResource asCarResource(Car car);

    /**
     * Allows to map a {@link Bill} to a {@link BillResource}
     * @param bill the {@link Bill} to map
     * @return a new {@link BillResource}
     */
    BillResource asBillResource(Bill bill);

    /**
     * Allows to map a {@link ParkingSlot car} to a {@link ParkingSlotResource}
     * @param parkingSlot the {@link ParkingSlot car} to map
     * @return a new {@link ParkingSlotResource}
     */
    ParkingSlotResource asParkingSlotResource(ParkingSlot parkingSlot);

    /**
     * Allows to map a list of {@link ParkingSlot} to a list of
     * {@link ParkingSlotResource}
     * @param parkingSlots the list of {@link ParkingSlot} to map
     * @return a new list of {@link ParkingSlotResource}
     */
    List<ParkingSlotResource> asParkingSlotResourceList(List<ParkingSlot> parkingSlots);
}
