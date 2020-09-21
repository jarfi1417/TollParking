package com.tollparkingapi.tollparking.config;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.tollparkingapi.tollparking.form.TollParkingForm;
import com.tollparkingapi.tollparking.mapper.TollParkingMapper;

@RunWith(SpringRunner.class)
public abstract class TollParkingTestConfiguration {

    @Autowired
    protected TollParkingMapper tollParkingMapper;

    /**
     * Allows to create a new {@link TollParkingForm}
     * @param nbStandardParkingSlots number of standard parking slots
     * @param nb20kwElectricParkingSlots number of 20 KW electric parking slots
     * @param nb50kwElectricParkingSlots number of 50 KW electric parking slots
     * @param pricingPolicyType the pricing policy type
     * @return a new {@link TollParkingForm}
     */
    protected TollParkingForm createTollParkingForm(
            int nbStandardParkingSlots,
            int nb20KWElectricParkingSlots,
            int nb50KWElectricParkingSlots,
            String pricingPolicyType) {
        TollParkingForm tollParkingForm = new TollParkingForm();
        tollParkingForm.setNb20KWElectricParkingSlots(nb20KWElectricParkingSlots);
        tollParkingForm.setNb50KWElectricParkingSlots(nb50KWElectricParkingSlots);
        tollParkingForm.setNbStandardParkingSlots(nbStandardParkingSlots);
        tollParkingForm.setPricingPolicyType(pricingPolicyType);
        tollParkingForm.setPricePerHour(2.5);
        tollParkingForm.setFixedFee(10.0);
        return tollParkingForm;
    }
}
