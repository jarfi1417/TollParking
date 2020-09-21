package com.tollparkingapi.tollparking.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import com.tollparkingapi.tollparking.config.TollParkingTestConfiguration;
import com.tollparkingapi.tollparking.entity.Car;
import com.tollparkingapi.tollparking.entity.EngineType;
import com.tollparkingapi.tollparking.entity.ParkingSlot;
import com.tollparkingapi.tollparking.entity.TollParking;
import com.tollparkingapi.tollparking.exception.NoParkingSlotAvailableException;
import com.tollparkingapi.tollparking.mapper.TollParkingMapper;
import com.tollparkingapi.tollparking.service.ParkingSlotService;

/**
 * Class to test the parking slot services
 * @author Jeremy.ARFI
 */
@ContextConfiguration(classes = ParkingSlotServiceImplTest.SpringTestConfig.class)
public class ParkingSlotServiceImplTest extends TollParkingTestConfiguration {

    /**
     * The test configuration for this test class
     */
    @Configuration
    @ComponentScan(basePackageClasses = { ParkingSlotServiceImplTest.class, TollParkingMapper.class, ParkingSlotService.class })
    public static class SpringTestConfig {

    }

    @Autowired
    private ParkingSlotService parkingSlotService;

    /**
     * Allows to init the list of parking slots for the tests
     */
    @Before
    public void initParkingSlots() {
        List<ParkingSlot> slots = new ArrayList<>();

        Car firstCar = new Car();
        firstCar.setId("AA-123-BB");
        firstCar.setCarType(EngineType.STANDARD);

        Car secondCar = new Car();
        secondCar.setId("ZZ-666-TP");
        secondCar.setCarType(EngineType.ELECTRIC_20KW);

        slots.add(new ParkingSlot(1L, EngineType.STANDARD));
        slots.add(new ParkingSlot(2L, EngineType.STANDARD));

        ParkingSlot standardSlotOccupied = new ParkingSlot(3L, EngineType.STANDARD);
        standardSlotOccupied.setFree(false);
        standardSlotOccupied.setParkedCar(firstCar);
        standardSlotOccupied.setOccupationStartDate(LocalDateTime.now());
        slots.add(standardSlotOccupied);

        slots.add(new ParkingSlot(4L, EngineType.ELECTRIC_20KW));

        ParkingSlot electric20KwSlotOccupied = new ParkingSlot(4L, EngineType.ELECTRIC_20KW);
        electric20KwSlotOccupied.setFree(false);
        electric20KwSlotOccupied.setParkedCar(secondCar);
        electric20KwSlotOccupied.setOccupationStartDate(LocalDateTime.now());
        slots.add(electric20KwSlotOccupied);

        slots.add(new ParkingSlot(6L, EngineType.ELECTRIC_50KW));
        slots.add(new ParkingSlot(7L, EngineType.ELECTRIC_50KW));
        slots.add(new ParkingSlot(8L, EngineType.ELECTRIC_50KW));

        TollParking.getInstance().setParkingSlots(slots);
    }

    @Test
    public void testGetAllAvailableParkingSlots() throws Exception {
        List<ParkingSlot> slots = parkingSlotService.getAllAvailableParkingSlots();
        assertThat(slots).isNotNull().hasSize(6);
    }

    @Test
    public void testGetAllAvailableParkingSlotsForGiven20KwElectricCar() throws Exception {
        List<ParkingSlot> slots = parkingSlotService.getAllAvailableParkingSlotsForGivenCarType(EngineType.ELECTRIC_20KW);
        assertThat(slots).isNotNull().hasSize(3);
    }

    @Test
    public void testGetAllAvailableParkingSlotsForGiven50KwElectricCar() throws Exception {
        List<ParkingSlot> slots = parkingSlotService.getAllAvailableParkingSlotsForGivenCarType(EngineType.ELECTRIC_50KW);
        assertThat(slots).isNotNull().hasSize(5);
    }

    @Test
    public void testGetAllAvailableParkingSlotsForGivenStandardCar() throws Exception {
        List<ParkingSlot> slots = parkingSlotService.getAllAvailableParkingSlotsForGivenCarType(EngineType.STANDARD);
        assertThat(slots).isNotNull().hasSize(2);
    }

    @Test
    public void testGetParkingSlotFromParkedCar() throws Exception {
        ParkingSlot occupiedSlot = parkingSlotService.getParkingSlotFromParkedCar("AA-123-BB");
        assertThat(occupiedSlot).isNotNull();
        Car parkedCar = occupiedSlot.getParkedCar();
        assertThat(parkedCar).isNotNull();
        assertThat(parkedCar.getId()).isEqualTo("AA-123-BB");
        assertThat(occupiedSlot.isFree()).isFalse();
    }

    @Test
    public void testResetParkingSlot() throws Exception {
        parkingSlotService.resetParkingSlot(4L);
        ParkingSlot freedSlot = TollParking.getInstance().getParkingSlots().stream()
                .filter(slot -> 4L == slot.getId())
                .findAny()
                .orElse(null);

        assertThat(freedSlot).isNotNull();
        assertThat(freedSlot.isFree()).isTrue();
        assertThat(freedSlot.getParkedCar()).isNull();
        assertThat(freedSlot.getOccupationStartDate()).isNull();
        assertThat(freedSlot.getParkingSlotType()).isEqualTo(EngineType.ELECTRIC_20KW);
    }

    @Test(expected = NoParkingSlotAvailableException.class)
    public void testResetParkingSlotWrong() throws Exception {
        parkingSlotService.resetParkingSlot(499L);

    }

}
