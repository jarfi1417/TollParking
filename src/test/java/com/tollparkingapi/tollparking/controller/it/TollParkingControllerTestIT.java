package com.tollparkingapi.tollparking.controller.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tollparkingapi.tollparking.config.TollParkingTestConfigurationIT;
import com.tollparkingapi.tollparking.entity.EngineType;
import com.tollparkingapi.tollparking.entity.TollParking;
import com.tollparkingapi.tollparking.resource.BillResource;
import com.tollparkingapi.tollparking.resource.CarResource;
import com.tollparkingapi.tollparking.resource.ParkingSlotResource;

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
     * @return the mock mvc result
     * @throws Exception
     */
    private MvcResult testCheckOut(String carId, ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(put("/checkout/{carId}", carId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(expectedStatus)
                .andReturn();
    }

    @Test
    public void testInitTollParking() throws Exception {
        MvcResult result = testInitTollParking("initTollParkingForm.json", status().isOk());

        List<ParkingSlotResource> parkingSlotResources = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<ParkingSlotResource>>() {
        });

        assertThat(parkingSlotResources).isNotEmpty().hasSize(10);
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
        MvcResult result = testCheckIn("checkInCarForm.json", status().isOk());

        ParkingSlotResource parkingSlotResource = mapper.readValue(result.getResponse().getContentAsByteArray(),
                ParkingSlotResource.class);
        assertThat(parkingSlotResource).isNotNull();
        assertThat(parkingSlotResource.isFree()).isFalse();
        CarResource carResource = parkingSlotResource.getParkedCar();
        assertThat(carResource).isNotNull();
        assertThat(carResource.getId()).isEqualTo("AA-123-BB");
        assertThat(carResource.getCarType()).isEqualTo(EngineType.STANDARD);
    }

    @Test
    public void testCheckInNoParkingSlotAvailable() throws Exception {
        testCheckIn("checkInCarForm.json", status().isNotFound());
    }

    @Test
    public void testCheckOut() throws Exception {
        testInitTollParking("initTollParkingForm.json", status().isOk());
        testCheckIn("checkInCarForm.json", status().isOk());
        MvcResult result = testCheckOut("AA-123-BB", status().isOk());
        BillResource billResource = mapper.readValue(result.getResponse().getContentAsByteArray(),
                BillResource.class);
        assertThat(billResource).isNotNull();

        // Price to pay is equals to 0.0 because of the very short period of
        // park
        assertThat(billResource.getTotalPriceToPay()).isEqualTo(0.0);
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
