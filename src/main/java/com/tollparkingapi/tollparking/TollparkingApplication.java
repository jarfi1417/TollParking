package com.tollparkingapi.tollparking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Springboot main class
 * @author Jeremy.ARFI
 */
@SpringBootApplication
@EnableAutoConfiguration
public class TollparkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TollparkingApplication.class, args);
    }

}
