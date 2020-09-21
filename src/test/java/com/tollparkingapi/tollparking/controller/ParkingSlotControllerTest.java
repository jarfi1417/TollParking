package com.tollparkingapi.tollparking.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tollparkingapi.tollparking.config.TollParkingTestConfiguration;
import com.tollparkingapi.tollparking.entity.Car;
import com.tollparkingapi.tollparking.entity.EngineType;
import com.tollparkingapi.tollparking.entity.ParkingSlot;
import com.tollparkingapi.tollparking.exception.NoParkingSlotAvailableException;
import com.tollparkingapi.tollparking.form.TollParkingForm;
import com.tollparkingapi.tollparking.mapper.TollParkingMapper;
import com.tollparkingapi.tollparking.resource.CarResource;
import com.tollparkingapi.tollparking.resource.ParkingSlotResource;
import com.tollparkingapi.tollparking.service.ParkingSlotService;
import com.tollparkingapi.tollparking.service.TollParkingService;

/**
 * Unit test class for ParkingSlotController
 * @author Jeremy.ARFI
 */
@ContextConfiguration(classes = ParkingSlotControllerTest.SpringTestConfig.class)
@WebMvcTest(ParkingSlotController.class)
public class ParkingSlotControllerTest extends TollParkingTestConfiguration {

    /**
     * The test configuration for this test class
     */
    @Configuration
    @ComponentScan(basePackageClasses = {
            TollParkingForm.class, ParkingSlotControllerTest.class, TollParkingMapper.class, ParkingSlotController.class })
    public static class SpringTestConfig {

    }

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ParkingSlotService parkingSlotService;

    @MockBean
    private TollParkingService tollParkingService;

    @Test
    public void testGetAllAvailableParkingSlots() throws Exception {
        Mockito.when(parkingSlotService.getAllAvailableParkingSlots()).thenReturn(new ArrayList<>());

        MvcResult result = mvc.perform(get("/parking-slots/all-available")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<ParkingSlotResource> parkingSlotResources = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<ParkingSlotResource>>() {
        });

        assertThat(parkingSlotResources).isEmpty();
    }

    @Test
    public void testGetAllAvailableParkingSlotsEngineType() throws Exception {
        Mockito.when(parkingSlotService.getAllAvailableParkingSlotsForGivenCarType(
                Mockito.any(EngineType.class))).thenReturn(new ArrayList<>());

        MvcResult result = mvc.perform(get("/parking-slots/all-available-by-engine-type")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("engineType", "STANDARD"))
                .andExpect(status().isOk())
                .andReturn();

        List<ParkingSlotResource> parkingSlotResources = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<ParkingSlotResource>>() {
        });

        assertThat(parkingSlotResources).isEmpty();
    }

    @Test
    public void testGetParkingSlotFromParkedCar() throws Exception {
        Car firstCar = new Car();
        firstCar.setId("AA-123-BB");
        firstCar.setCarType(EngineType.STANDARD);
        ParkingSlot standardSlotOccupied = new ParkingSlot(3L, EngineType.STANDARD);
        standardSlotOccupied.setFree(false);
        standardSlotOccupied.setParkedCar(firstCar);
        standardSlotOccupied.setOccupationStartDate(LocalDateTime.of(2020, 9, 30, 10, 30, 20));

        Mockito.when(parkingSlotService.getParkingSlotFromParkedCar(
                Mockito.anyString())).thenReturn(standardSlotOccupied);
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
        mvc.perform(put("/parking-slots/reset/{parkingSlotId}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testResetParkingSlotNoParkingSlotsAvailable() throws Exception {
        Mockito.doThrow(new NoParkingSlotAvailableException("Error")).when(parkingSlotService).resetParkingSlot(Mockito.anyLong());

        mvc.perform(put("/parking-slots/reset/{parkingSlotId}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
