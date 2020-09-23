package com.tollparkingapi.tollparking.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tollparkingapi.tollparking.config.TollParkingTestConfiguration;
import com.tollparkingapi.tollparking.entity.TollParking;
import com.tollparkingapi.tollparking.entity.pricingpolicy.PricingPolicyPerHourBuilder;
import com.tollparkingapi.tollparking.exception.CarNotFoundException;
import com.tollparkingapi.tollparking.exception.NoParkingSlotAvailableException;
import com.tollparkingapi.tollparking.exception.ParkingSlotConfigurationException;
import com.tollparkingapi.tollparking.exception.WrongPricingPolicyException;
import com.tollparkingapi.tollparking.form.CarForm;
import com.tollparkingapi.tollparking.form.TollParkingForm;
import com.tollparkingapi.tollparking.mapper.TollParkingMapper;
import com.tollparkingapi.tollparking.service.ParkingSlotService;
import com.tollparkingapi.tollparking.service.TollParkingService;

/**
 * Unit test class for TollParkingController
 * @author Jeremy.ARFI
 */
@ContextConfiguration(classes = TollParkingControllerTest.SpringTestConfig.class)
@WebMvcTest(TollParkingController.class)
public class TollParkingControllerTest extends TollParkingTestConfiguration {

    /**
     * The test configuration for this test class
     */
    @Configuration
    @ComponentScan(basePackageClasses = {
            TollParkingForm.class, TollParkingControllerTest.class, TollParkingMapper.class, TollParkingController.class })
    public static class SpringTestConfig {

    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TollParkingService tollParkingService;

    @MockBean
    private ParkingSlotService parkingSlotService;

    /**
     * Allows to perform an init the toll parking request
     * @param expectedStatus the expected status returned by the request
     * @throws Exception
     */
    private void testInitTollParking(ResultMatcher expectedStatus) throws Exception {
        mvc.perform(post("/init")
                .content(objectMapper.writeValueAsString(new TollParkingForm()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(expectedStatus);
    }

    /**
     * Allows to perform a check in request
     * @param expectedStatus the expected status returned by the request
     * @throws Exception
     */
    private void testCheckIn(ResultMatcher expectedStatus) throws Exception {
        mvc.perform(put("/checkin")
                .content(objectMapper.writeValueAsString(new CarForm()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(expectedStatus);
    }

    /**
     * Allows to perform a check out request
     * @param expectedStatus the expected status returned by the request
     * @throws Exception
     */
    private void testCheckOut(ResultMatcher expectedStatus) throws Exception {
        mvc.perform(put("/checkout/{carId}", "AA-123-BB")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(expectedStatus);
    }

    @Test
    public void testInitTollParking() throws Exception {
        TollParking tollParking = new TollParking();

        PricingPolicyPerHourBuilder pricingPolicyPerHourBuilder = new PricingPolicyPerHourBuilder();
        pricingPolicyPerHourBuilder.setFixedFee(10.0);
        pricingPolicyPerHourBuilder.setPricePerHour(2.5);
        tollParking.setPricingPolicyBuilder(pricingPolicyPerHourBuilder);

        Mockito.when(tollParkingService.initTollParking(Mockito.any(TollParkingForm.class))).thenReturn(tollParking);
        testInitTollParking(status().isOk());
    }

    @Test
    public void testInitTollParkingWrongPricingPolicy() throws Exception {
        Mockito.doThrow(new WrongPricingPolicyException("Error")).when(tollParkingService).initTollParking(Mockito.any(TollParkingForm.class));
        testInitTollParking(status().isBadRequest());
    }

    @Test
    public void testInitTollParkingParkingSlotConfigurationError() throws Exception {
        Mockito.doThrow(new ParkingSlotConfigurationException("Error")).when(tollParkingService).initTollParking(Mockito.any(TollParkingForm.class));
        testInitTollParking(status().isBadRequest());
    }

    @Test
    public void testCheckIn() throws Exception {
        testCheckIn(status().isOk());
    }

    @Test
    public void testCheckInNoParkingSlotAvailable() throws Exception {
        Mockito.doThrow(new NoParkingSlotAvailableException("Error")).when(tollParkingService).enterParking(Mockito.any(CarForm.class));
        testCheckIn(status().isNotFound());
    }

    @Test
    public void testCheckOut() throws Exception {
        testCheckOut(status().isOk());
    }

    @Test
    public void testCheckOutCarNotFoundException() throws Exception {
        Mockito.doThrow(new CarNotFoundException("Error")).when(tollParkingService).leaveParking(Mockito.anyString());
        testCheckOut(status().isNotFound());
    }

    @Test
    public void testGetTollParkingSize() throws Exception {
        Mockito.when(tollParkingService.getTollParkingSize()).thenReturn(10);

        MvcResult result = mvc.perform(get("/size")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("10");
    }

}
