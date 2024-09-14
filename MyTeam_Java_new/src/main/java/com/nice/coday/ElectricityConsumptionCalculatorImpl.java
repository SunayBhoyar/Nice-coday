package com.nice.coday;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ElectricityConsumptionCalculatorImpl implements ElectricityConsumptionCalculator {
    // public Map<String, Double> chargingStationData;
    // public Map<String, Double> entryExitPointData;
    // public Map<String, VehicleInfo> vehicleTypeData;
    // public Map<String, Map<String, Double>> timeToChargeData;
    // public List<TripDetails> tripDetails;

    public List<ChargingStation> chargingStationData;
    public Map<String, Double> entryExitPointData;
    public Map<String, VehicleInfo> vehicleTypeData;
    public Map<String, Map<String, Double>> timeToChargeData;
    public List<TripDetails> tripDetails;

    // imp
    Map<String,result> consumptionDetails = new HashMap<>();    
    Map<String, Long> totalChargingStationTime = new HashMap<>();
    public String closestChargingStation(long distance , long start) throws IOException {
        int low = 0;
        int high = chargingStationData.size() - 1;
        String ans="-1";
        ChargingStation closestStation = null;
        while (low <= high) {
            int mid = (int) (low + (high - low) / 2);
            ChargingStation currentStation = chargingStationData.get(mid);

            if (currentStation.getDistanceFromStart() <= (start + distance)) {
                if (currentStation.getDistanceFromStart() > start) {
                    closestStation = currentStation;
                    ans=closestStation.getStationName();
                }
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        // if (closestStation != null) {
        //     return closestStation.getStationName();
        // } else {
        //     return "-1";
        // }
        return ans;
    }   
    @Override
    public ConsumptionResult calculateElectricityAndTimeConsumption(ResourceInfo resourceInfo) throws IOException {
        chargingStationData=CSVParser.readChargingStationData(resourceInfo.chargingStationInfoPath);
        entryExitPointData=CSVParser.readEntryExitPointData(resourceInfo.entryExitPointInfoPath);
        // for (String key : entryExitPointData.keySet()) {
        
        //     Double value = entryExitPointData.get(key);
        //     System.out.println("Key: " + key + ", Value: " + value);
        // }
        vehicleTypeData=CSVParser.readVehicleTypeData(resourceInfo.vehicleTypeInfoPath);
        Map<String, Double> stationMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(resourceInfo.chargingStationInfoPath.toString()))) {
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

                // Assuming first column is station name and second is distance
                String stationName = values[0];
                double distanceFromStart = Double.parseDouble(values[1]);

                // Store the data in the map with station name as key and distance as value
                stationMap.put(stationName, distanceFromStart);
                totalChargingStationTime.put(stationName,0L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        timeToChargeData=CSVParser.readTimeToChargeData(resourceInfo.timeToChargeVehicleInfoPath);
        tripDetails=CSVParser.readTripDetails(resourceInfo.tripDetailsPath);

        ConsumptionResult result=new ConsumptionResult();

        try (BufferedReader br = new BufferedReader(new FileReader(resourceInfo.vehicleTypeInfoPath.toString()))) {
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

                // Assuming first column is station name and second is distance
                String vehicleName = values[0];
                //amp
                result obj=new result();
                consumptionDetails.put(vehicleName,obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        for(TripDetails trip:tripDetails){
            double start = entryExitPointData.get(trip.getEntryPoint());
            double init_start=start;
            double end = entryExitPointData.get(trip.getExitPoint());
            double currBattery=trip.getRemainingBatteryPercentage();
            String vehicleType=trip.getVehicleType();
            double mileage=vehicleTypeData.get(vehicleType).getMileage();
            int numberOfUnitsForFullyCharge=vehicleTypeData.get(vehicleType).getNumberOfUnitsForFullyCharge();
            
            // System.out.println(start+" "+end);
            // long distanceTillNow=(long) start;
            double unitsTillNow=0L,timeTillNow=0L;
            boolean flag=true;
            while(flag){
                double distanceTravellable=(mileage*currBattery)/100;
                String chargingStation=closestChargingStation(( long)distanceTravellable, (long)start);
                Double chargingStationDistance=0D;
                
                // System.out.print("Distance now "+distanceTravellable);
                if(distanceTravellable + start >=end){
                        // if (consumptionDetails.containsKey(vehicleType)) {
                            // Key exists, update the existing result
                            result tempRes=consumptionDetails.get(vehicleType);
                            double units = tempRes.units;
                            long time = tempRes.time;
                            long TTrip = tempRes.trips;
                            // consumptionDetails.get(vehicleType).trips++;
                            // if(start==init_start)
                            // units +=(end-init_start)*numberOfUnitsForFullyCharge/mileage
                            units+=unitsTillNow;
                            // else
                            // units +=(end-start)*numberOfUnitsForFullyCharge/mileage;
                            time += timeTillNow;
                            TTrip += 1;
                            result temp=new result(units, time,TTrip);
                            consumptionDetails.put(vehicleType, temp);
                            flag=false;
                            break;
                        // } else {
                        //     // Key does not exist, create a new result
                        //     result newResult = new result();
                        //     newResult.units = unitsTillNow;
                        //     newResult.time = timeTillNow;
                        //     // newResult.trips = 1;
                        //     // Add the new result to the map
                        //     consumptionDetails.put(vehicleType, newResult);
                        // }
                }
                else if(chargingStation.equals("-1"))
                {
                    result existingResult = consumptionDetails.get(vehicleType);
                            // consumptionDetails.get(vehicleType).trips++;
                            // existingResult.units +=(start-init_start)*numberOfUnitsForFullyCharge/mileage; 
                            existingResult.units +=unitsTillNow;
                            existingResult.time += timeTillNow;
                            consumptionDetails.put(vehicleType, existingResult);
                            flag=false;
                        break;
                }
                else{
                    // if(!chargingStation.equals("-1")) chargingStationDistance=stationMap.get(chargingStation);
                    // else{
                    //     result existingResult = consumptionDetails.get(vehicleType);
                    //         consumptionDetails.get(vehicleType).trips++;
                    //         // existingResult.units +=(start-init_start)*numberOfUnitsForFullyCharge/mileage; 
                    //         existingResult.units +=unitsTillNow;
                    //         existingResult.time += timeTillNow;
                    //         consumptionDetails.put(vehicleType, existingResult);
                    //         flag=false;
                    //     break;
                    // }
                    chargingStationDistance=stationMap.get(chargingStation);
                    double batteryleft=(distanceTravellable+start-chargingStationDistance)*100/(mileage);
                    double chargableBattery=100-batteryleft;
                    double UnitsToCharge=chargableBattery*(vehicleTypeData.get(vehicleType).getNumberOfUnitsForFullyCharge())/(100);
                    double chargingSpeedPerUnit=timeToChargeData.get(vehicleType).get(chargingStation);
                    timeTillNow+= (UnitsToCharge*chargingSpeedPerUnit);
                    unitsTillNow+=UnitsToCharge;
                    // totalChargingStationTime.put(chargingStation)=totalChargingStationTime.get(chargingStation)+(long)timeTillNow;
                    totalChargingStationTime.put(chargingStation, totalChargingStationTime.getOrDefault(chargingStation, 0L) + (long) (UnitsToCharge*chargingSpeedPerUnit));
                    //
                    start=chargingStationDistance;
                    currBattery=100;
                }
                // sunay func
                
            }
        }
        result.setTotalChargingStationTime(totalChargingStationTime);
        List<ConsumptionDetails> MainconsumptionDetails = new ArrayList<>();
        consumptionDetails.forEach((key, value) -> {
            MainconsumptionDetails.add(new ConsumptionDetails(key,value.units,value.time,value.trips));
        });
        result.setConsumptionDetails(MainconsumptionDetails);
        return result;
    }
}