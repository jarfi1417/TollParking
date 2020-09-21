package com.tollparkingapi.tollparking.controller.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tollparkingapi.tollparking.config.TollParkingTestConfigurationIT;
import com.tollparkingapi.tollparking.entity.EngineType;
import com.tollparkingapi.tollparking.resource.CarResource;
import com.tollparkingapi.tollparking.resource.ParkingSlotResource;

/**
 * Integration test class for ParkingSlotController
 * @author Jeremy.ARFI
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ParkingSlotControllerTestIT extends TollParkingTestConfigurationIT {

    @Before
    public void init() throws Exception {
        testInitTollParking("initTollParkingForm.json", status().isOk());
    }

    @Test
    public void testGetAllAvailableParkingSlots() throws Exception {
        MvcResult result = mvc.perform(get("/parking-slots/all-available")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<ParkingSlotResource> parkingSlotResources = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<ParkingSlotResource>>() {
        });

        assertThat(parkingSlotResources).isNotEmpty().hasSize(10);
    }

    @Test
    public void testGetAllAvailableParkingSlotsEngineType() throws Exception {
        MvcResult result = mvc.perform(get("/parking-slots/all-available-by-engine-type")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("engineType", "STANDARD"))
                .andExpect(status().isOk())
                .andReturn();

        List<ParkingSlotResource> parkingSlotResources = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<ParkingSlotResource>>() {
        });

        assertThat(parkingSlotResources).isNotEmpty().hasSize(2);
    }

    @Test
    public void testGetParkingSlotFromParkedCar() throws Exception {
        testCheckIn("checkInCarForm.json", status().isOk());

        MvcResult result = mvc.perform(get("/parking-slots/by-car/{cardId}", "AA-123-BB")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

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
    public void testResetParkingSlot() throws Exception {
        testCheckIn("checkInCarForm.json", status().isOk());
        MvcResult result = mvc.perform(get("/parking-slots/by-car/{cardId}", "AA-123-BB")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ParkingSlotResource parkingSlotResource = mapper.readValue(result.getResponse().getContentAsByteArray(),
                ParkingSlotResource.class);
        assertThat(parkingSlotResource).isNotNull();
        mvc.perform(put("/parking-slots/reset/{parkingSlotId}", parkingSlotResource.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testResetParkingSlotNoParkingSlotsAvailable() throws Exception {
        mvc.perform(put("/parking-slots/reset/{parkingSlotId}", 99999L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
