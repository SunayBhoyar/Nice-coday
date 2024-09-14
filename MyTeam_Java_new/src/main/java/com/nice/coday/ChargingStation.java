package com.nice.coday;

public class ChargingStation {
    private final String stationName;
    private final double distanceFromStart;

    // Constructor
    public ChargingStation(String stationName, double distanceFromStart) {
        this.stationName = stationName;
        this.distanceFromStart = distanceFromStart;
    }

    public String getStationName() {
        return stationName;
    }

    public double getDistanceFromStart() {
        return distanceFromStart;
    }

    @Override
    public String toString() {
        return "ChargingStation{" +
               "stationName='" + stationName + '\'' +
               ", distanceFromStart=" + distanceFromStart +
               '}';
    }

}
