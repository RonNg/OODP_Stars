package com.OODPAssn1.Entities;


import java.io.Serializable;
import java.time.LocalTime;

/**
 * The TimeSlot class is a container that stores start and end time as well as location of either a tutorial or lab
 */
public class TimeSlot implements Serializable {

    private DAY day; //
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String type;
    /**
     * @param day      Day in which the lec/lab//tut will be held
     * @param startH   Start time (hour) (in 24 hours) of this lec/lab/tut
     * @param startM   Start time (minutes) of this lec/lab/tut
     * @param endH     End time (hour) (in 24 hours) of this lec/lab/tut
     * @param endM     End time (minutes) of this lec/lab/tut
     * @param location Location of the lec/lab/tut
     */
    public TimeSlot(DAY day, int startH, int startM, int endH, int endM, String location, String type) {
        this.day = day;
        startTime = LocalTime.of(startH, startM);
        endTime = LocalTime.of(endH, endM);
        this.location = location;
        this.type = type;
    }

    /**
     * @return The day of the lec/lab/tut
     */
    public DAY getDay() {
        return day;
    }

    /**
     * @param day Day of the lec/lab/tut
     */
    public void setDay(DAY day) {
        this.day = day;
    }

    /**
     * @return Location of the lec/lab/tut
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location Location of the lec/lab/tut
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return Start time in String of the lec/lab/tut
     */
    public String getStartTime() {
        return startTime.toString();
    }

    /**
     * @return End time in String of the lec/lab/tut
     */
    public String getEndTime() {
        return endTime.toString();
    }


    /*=====================
            MUTATOR
     =======================*/

    /**
     * @return The type of event this is (lec, lab or tut)
     */
    public String getType() {
        return type;
    }

    /**
     * @param type Set the type of this TimeSlot (is it a lec, lab or tut)
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param startH Starting hour of the lec/lab/tut in 24hrs format
     * @param startM Starting minutes of the lec/lab/tut <br>
     *               Example if the start time is 1430hrs, startH should be 14 and startM should be 30
     */
    public void setStartTime(int startH, int startM) {
        startTime = LocalTime.of(startH, startM);
    }

    /**
     * @param endH Ending hour of the lec/lab/tut in 24hrs format
     * @param endM Ending minutes of the lec/lab/tut <br>
     *             Example if the end time is 1430hrs, endH should be 14 and endM should be 30
     */
    public void setEndTIme(int endH, int endM) {
        endTime = LocalTime.of(endH, endM);
    }

    public enum DAY {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }
}
