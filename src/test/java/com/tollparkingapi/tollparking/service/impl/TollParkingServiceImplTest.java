package com.tollparkingapi.tollparking.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import com.tollparkingapi.tollparking.config.TollParkingTestConfiguration;
import com.tollparkingapi.tollparking.entity.Bill;
import com.tollparkingapi.tollparking.entity.ParkingSlot;
import com.tollparkingapi.tollparking.entity.TollParking;
import com.tollparkingapi.tollparking.entity.pricingpolicy.PricingPolicy;
import com.tollparkingapi.tollparking.entity.pricingpolicy.PricingPolicyPerHourBuilder;
import com.tollparkingapi.tollparking.entity.pricingpolicy.PricingPolicyPerHourWithFixedFeeBuilder;
import com.tollparkingapi.tollparking.exception.CarNotFoundException;
import com.tollparkingapi.tollparking.exception.ParkingSlotConfigurationException;
import com.tollparkingapi.tollparking.exception.WrongEngineTypeException;
import com.tollparkingapi.tollparking.exception.WrongPricingPolicyException;
import com.tollparkingapi.tollparking.form.CarForm;
import com.tollparkingapi.tollparking.form.TollParkingForm;
import com.tollparkingapi.tollparking.mapper.TollParkingMapper;
import com.tollparkingapi.tollparking.service.TollParkingService;

/**
 * Class to test the toll parking services
 * @author Jeremy.ARFI
 */
@ContextConfiguration(classes = TollParkingServiceImplTest.SpringTestConfig.class)
public class TollParkingServiceImplTest extends TollParkingTestConfiguration {

    /**
     * The test configuration for this test class
     */
    @Configuration
    @ComponentScan(basePackageClasses = { TollParkingServiceImplTest.class, TollParkingMapper.class, TollParkingService.class })
    public static class SpringTestConfig {

    }

    @Autowired
    private TollParkingService tollParkingService;

    /**
     * Allows to init and get a new {@link TollParking}
     * @param nbStandardParkingSlots number of standard parking slots
     * @param nb20kwElectricParkingSlots number of 20 KW electric parking slots
     * @param nb50kwElectricParkingSlots number of 50 KW electric parking slots
     * @param pricingPolicyType the pricing policy type
     * @return a new {@link TollParking}
     * @throws Exception
     */
    private TollParking init(
            int nbStandardParkingSlots,
            int nb20KWElectricParkingSlots,
            int nb50KWElectricParkingSlots,
            String pricingPolicyType) throws Exception {
        TollParkingForm tollParkingForm = createTollParkingForm(
                nbStandardParkingSlots, nb20KWElectricParkingSlots, nb50KWElectricParkingSlots, pricingPolicyType);
        tollParkingService.initTollParking(tollParkingForm);
        TollParking tollParking = TollParking.getInstance();
        assertThat(tollParking).isNotNull();
        return tollParking;
    }

    /**
     * Allows to enter the parking and get the car identifier
     * @param carType the car engine type
     * @return the car identifier which entered the parking
     * @throws Exception
     */
    private ParkingSlot enterParking(String carType) throws Exception {
        CarForm carForm = new CarForm();
        carForm.setCarType(carType);
        carForm.setId("AA-123-BB");
        ParkingSlot parkingSlotOccupied = tollParkingService.enterParking(carForm);
        TollParking tollParking = TollParking.getInstance();
        assertThat(tollParking).isNotNull();
        assertThat(tollParking.getParkingSlots())
                .filteredOn(parkingSlot -> parkingSlot.getParkedCar() != null)
                .extracting(parkingSlot -> parkingSlot.getParkedCar().getId())
                .contains("AA-123-BB");
        return parkingSlotOccupied;
    }

    @Test
    public void testInitTollParking() throws Exception {
        TollParking tollParking = init(5, 2, 3, "PER_HOUR");
        assertThat(tollParking.getPricingPolicyBuilder()).isInstanceOf(PricingPolicyPerHourBuilder.class);
        List<ParkingSlot> parkingSlots = tollParking.getParkingSlots();
        assertThat(parkingSlots).hasSize(10);
        assertThat(parkingSlots).filteredOn("parkingSlotType.name", "STANDARD").hasSize(5);
        assertThat(parkingSlots).filteredOn("parkingSlotType.name", "ELECTRIC_20KW").hasSize(2);
        assertThat(parkingSlots).filteredOn("parkingSlotType.name", "ELECTRIC_50KW").hasSize(3);

    }

    @Test
    public void testInitTollParkingWithPricingPolicyPerHourWithFee() throws Exception {
        TollParking tollParking = init(5, 2, 3, "PER_HOUR_WITH_FIXED_FEE");
        assertThat(tollParking.getPricingPolicyBuilder()).isInstanceOf(PricingPolicyPerHourWithFixedFeeBuilder.class);
        List<ParkingSlot> parkingSlots = tollParking.getParkingSlots();
        assertThat(parkingSlots).hasSize(10);
        assertThat(parkingSlots).filteredOn("parkingSlotType.name", "STANDARD").hasSize(5);
        assertThat(parkingSlots).filteredOn("parkingSlotType.name", "ELECTRIC_20KW").hasSize(2);
        assertThat(parkingSlots).filteredOn("parkingSlotType.name", "ELECTRIC_50KW").hasSize(3);

    }

    @Test(expected = WrongPricingPolicyException.class)
    public void testInitTollParkingWrongPricingPolicy() throws Exception {
        TollParkingForm tollParkingForm = createTollParkingForm(5, 2, 3, "WRONG_PRICING_POLICY");
        tollParkingService.initTollParking(tollParkingForm);
    }

    @Test(expected = WrongPricingPolicyException.class)
    public void testInitTollParkingNullPricingPolicy() throws Exception {
        TollParkingForm tollParkingForm = createTollParkingForm(5, 2, 3, null);
        tollParkingService.initTollParking(tollParkingForm);
    }

    @Test(expected = ParkingSlotConfigurationException.class)
    public void testInitTollParkingBadConfiguration() throws Exception {
        TollParkingForm tollParkingForm = createTollParkingForm(0, 0, 0, "PER_HOUR");
        tollParkingService.initTollParking(tollParkingForm);
    }

    @Test(expected = ParkingSlotConfigurationException.class)
    public void testInitTollParkingWrongConfiguration() throws Exception {
        TollParkingForm tollParkingForm = createTollParkingForm(-1, 2, 3, "PER_HOUR");
        tollParkingService.initTollParking(tollParkingForm);
    }

    @Test
    public void testGetTollParkingSize() throws Exception {
        init(5, 2, 3, "PER_HOUR");
        assertThat(tollParkingService.getTollParkingSize()).isEqualTo(10);
    }

    @Test
    public void testEnterParking() throws Exception {
        init(5, 2, 3, "PER_HOUR");
        enterParking("STANDARD");
    }

    @Test(expected = WrongEngineTypeException.class)
    public void testEnterParkingWrongCarType() throws Exception {
        init(5, 2, 3, "PER_HOUR");
        enterParking("WRONG_CAR_TYPE");
    }

    @Test
    public void testLeaveParking() throws Exception {
        init(5, 2, 3, "PER_HOUR");
        ParkingSlot parkingSlotOccupied = enterParking("STANDARD");

        Bill bill = tollParkingService.leaveParking(parkingSlotOccupied.getParkedCar().getId());
        assertThat(tollParkingService.getTollParkingSize()).isEqualTo(10);
        assertThat(bill).isNotNull();
        int nbHoursSpent = (int) Duration.between(bill.getOccupationStartDate(), bill.getOccupationEndDate()).toHours();
        assertThat(bill.getTotalPriceToPay()).isEqualTo(nbHoursSpent * TollParking.getInstance().getPricingPolicy().getPricePerHour());
        ParkingSlot billedParkingSlot = bill.getBilledParkingSlot();
        assertThat(billedParkingSlot).isNotNull();
    }

    @Test
    public void testLeaveParkingWithPricingPolicyPerHourWithFee() throws Exception {
        init(5, 2, 3, "PER_HOUR_WITH_FIXED_FEE");
        ParkingSlot parkingSlotOccupied = enterParking("STANDARD");
        Bill bill = tollParkingService.leaveParking(parkingSlotOccupied.getParkedCar().getId());
        assertThat(tollParkingService.getTollParkingSize()).isEqualTo(10);
        assertThat(bill).isNotNull();
        int nbHoursSpent = (int) Duration.between(bill.getOccupationStartDate(), bill.getOccupationEndDate()).toHours();
        PricingPolicy pricingPolicy = TollParking.getInstance().getPricingPolicy();
        assertThat(bill.getTotalPriceToPay()).isEqualTo((nbHoursSpent * pricingPolicy.getPricePerHour()) + pricingPolicy.getFixedFee());

        ParkingSlot billedParkingSlot = bill.getBilledParkingSlot();
        assertThat(billedParkingSlot).isNotNull();
    }

    @Test(expected = CarNotFoundException.class)
    public void testLeaveParkingWithWrongCarId() throws Exception {
        init(5, 2, 3, "PER_HOUR_WITH_FIXED_FEE");
        enterParking("STANDARD");
        tollParkingService.leaveParking("WRONG_CAR_ID");
    }

}
