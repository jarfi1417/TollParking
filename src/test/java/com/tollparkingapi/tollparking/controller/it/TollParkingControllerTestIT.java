package com.tollparkingapi.tollparking.controller.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import com.tollparkingapi.tollparking.config.TollParkingTestConfigurationIT;
import com.tollparkingapi.tollparking.entity.TollParking;

/**
 * Integration test class for TollParkingController
 * @author Jeremy.ARFI
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TollParkingControllerTestIT extends TollParkingTestConfigurationIT {

    @After
    public void clear() {
        TollParking.getInstance().getParkingSlots().clear();
    }

    /**
     * Allows to perform a check out request
     * @param carId the car identifier
     * @param expectedStatus the expected status returned by the request
     * @throws Exception
     */
    private void testCheckOut(String carId, ResultMatcher expectedStatus) throws Exception {
        mvc.perform(put("/checkout/{carId}", carId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(expectedStatus);
    }

    @Test
    public void testInitTollParking() throws Exception {
        testInitTollParking("initTollParkingForm.json", status().isOk());
    }

    @Test
    public void testInitTollParkingWrongPricingPolicy() throws Exception {
        testInitTollParking("initTollParkingFormWrongPricingPolicy.json", status().isBadRequest());
    }

    @Test
    public void testInitTollParkingParkingSlotConfigurationError() throws Exception {
        testInitTollParking("initTollParkingFormParkingSlotConfigurationError.json", status().isBadRequest());
    }

    @Test
    public void testCheckIn() throws Exception {
        testInitTollParking("initTollParkingForm.json", status().isOk());
        testCheckIn("checkInCarForm.json", status().isOk());
    }

    @Test
    public void testCheckInNoParkingSlotAvailable() throws Exception {
        testCheckIn("checkInCarForm.json", status().isNotFound());
    }

    @Test
    public void testCheckOut() throws Exception {
        testInitTollParking("initTollParkingForm.json", status().isOk());
        testCheckIn("checkInCarForm.json", status().isOk());
        testCheckOut("AA-123-BB", status().isOk());
    }

    @Test
    public void testCheckOutCarNotFoundException() throws Exception {
        testInitTollParking("initTollParkingForm.json", status().isOk());
        testCheckOut("CAR_ID_NOT_FOUND", status().isNotFound());
    }

    @Test
    public void testGetTollParkingSize() throws Exception {
        testInitTollParking("initTollParkingForm.json", status().isOk());

        MvcResult result = mvc.perform(get("/size")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("10");
    }

}
