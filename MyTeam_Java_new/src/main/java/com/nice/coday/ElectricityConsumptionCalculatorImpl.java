package com.nice.coday;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ElectricityConsumptionCalculatorImpl implements ElectricityConsumptionCalculator {
    

    public List<ChargingStation> chargingStationData;
    public Map<String, Integer> stationIndexMap ;
    public Map<String, Double> entryExitPointData;
    public Map<String, VehicleInfo> vehicleTypeData;
    public Map<String, Map<String, Double>> timeToChargeData;
    public List<TripDetails> tripDetails;

    public Map<String, Integer> getStationNameIndexMap() {
        Map<String, Integer> stationIndexMap = new HashMap<>();

        for (int i = 0; i < chargingStationData.size(); i++) {
            ChargingStation station = chargingStationData.get(i);
            stationIndexMap.put(station.getStationName(), i); 
        }

        return stationIndexMap;
    }
    public List<ConsumptionDetails> createConsumptionDetailsList() {
        List<ConsumptionDetails> consumptionDetailsList = new ArrayList<>();
        for (String vehicleType : vehicleTypeData.keySet()) {
            ConsumptionDetails consumptionDetails = new ConsumptionDetails(
                vehicleType,    
                0.0,          
                0L,        
                0L   
            );
            consumptionDetailsList.add(consumptionDetails);
        }
        return consumptionDetailsList;
    }

    public String closestChargingStation(long distanceTravellable , long startPosition) throws IOException {
        int low = 0;
        int high = chargingStationData.size() - 1;

        ChargingStation closestStation = null;
        while (low <= high) {
            int mid = (int) (low + (high - low) / 2);
            ChargingStation currentStation = chargingStationData.get(mid);

            if (currentStation.getDistanceFromStart() <= (startPosition + distanceTravellable)) {
                if (currentStation.getDistanceFromStart() >= startPosition) {
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
            return "-1";
        }
    }   

    @Override
    public ConsumptionResult calculateElectricityAndTimeConsumption(ResourceInfo resourceInfo) throws IOException {
        ConsumptionResult result  = new ConsumptionResult() ; 

        // read the CSV file 
        chargingStationData=CSVParser.readChargingStationData(resourceInfo.chargingStationInfoPath);
        entryExitPointData=CSVParser.readEntryExitPointData(resourceInfo.entryExitPointInfoPath);
        vehicleTypeData=CSVParser.readVehicleTypeData(resourceInfo.vehicleTypeInfoPath);
        timeToChargeData=CSVParser.readTimeToChargeData(resourceInfo.timeToChargeVehicleInfoPath);
        tripDetails=CSVParser.readTripDetails(resourceInfo.tripDetailsPath);

        stationIndexMap = getStationNameIndexMap() ; 

        // output file
        List<ConsumptionDetails> consumptionDetails = createConsumptionDetailsList();
        Map<String, Long> totalChargingStationTime = new HashMap<>();


        for(TripDetails trip:tripDetails){

            double start = entryExitPointData.get(trip.getEntryPoint());
            double end = entryExitPointData.get(trip.getExitPoint());

            double currBattery=trip.getRemainingBatteryPercentage();
            String vehicleType=trip.getVehicleType();
            double mileage=vehicleTypeData.get(vehicleType).getMileage();

            int numberOfUnitsForFullyCharge=vehicleTypeData.get(vehicleType).getNumberOfUnitsForFullyCharge();

            long unitsTillNow=0L,timeTillNow=0L;
            boolean flag=true;

            while(flag){
                double distanceTravellable=(mileage*currBattery)/100;
                if(distanceTravellable + start >= end){
                        consumptionDetails.get(vehicleType).trips++;
                        existingResult.units += unitsTillNow;
                        existingResult.time += timeTillNow;
                        existingResult.trips += 1;
                        flag=false;
                    }
                }
            //     long chargingStationDistance=0L;
                
            //     System.out.print("Distance now "+distanceTravellable);
            //     String chargingStation=closestChargingStation(( long)distanceTravellable, (long)start);
                
            //     // else{
            //     //     if(!chargingStation.equals("-1")) chargingStationData.get(stationIndexMap.get(chargingStation)).getStationName();
            //     //     else{
            //     //         break;
            //     //     }
            //     //     double batteryleft=(distanceTravellable+start-chargingStationDistance)*100/(mileage);
            //     //     double chargableBattery=100-batteryleft;
            //     //     double UnitsToCharge=100*(vehicleTypeData.get(vehicleType).getNumberOfUnitsForFullyCharge())/(chargableBattery);
            //     //     double chargingSpeedPerUnit=timeToChargeData.get(vehicleType).get(chargingStation);
            //     //     timeTillNow+= (UnitsToCharge*chargingSpeedPerUnit);
            //     //     unitsTillNow+=UnitsToCharge;

            //     //     //
            //     //     start=chargingStationDistance;
            //     //     currBattery=100;
            //     // }
                
            }
        }

        return result;
        
    }
}
