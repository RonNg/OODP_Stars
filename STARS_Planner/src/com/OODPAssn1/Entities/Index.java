package com.OODPAssn1.Entities;

import java.util.ArrayList;
import java.util.List;

public class Index {

    private int indexNum;
    private int maxNumberOfStudent;
    private int numberOfStudent;
    private List<String> studentsRegisteredList; // Stores matrix number
    private List<TimeSlot> tutLabTimeSlotList; // Stores class times

    public Index(int indexNum, int maxNumberOfStudent){
        this.indexNum = indexNum;
        this.maxNumberOfStudent = maxNumberOfStudent;
        numberOfStudent = 0;
        studentsRegisteredList = new ArrayList<String>();
        tutLabTimeSlotList = new ArrayList<TimeSlot>();
    }

    //-------Index number methods--------

    public void setIndexNumber(int indexNum){
        this.indexNum = indexNum;
    }

    public int getIndexNum(){
        return indexNum;
    }

    //-------Max number of students methods--------

    public void setMaxNumberOfStudent(int maxNumberOfStudent){
        this.maxNumberOfStudent = maxNumberOfStudent;
    }

    public int getMaxNumberOfStudent(){
        return maxNumberOfStudent;
    }




}
