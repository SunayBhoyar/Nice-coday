package com.nice.coday;


import java.io.IOException;
import java.util.List;
import java.util.Map;
public class ElectricityConsumptionCalculatorImpl implements ElectricityConsumptionCalculator {
    public List<ChargingStation> chargingStationData;
    public Map<String, Double> entryExitPointData;
    public Map<String, VehicleInfo> vehicleTypeData;
    public Map<String, Map<String, Double>> timeToChargeData;
    public List<TripDetails> tripDetails;

    public String closestChargingStation(int distance , int start) throws IOException {
        int low = 0;
        int high = chargingStationData.size() - 1;

        ChargingStation closestStation = null;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            ChargingStation currentStation = chargingStationData.get(mid);

            if (currentStation.getDistanceFromStart() <= (start + distance)) {
                if (currentStation.getDistanceFromStart() >= start) {
                    closestStation = currentStation;
                }
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        if (closestStation != null) {
            return closestStation.getStationName();
        } else {
            return "No Station Found";
        }
    }    

    @Override
    public ConsumptionResult calculateElectricityAndTimeConsumption(ResourceInfo resourceInfo) throws IOException {

        ConsumptionResult results = new ConsumptionResult(); 

        chargingStationData=CSVParser.readChargingStationData(resourceInfo.chargingStationInfoPath);
        entryExitPointData=CSVParser.readEntryExitPointData(resourceInfo.entryExitPointInfoPath);
        vehicleTypeData=CSVParser.readVehicleTypeData(resourceInfo.vehicleTypeInfoPath);
        timeToChargeData=CSVParser.readTimeToChargeData(resourceInfo.timeToChargeVehicleInfoPath);
        tripDetails=CSVParser.readTripDetails(resourceInfo.tripDetailsPath);
        
        System.out.println(closestChargingStation(55,40));
        
        return results ; 
    }
}
