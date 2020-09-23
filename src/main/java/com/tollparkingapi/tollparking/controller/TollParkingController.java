package com.tollparkingapi.tollparking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tollparkingapi.tollparking.entity.TollParking;
import com.tollparkingapi.tollparking.exception.CarNotFoundException;
import com.tollparkingapi.tollparking.exception.NoParkingSlotAvailableException;
import com.tollparkingapi.tollparking.exception.ParkingSlotConfigurationException;
import com.tollparkingapi.tollparking.exception.WrongEngineTypeException;
import com.tollparkingapi.tollparking.exception.WrongPricingPolicyException;
import com.tollparkingapi.tollparking.form.CarForm;
import com.tollparkingapi.tollparking.form.TollParkingForm;
import com.tollparkingapi.tollparking.mapper.TollParkingMapper;
import com.tollparkingapi.tollparking.resource.BillResource;
import com.tollparkingapi.tollparking.resource.ParkingSlotResource;
import com.tollparkingapi.tollparking.resource.TollParkingResource;
import com.tollparkingapi.tollparking.service.TollParkingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller to manage the Toll Parking services
 * @author Jeremy.ARFI
 */
@RestController
public class TollParkingController {

    @Autowired
    private TollParkingService tollParkingService;

    @Autowired
    private TollParkingMapper mapper;

    /**
     * Init the Toll Parking with the given {@link TollParkingForm} data
     * @param tollParkingForm the data to init the toll parking
     * @return the toll parking information
     */
    @Operation(summary = "Allows to init the toll parking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "toll parking initiated", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Wrong pricing policy", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/init")
    public TollParkingResource initTollParking(@RequestBody TollParkingForm tollParkingForm) {
        try {
            TollParking tollParking = tollParkingService.initTollParking(tollParkingForm);
            TollParkingResource tollParkingResource = new TollParkingResource();
            tollParkingResource.setParkingSlots(mapper.asParkingSlotResourceList(tollParking.getParkingSlots()));
            tollParkingResource.setPricingPolicy(mapper.asPricingPolicyResource(tollParking.getPricingPolicy()));
            return tollParkingResource;
        } catch (WrongPricingPolicyException wpe) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, wpe.getMessage(), wpe);
        } catch (ParkingSlotConfigurationException psce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, psce.getMessage(), psce);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * Allows to check in the toll parking
     * @param carForm the car data
     * @return the parking slot data where the given car is parked
     */
    @Operation(summary = "Allows to check in the toll parking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "check in the toll parking", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "No parking slot available found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/checkin")
    public ParkingSlotResource checkIn(@RequestBody CarForm carForm) {
        try {
            return mapper.asParkingSlotResource(tollParkingService.enterParking(carForm));
        } catch (NoParkingSlotAvailableException npsae) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, npsae.getMessage(), npsae);

        } catch (WrongEngineTypeException wete) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, wete.getMessage(), wete);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * Allows to check out the toll parking
     * @param carId the car identifier
     * @return the bill
     */
    @Operation(summary = "Allows to check out the toll parking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "check out the toll parking", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "No car found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/checkout/{carId}")
    public BillResource checkOut(@PathVariable String carId) {
        try {
            return mapper.asBillResource(tollParkingService.leaveParking(carId));
        } catch (CarNotFoundException cnfe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, cnfe.getMessage(), cnfe);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * Allows to get the toll parking total size
     * @return the toll parking total size
     */
    @Operation(summary = "Allows to get the toll parking total size")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get the toll parking total size", content = { @Content(mediaType = "application/json") })
    })
    @GetMapping("/size")
    public int getTollParkingSize() {
        return tollParkingService.getTollParkingSize();
    }

}
