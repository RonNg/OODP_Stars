package com.OODPAssn1.Entities;


import java.time.LocalTime;

public class TimeSlot
{

    public enum DAY
    {
        MON, TUE, WED, THU, FRI, SAT, SUN
    };

    private DAY day; //
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String type;

    public TimeSlot(DAY day, int startH, int startM, int endH, int endM, String location, String type)
    {
        this.day = day;
        startTime = LocalTime.of(startH, startM);
        endTime = LocalTime.of(endH, endM);
        this.location = location;
        this.type = type;
    }

    //-------Day methods--------

    public void setDay(DAY day)
    {
        this.day = day;
    }

    public DAY getDay()
    {
        return day;
    }

    //-------Start Time methods--------

    public void setStartTime(int startH, int startM)
    {
        startTime = LocalTime.of(startH, startM);
    }

    public String getStartTime()
    {
        return startTime.toString();
    }

    //-------End Time methods--------

    public void setEndTIme(int endH, int endM)
    {
        endTime = LocalTime.of(endH, endM);
    }

    public String getEndTime()
    {
        return endTime.toString();
    }

    //-------Location methods--------

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }

    //-------Type methods--------

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

}
