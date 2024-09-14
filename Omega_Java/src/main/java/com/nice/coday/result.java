package com.nice.coday;

public class result {

    // Data members
    public double units;
    public long time;
    public long trips;

    // Constructor
    public result(){
        units=0;
        time=0L;
        trips=0;
    }

    // Overloaded constructor
    public result(double units, long time, long trips)
    {
        this.units=units;
        this.time=time;
        this.trips=trips;
    }
}
