# Toll Parking Library
Toll Parking Java API

## Description

This API manage a toll parking for standard and electric cars. Cars can checkin and checkout. Customers are billed when leaving automatically.

## Technical details

* Spring Boot
* Maven
* JUnit 4
* Mapstruct
* Lombok
* Swagger

## Prerequisites:

* JDK 8
* Maven

### How to

Clone the repository
```
git clone https://github.com/jarfi1417/TollParking.git
```

Compile and run the unit and integration tests
```
mvn clean install
```

Run the application
```
mvn spring-boot:run
```

Swagger documentation can be found at: http://localhost:8888/tollparking/swagger-ui/

## How to use the API

In order to start using the toll parking services, you must launch the initTollParking service first to initialize the parking.

NB : everything is stored in memory.

## REST endpoints

### TollParking

* initTollParking: init toll parking with toll parking data ie. the number of parking slots for each engine types (STANDARD - ELECTRIC_20KW - ELECTRIC_50KW), a pricing policy type (for now PER_HOUR or PER_HOUR_WITH_FIXED_FEE) and a the price information.

* checkIn: A car wants to enter the parking. 

* checkOut: A car wants to leave the parking.

* getTollParkingSize: the toll parking parking slots total size

### ParkingSlot

* getAllAvailableParkingSlots: All the toll parking parking slots still available

* getAllAvailableParkingSlotsFromEngineType: All the toll parking parking slots still available for a given engine type

* getParkingSlotFromParkedCar: the parking slot where the given car is parked

* resetParkingSlot: reset the given parking slot to initial state
