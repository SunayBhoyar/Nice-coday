package com.nice.coday;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ElectricityConsumptionCalculatorImpl implements ElectricityConsumptionCalculator {


    public List<ChargingStation> chargingStationData;
    public Map<String, Double> entryExitPointData;
    public Map<String, VehicleInfo> vehicleTypeData;
    public Map<String, Map<String, Double>> timeToChargeData;
    public List<TripDetails> tripDetails;
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
        return ans;
    }   
    @Override
    public ConsumptionResult calculateElectricityAndTimeConsumption(ResourceInfo resourceInfo) throws IOException {
        chargingStationData=CSVParser.readChargingStationData(resourceInfo.chargingStationInfoPath);
        entryExitPointData=CSVParser.readEntryExitPointData(resourceInfo.entryExitPointInfoPath);
        vehicleTypeData=CSVParser.readVehicleTypeData(resourceInfo.vehicleTypeInfoPath);
        Map<String, Double> stationMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(resourceInfo.chargingStationInfoPath.toString()))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] values = line.split(",");
                String stationName = values[0];
                double distanceFromStart = Double.parseDouble(values[1]);
                stationMap.put(stationName, distanceFromStart);
<<<<<<< Updated upstream
=======
                //amp
                result obj=new result();
                consumptionDetails.put(stationName,obj);
>>>>>>> Stashed changes
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
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] values = line.split(",");
                String vehicleName = values[0];
                result obj=new result();
                consumptionDetails.put(vehicleName,obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        for(TripDetails trip:tripDetails){
            double start = entryExitPointData.get(trip.getEntryPoint());
            double end = entryExitPointData.get(trip.getExitPoint());
            double currBattery=trip.getRemainingBatteryPercentage();
            String vehicleType=trip.getVehicleType();
            
            double mileage=vehicleTypeData.get(vehicleType).getMileage();
            double unitsTillNow=0D;
            long timeTillNow=0L;
            boolean flag=true;

            while(flag){
                double distanceTravellable=(mileage*currBattery)/100;
                String chargingStation=closestChargingStation(( long)distanceTravellable, (long)start);
                Double chargingStationDistance=0D;
<<<<<<< Updated upstream
                if((distanceTravellable + start) > end)
                {
                            result tempRes=consumptionDetails.get(vehicleType);
                            double units = tempRes.units;
                            long time = tempRes.time;
                            long TTrip = tempRes.trips;
                            units+=unitsTillNow;
                            time += timeTillNow;
                            TTrip += 1;
                            result temp=new result(units, time,TTrip);
                            consumptionDetails.put(vehicleType, temp);
=======
                
                System.out.print("Distance now "+distanceTravellable);

                if(distanceTravellable + start >=end){
                        // if (consumptionDetails.containsKey(vehicleType)) {
                            // Key exists, update the existing result
                            result existingResult = consumptionDetails.get(vehicleType);
                            consumptionDetails.get(vehicleType).trips++;
                            existingResult.units +=(end-init_start)*numberOfUnitsForFullyCharge/mileage; ;
                            existingResult.time += timeTillNow;
                            existingResult.trips += 1;
                            consumptionDetails.put(vehicleType, existingResult);
>>>>>>> Stashed changes
                            flag=false;
                            break;
                }
                else if(chargingStation.equals("-1"))
                {
                    result existingResult = consumptionDetails.get(vehicleType);
                            existingResult.units +=unitsTillNow;
                            existingResult.time += (long)timeTillNow;
                            consumptionDetails.put(vehicleType, existingResult);
                            flag=false;
                        break;
                }
                else{
                    chargingStationDistance=stationMap.get(chargingStation);
                    double batteryleft=(distanceTravellable+start-chargingStationDistance)*100/(mileage);
                    double chargableBattery=100-batteryleft;
                    double UnitsToCharge=chargableBattery*(vehicleTypeData.get(vehicleType).getNumberOfUnitsForFullyCharge())/(100);
                    double chargingSpeedPerUnit=timeToChargeData.get(vehicleType).get(chargingStation);
                    timeTillNow += (UnitsToCharge*chargingSpeedPerUnit);
                    unitsTillNow += UnitsToCharge;
                    totalChargingStationTime.put(chargingStation, totalChargingStationTime.getOrDefault(chargingStation, 0L) + (long) (UnitsToCharge*chargingSpeedPerUnit));
                    start=chargingStationDistance;
                    currBattery=100;
                }
                
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
// package com.nice.coday;

// import java.io.BufferedReader;
// import java.io.FileReader;
// import java.io.IOException;
// import java.math.BigDecimal;
// import java.math.RoundingMode;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// public class ElectricityConsumptionCalculatorImpl implements ElectricityConsumptionCalculator {

//     public List<ChargingStation> chargingStationData;
//     public Map<String, Double> entryExitPointData;
//     public Map<String, VehicleInfo> vehicleTypeData;
//     public Map<String, Map<String, Double>> timeToChargeData;
//     public List<TripDetails> tripDetails;

//     Map<String, result> consumptionDetails = new HashMap<>();
//     Map<String, Long> totalChargingStationTime = new HashMap<>();

//     public String closestChargingStation(long distance, long start) throws IOException {
//         int low = 0;
//         int high = chargingStationData.size() - 1;
//         String ans = "-1";
//         ChargingStation closestStation = null;

//         while (low <= high) {
//             int mid = (int) (low + (high - low) / 2);
//             ChargingStation currentStation = chargingStationData.get(mid);

//             if (currentStation.getDistanceFromStart() <= (start + distance)) {
//                 if (currentStation.getDistanceFromStart() > start) {
//                     closestStation = currentStation;
//                     ans = closestStation.getStationName();
//                 }
//                 low = mid + 1;
//             } else {
//                 high = mid - 1;
//             }
//         }
//         return ans;
//     }

//     @Override
//     public ConsumptionResult calculateElectricityAndTimeConsumption(ResourceInfo resourceInfo) throws IOException {
//         chargingStationData = CSVParser.readChargingStationData(resourceInfo.chargingStationInfoPath);
//         entryExitPointData = CSVParser.readEntryExitPointData(resourceInfo.entryExitPointInfoPath);
//         vehicleTypeData = CSVParser.readVehicleTypeData(resourceInfo.vehicleTypeInfoPath);
//         Map<String, Double> stationMap = new HashMap<>();

//         try (BufferedReader br = new BufferedReader(new FileReader(resourceInfo.chargingStationInfoPath.toString()))) {
//             String line;
//             boolean firstLine = true;

//             while ((line = br.readLine()) != null) {
//                 if (firstLine) {
//                     firstLine = false;
//                     continue;
//                 }
//                 String[] values = line.split(",");
//                 String stationName = values[0];
//                 double distanceFromStart = Double.parseDouble(values[1]);
//                 stationMap.put(stationName, distanceFromStart);
//                 totalChargingStationTime.put(stationName, 0L);
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }

//         timeToChargeData = CSVParser.readTimeToChargeData(resourceInfo.timeToChargeVehicleInfoPath);
//         tripDetails = CSVParser.readTripDetails(resourceInfo.tripDetailsPath);

//         ConsumptionResult result = new ConsumptionResult();

//         try (BufferedReader br = new BufferedReader(new FileReader(resourceInfo.vehicleTypeInfoPath.toString()))) {
//             String line;
//             boolean firstLine = true;

//             while ((line = br.readLine()) != null) {
//                 if (firstLine) {
//                     firstLine = false;
//                     continue;
//                 }
//                 String[] values = line.split(",");
//                 String vehicleName = values[0];
//                 result obj = new result();
//                 consumptionDetails.put(vehicleName, obj);
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }

//         for (TripDetails trip : tripDetails) {
//             double start = entryExitPointData.get(trip.getEntryPoint());
//             double end = entryExitPointData.get(trip.getExitPoint());
//             double currBattery = trip.getRemainingBatteryPercentage();
//             String vehicleType = trip.getVehicleType();
//             BigDecimal mileage = BigDecimal.valueOf(vehicleTypeData.get(vehicleType).getMileage());
//             BigDecimal unitsTillNow = BigDecimal.ZERO;
//             long timeTillNow = 0L;
//             boolean flag = true;

//             while (flag) {
//                 BigDecimal distanceTravellable = mileage.multiply(BigDecimal.valueOf(currBattery)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
//                 String chargingStation = closestChargingStation(distanceTravellable.longValue(), (long) start);
//                 BigDecimal chargingStationDistance = BigDecimal.ZERO;

//                 if ((distanceTravellable.doubleValue() + start) > end) {
//                     result tempRes = consumptionDetails.get(vehicleType);
//                     BigDecimal units = BigDecimal.valueOf(tempRes.units).add(unitsTillNow).setScale(2, RoundingMode.HALF_UP);
//                     long time = tempRes.time + timeTillNow;
//                     long TTrip = tempRes.trips + 1;

//                     result temp = new result(units.doubleValue(), time, TTrip);
//                     consumptionDetails.put(vehicleType, temp);
//                     flag = false;
//                     break;
//                 } else if (chargingStation.equals("-1")) {
//                     result existingResult = consumptionDetails.get(vehicleType);
//                     existingResult.units = BigDecimal.valueOf(existingResult.units).add(unitsTillNow).setScale(2, RoundingMode.HALF_UP).doubleValue();
//                     existingResult.time += timeTillNow;
//                     consumptionDetails.put(vehicleType, existingResult);
//                     flag = false;
//                     break;
//                 } else {
//                     chargingStationDistance = BigDecimal.valueOf(stationMap.get(chargingStation));
//                     BigDecimal batteryleft = BigDecimal.valueOf((distanceTravellable.doubleValue() + start - chargingStationDistance.doubleValue()) * 100 / mileage.doubleValue());
//                     BigDecimal chargableBattery = BigDecimal.valueOf(100).subtract(batteryleft);
//                     BigDecimal UnitsToCharge = chargableBattery.multiply(BigDecimal.valueOf(vehicleTypeData.get(vehicleType).getNumberOfUnitsForFullyCharge())).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
//                     BigDecimal chargingSpeedPerUnit = BigDecimal.valueOf(timeToChargeData.get(vehicleType).get(chargingStation));

//                     timeTillNow += UnitsToCharge.multiply(chargingSpeedPerUnit).longValue();
//                     unitsTillNow = unitsTillNow.add(UnitsToCharge).setScale(2, RoundingMode.HALF_UP);
//                     totalChargingStationTime.put(chargingStation, totalChargingStationTime.getOrDefault(chargingStation, 0L) + UnitsToCharge.multiply(chargingSpeedPerUnit).longValue());

//                     start = chargingStationDistance.doubleValue();
//                     currBattery = 100;
//                 }
//             }
//         }

//         result.setTotalChargingStationTime(totalChargingStationTime);
//         List<ConsumptionDetails> MainconsumptionDetails = new ArrayList<>();
//         consumptionDetails.forEach((key, value) -> {
//             MainconsumptionDetails.add(new ConsumptionDetails(key, value.units, value.time, value.trips));
//         });
//         result.setConsumptionDetails(MainconsumptionDetails);
//         return result;
//     }
// }
