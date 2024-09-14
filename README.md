# Electricity Consumption Calculator

## Project Overview

The Electricity Consumption Calculator project is designed to compute electricity consumption and charging time for electric vehicles based on given input data. The application processes CSV files containing information about charging stations, entry and exit points, vehicle types, charging times, and trip details. It calculates the total electricity consumption, time spent at charging stations, and other relevant metrics for each vehicle type.

## Project Structure

- **`com.nice.coday`**: Contains all the classes and implementation required for the electricity consumption calculation.

## Classes

### 1. `ResourceInfo`

**Description**: Represents the paths to the CSV files required for processing.

**Fields**:
- `chargingStationInfoPath` (Path): Path to the CSV file containing charging station data.
- `entryExitPointInfoPath` (Path): Path to the CSV file containing entry and exit points.
- `timeToChargeVehicleInfoPath` (Path): Path to the CSV file containing time to charge data for vehicles.
- `tripDetailsPath` (Path): Path to the CSV file containing trip details.
- `vehicleTypeInfoPath` (Path): Path to the CSV file containing vehicle type information.

**Methods**:
- Getters and setters for each field.

### 2. `ChargingStation`

**Description**: Represents a charging station with its name and distance from the starting point.

**Fields**:
- `stationName` (String): Name of the charging station.
- `distanceFromStart` (Double): Distance of the station from the starting point.

**Methods**:
- Getters and setters for each field.

### 3. `VehicleInfo`

**Description**: Contains information about vehicle types, including charging details.

**Fields**:
- `numberOfUnitsForFullyCharge` (int): Number of units required to fully charge the vehicle.
- `mileage` (Double): Mileage of the vehicle (distance per unit of charge).

**Methods**:
- Getters and setters for each field.
- `toString()`: Returns a string representation of the `VehicleInfo` object.

### 4. `TripDetails`

**Description**: Represents a trip taken by a vehicle, including entry and exit points and remaining battery percentage.

**Fields**:
- `id` (String): Unique identifier for the trip.
- `vehicleType` (String): Type of the vehicle.
- `remainingBatteryPercentage` (Double): Battery percentage remaining at the start of the trip.
- `entryPoint` (String): Entry point of the trip.
- `exitPoint` (String): Exit point of the trip.

**Methods**:
- Getters for each field.
- `toString()`: Returns a string representation of the `TripDetails` object.

### 5. `result`

**Description**: Holds the result of the electricity consumption calculation for each vehicle type.

**Fields**:
- `units` (BigDecimal): Total units of electricity consumed.
- `time` (BigDecimal): Total time spent charging.
- `trips` (Long): Number of trips.

**Methods**:
- Getters and setters for each field.
- `toString()`: Returns a string representation of the `result` object.

### 6. `ConsumptionDetails`

**Description**: Represents detailed consumption metrics for each vehicle type.

**Fields**:
- `vehicleType` (String): Type of vehicle.
- `units` (Double): Units of electricity consumed.
- `time` (Long): Time spent charging.
- `trips` (Long): Number of trips.

**Methods**:
- Getters and setters for each field.

### 7. `ElectricityConsumptionCalculator`

**Description**: Interface defining the contract for calculating electricity consumption and time.

**Methods**:
- `calculateElectricityAndTimeConsumption(ResourceInfo resourcesInfo)`: Calculates and returns the electricity consumption and charging time based on the provided `ResourceInfo`.

### 8. `ElectricityConsumptionCalculatorImpl`

**Description**: Implementation of the `ElectricityConsumptionCalculator` interface.

**Key Methods**:
- `closestChargingStation(long distance, long start)`: Finds the closest charging station within a specified distance from the start point.
- `calculateElectricityAndTimeConsumption(ResourceInfo resourceInfo)`: Performs the main calculation of electricity consumption and time, using the provided resource paths to read CSV data and compute results.

## Usage

1. **Setup Paths**:
   Create a `ResourceInfo` object with paths to your CSV files.

   ```java
   ResourceInfo resourceInfo = new ResourceInfo(
       Path.of("path/to/chargingStations.csv"),
       Path.of("path/to/entryExitPoints.csv"),
       Path.of("path/to/timeToCharge.csv"),
       Path.of("path/to/tripDetails.csv"),
       Path.of("path/to/vehicleTypes.csv")
   );
   ```
2. **Create Calculator**:
   Instantiate the `ElectricityConsumptionCalculatorImpl` class.
   
    ```java
    ElectricityConsumptionCalculator calculator = new ElectricityConsumptionCalculatorImpl();
    ```
3. **Calculate Consumption**:
   Call the `calculateElectricityAndTimeConsumption` method with the `ResourceInfo` object.
   
    ```java
    ConsumptionResult result = calculator.calculateElectricityAndTimeConsumption(resourceInfo);
    ```
4. **Process Results**:
   Access the results from the `ConsumptionResult` object.
   
    ```java
    List<ConsumptionDetails> details = result.getConsumptionDetails();
    Map<String, Long> chargingStationTimes = result.getTotalChargingStationTime();

    ```

