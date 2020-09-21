# Toll Parking Library
Toll Parking Java API

## Description

The API is used to manage a toll parking.

### How to

Build the code
```
mvn clean install
```
Run application
```
mvn spring-boot:run
```

Swagger documentation can be found at: http://localhost:8888/tollparking/swagger-ui/

## REST endpoints

### TollParking

initTollParking: init toll parking with toll parking data ie. the number of parking slots for each engine types (STANDARD - ELECTRIC_20KW - ELECTRIC_50KW), a pricing policy type (for now PER_HOUR or PER_HOUR_WITH_FIXED_FEE) and a the price information.

checkIn: A car wants to enter the parking. 

checkOut: A car wants to leave the parking.

getTollParkingSize: the toll parking parking slots total size

### ParkingSlot

getAllAvailableParkingSlots: All the toll parking parking slots still available

getAllAvailableParkingSlotsFromEngineType: All the toll parking parking slots still available for a given engine type

getParkingSlotFromParkedCar: the parking slot where the given car is parked

resetParkingSlot: reset the given parking slot to initial state