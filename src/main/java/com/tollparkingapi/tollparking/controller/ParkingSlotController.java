package com.tollparkingapi.tollparking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tollparkingapi.tollparking.entity.EngineType;
import com.tollparkingapi.tollparking.entity.ParkingSlot;
import com.tollparkingapi.tollparking.exception.CarNotFoundException;
import com.tollparkingapi.tollparking.exception.NoParkingSlotAvailableException;
import com.tollparkingapi.tollparking.mapper.TollParkingMapper;
import com.tollparkingapi.tollparking.resource.ParkingSlotResource;
import com.tollparkingapi.tollparking.service.ParkingSlotService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller to manage the parking slot services
 * @author Jeremy.ARFI
 */
@RestController
@RequestMapping("/parking-slots")
public class ParkingSlotController {

    @Autowired
    private ParkingSlotService parkingSlotService;

    @Autowired
    private TollParkingMapper tollParkingMapper;

    /**
     * Allows to get all available parking slots
     * @return all available parking slots
     */
    @Operation(summary = "Allows to get all available parking slots")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "all available parking slots", content = { @Content(mediaType = "application/json") }) })
    @GetMapping("/all-available")
    public List<ParkingSlotResource> getAllAvailableParkingSlots() {
        List<ParkingSlot> slots = parkingSlotService.getAllAvailableParkingSlots();
        return tollParkingMapper.asParkingSlotResourceList(slots);
    }

    /**
     * Allows to get all available parking slots from a given engine type
     * @param engineType the engine type
     * @return all available parking slots from a given engine type
     */
    @Operation(summary = "Allows to get all available parking slots from a given engine type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "all available parking slots from a given engine type", content = { @Content(mediaType = "application/json") }) })
    @GetMapping("/all-available-by-engine-type")
    public List<ParkingSlotResource> getAllAvailableParkingSlotsFromEngineType(EngineType engineType) {
        return tollParkingMapper.asParkingSlotResourceList(parkingSlotService.getAllAvailableParkingSlotsForGivenCarType(engineType));
    }

    /**
     * Allows to get the parking slot from a given car identifier
     * @param carId the car identifier
     * @return all available parking slots from a given engine type
     */
    @Operation(summary = "Allows to get the parking slot from a given car identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the parking slot from a given car identifier", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "car identifier not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/by-car/{carId}")
    public ParkingSlotResource getParkingSlotFromParkedCar(@PathVariable String carId) {
        try {
            return tollParkingMapper.asParkingSlotResource(parkingSlotService.getParkingSlotFromParkedCar(carId));
        } catch (CarNotFoundException cnfe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, cnfe.getMessage(), cnfe);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * Allows to reset the given parking slot
     * @param parkingSlotId the parking slot identifier
     */
    @Operation(summary = "Allows to reset the given parking slot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the parking slot has been reset", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "parking slot identifier not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/reset/{parkingSlotId}")
    public void resetParkingSlot(@PathVariable Long parkingSlotId) {
        try {
            parkingSlotService.resetParkingSlot(parkingSlotId);
        } catch (NoParkingSlotAvailableException npsae) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, npsae.getMessage(), npsae);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

}
