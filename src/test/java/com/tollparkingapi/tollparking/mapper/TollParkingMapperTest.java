package com.tollparkingapi.tollparking.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import com.tollparkingapi.tollparking.config.TollParkingTestConfiguration;
import com.tollparkingapi.tollparking.entity.Car;
import com.tollparkingapi.tollparking.entity.EngineType;
import com.tollparkingapi.tollparking.entity.ParkingSlot;
import com.tollparkingapi.tollparking.entity.pricingpolicy.PricingPolicy;
import com.tollparkingapi.tollparking.form.CarForm;
import com.tollparkingapi.tollparking.resource.CarResource;
import com.tollparkingapi.tollparking.resource.ParkingSlotResource;
import com.tollparkingapi.tollparking.resource.PricingPolicyResource;

/**
 * Class to test the toll parking mapper
 * @author Jeremy.ARFI
 */
@ContextConfiguration(classes = TollParkingMapperTest.SpringTestConfig.class)
public class TollParkingMapperTest extends TollParkingTestConfiguration {

    /**
     * The test configuration for this test class
     */
    @Configuration
    @ComponentScan(basePackageClasses = {
            TollParkingMapperTest.class, TollParkingMapper.class })
    public static class SpringTestConfig {

    }

    @Test
    public void testAsPricingPolicy() throws Exception {
        // given
        PricingPolicy pricingPolicy = new PricingPolicy();
        pricingPolicy.setFixedFee(1.0);
        pricingPolicy.setPricePerHour(2.0);

        // when
        PricingPolicyResource pricingPolicyResource = tollParkingMapper.asPricingPolicyResource(pricingPolicy);

        // then
        assertThat(pricingPolicyResource).isNotNull();
        assertThat(pricingPolicyResource.getFixedFee()).isEqualTo(1.0);
        assertThat(pricingPolicyResource.getPricePerHour()).isEqualTo(2.0);
    }

    @Test
    public void testAsCar() throws Exception {
        // given
        CarForm carForm = new CarForm();
        carForm.setId("TEST_ID");
        carForm.setCarType("STANDARD");

        // when
        Car car = tollParkingMapper.asCar(carForm);

        // then
        assertThat(car).isNotNull();
        assertThat(car.getId()).isEqualTo("TEST_ID");
        assertThat(car.getCarType()).isEqualTo(EngineType.STANDARD);
    }

    @Test
    public void testAsCarResource() throws Exception {
        // given
        Car car = new Car();
        car.setId("TEST_ID");
        car.setCarType(EngineType.STANDARD);

        // when
        CarResource carResource = tollParkingMapper.asCarResource(car);

        // then
        assertThat(carResource).isNotNull();
        assertThat(carResource.getId()).isEqualTo("TEST_ID");
        assertThat(carResource.getCarType()).isEqualTo(EngineType.STANDARD);
    }

    @Test
    public void testAsParkingSlotResource() throws Exception {
        // given
        Car parkedCar = new Car();
        parkedCar.setId("TEST_ID");
        parkedCar.setCarType(EngineType.STANDARD);

        ParkingSlot parkingSlot = new ParkingSlot(1L, EngineType.STANDARD);
        parkingSlot.setFree(false);
        parkingSlot.setOccupationStartDate(LocalDateTime.of(2020, 9, 15, 10, 25, 30));
        parkingSlot.setParkedCar(parkedCar);

        // when
        ParkingSlotResource parkingSlotResource = tollParkingMapper.asParkingSlotResource(parkingSlot);

        // then
        assertThat(parkingSlotResource).isNotNull();
        assertThat(parkingSlotResource.getId()).isEqualTo(1L);
        assertThat(parkingSlotResource.getParkingSlotType()).isEqualTo(EngineType.STANDARD);
        assertThat(parkingSlotResource.isFree()).isFalse();
        CarResource parkedCarResource = parkingSlotResource.getParkedCar();
        assertThat(parkedCarResource).isNotNull();
        assertThat(parkedCarResource.getId()).isEqualTo("TEST_ID");
        assertThat(parkedCarResource.getCarType()).isEqualTo(EngineType.STANDARD);

    }

}
