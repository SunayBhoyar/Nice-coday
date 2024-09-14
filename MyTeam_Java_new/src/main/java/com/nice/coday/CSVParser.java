package com.nice.coday;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CSVParser{
    public static List<ChargingStation> readChargingStationData(Path csvPath) {
        List<ChargingStation> stationList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toString()))) {
            String line;
            boolean firstLine = true;  // to skip the header

            while ((line = br.readLine()) != null) {
                // Skip the header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Split the line by commas
                String[] values = line.split(",");

                // Extract each column from the row
                String stationName = values[0];
                Double distanceFromStart = Double.parseDouble(values[1]);

                ChargingStation station = new ChargingStation(stationName, distanceFromStart);
                stationList.add(station);
            }
        } catch (Exception e) {
            e.toString();
        }

        return stationList;
    }
    public static Map<String, Double> readEntryExitPointData(Path csvPath) {
        Map<String, Double> entryExitMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toString()))) {
            String line;
            boolean firstLine = true;  // to skip the header

            while ((line = br.readLine()) != null) {
                // Skip the header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Split the line by commas
                String[] values = line.split(",");

                // Assuming first column is entry/exit point name and second is distance
                String entryExitPoint = values[0];
                double distanceFromStart = Double.parseDouble(values[1]);

                // Store the data in the map with entry/exit point as key and distance as value
                entryExitMap.put(entryExitPoint, distanceFromStart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entryExitMap;
    }
    public static Map<String, VehicleInfo> readVehicleTypeData(Path csvPath) {
        Map<String, VehicleInfo> vehicleMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toString()))) {
            String line;
            boolean firstLine = true;  // to skip the header

            while ((line = br.readLine()) != null) {
                // Skip the header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Split the line by commas
                String[] values = line.split(",");

                // Extract values from CSV
                String vehicleType = values[0];
                int numberOfUnitsForFullyCharge = Integer.parseInt(values[1]);
                double mileage = Double.parseDouble(values[2]);

                // Create a VehicleInfo object and store it in the map
                VehicleInfo vehicleInfo = new VehicleInfo(numberOfUnitsForFullyCharge, mileage);
                vehicleMap.put(vehicleType, vehicleInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vehicleMap;
    }
    public static Map<String, Map<String, Double>> readTimeToChargeData(Path csvPath) {
        // Outer map (VehicleType -> Inner Map)
        Map<String, Map<String, Double>> vehicleChargingMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toString()))) {
            String line;
            boolean firstLine = true;  // to skip the header

            while ((line = br.readLine()) != null) {
                // Skip the header line
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Split the line by commas
                String[] values = line.split(",");

                // Extract values from the CSV
                String vehicleType = values[0];
                String chargingStation = values[1];
                double timeToChargePerUnit = Double.parseDouble(values[2]);

                // Get the inner map for the specific vehicle type or create it if it doesn't exist
                Map<String, Double> chargingStationMap = vehicleChargingMap.getOrDefault(vehicleType, new HashMap<>());

                // Add the charging station and timeToChargePerUnit to the inner map
                chargingStationMap.put(chargingStation, timeToChargePerUnit);

                // Put the updated inner map back into the outer map
                vehicleChargingMap.put(vehicleType, chargingStationMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vehicleChargingMap;
    }
    public static List<TripDetails> readTripDetails(Path csvPath) {
        List<TripDetails> tripList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toString()))) {
            String line;
            boolean firstLine = true;  // to skip the header

            while ((line = br.readLine()) != null) {
                // Skip the header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Split the line by commas
                String[] values = line.split(",");

                // Extract each column from the row
                String id = values[0];
                String vehicleType = values[1];
                double remainingBatteryPercentage = Double.parseDouble(values[2]);
                String entryPoint = values[3];
                String exitPoint = values[4];

                // Create a TripDetails object and add it to the list
                TripDetails trip = new TripDetails(id, vehicleType, remainingBatteryPercentage, entryPoint, exitPoint);
                tripList.add(trip);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripList;
    }
    public static void main(String[] args)
    {

    }
}