package com.OODPAssn1.Entities;


import java.time.LocalTime;

public class TimeSlot {

    private char day; //
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String type;

    public TimeSlot(char day, int startH, int startM, int endH, int endM, String location, String type){

    }

    //-------Day methods--------

    public void setDay(char day){
        this.day = day;
    }

    public char getDay(){
        return day;
    }

    //-------Start Time methods--------

    public void setStartTime(int startH, int startM){
        startTime = LocalTime.of(startH,startM);
    }

    public String getStartTime(){
        return startTime.toString();
    }

    //-------End Time methods--------

    public void setEndTIme(int endH, int endM){
        endTime = LocalTime.of(endH, endM);
    }

    public String getEndTime(){
        return endTime.toString();
    }

    //-------Location methods--------

    public void setLocation(String location){
        this.location = location;
    }

    public String getLocation(){
        return location;
    }

    //-------Type methods--------

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

}
