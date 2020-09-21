package com.tollparkingapi.tollparking.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tollparkingapi.tollparking.mapper.TollParkingMapper;

@RunWith(SpringRunner.class)
public abstract class TollParkingTestConfigurationIT {

    @Autowired
    protected TollParkingMapper tollParkingMapper;

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;

    /**
     * Allows to read a file
     * @param file the file to read
     * @return the string value of the file content
     * @throws Exception if an error occured during the file reading
     */
    protected static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    /**
     * Allows to perform an init the toll parking request
     * @param jsonFile the input json file request
     * @param expectedStatus the expected status returned by the request
     * @throws Exception if an error occured during the file reading
     */
    protected void testInitTollParking(String jsonFile, ResultMatcher expectedStatus) throws Exception {
        mvc.perform(post("/init")
                .content(readFileAsString("src/test/resources/jsonSet/" + jsonFile))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(expectedStatus);
    }

    /**
     * Allows to perform a check in request
     * @param jsonFile the input json file request
     * @param expectedStatus the expected status returned by the request
     * @throws Exception if an error occured during the file reading
     */
    protected void testCheckIn(String jsonFile, ResultMatcher expectedStatus) throws Exception {
        mvc.perform(put("/checkin")
                .content(readFileAsString("src/test/resources/jsonSet/" + jsonFile))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(expectedStatus);
    }
}
