package com.OODPAssn1.Entities;

import java.util.ArrayList;
import java.util.List;

public class Index {

    private int indexNum;
    private int maxNumberOfStudent;
    private int numberOfStudent;
    private List<String> studentsEnrolledList; // Stores matrix number
    private List<TimeSlot> tutLabTimeSlotList; // Stores class times
    private List<String> studentWaitList; // Stores wait list

    public Index(int indexNum, int maxNumberOfStudent){
        this.indexNum = indexNum;
        this.maxNumberOfStudent = maxNumberOfStudent;
        numberOfStudent = 0;
        studentsEnrolledList = new ArrayList<String>();
        tutLabTimeSlotList = new ArrayList<TimeSlot>();
        studentWaitList = new ArrayList<String>();
    }

    //-------Index number methods--------

    public void setIndexNumber(int indexNum){
        this.indexNum = indexNum;
    }

    public int getIndexNum(){
        return indexNum;
    }

    //-------number of students methods--------

    public boolean setMaxNumberOfStudent(int maxNumberOfStudent){
        if(maxNumberOfStudent<numberOfStudent){
            return false;
        }
        this.maxNumberOfStudent = maxNumberOfStudent;
        return true;
    }

    public int getMaxNumberOfStudent(){
        return maxNumberOfStudent;
    }

    public int getNumberOfVacancy(){
        return maxNumberOfStudent = numberOfStudent;
    }

    //-------Time slot methods--------

    public List<TimeSlot> getTutLabTimeSlotList(){
        return tutLabTimeSlotList;
    }

    public boolean deleteTutLabTimeSlot(TimeSlot dTimeSlot){
        return tutLabTimeSlotList.remove(dTimeSlot);
    }

    //-------Tutorial time slot methods--------

    public boolean addTutTimeSlot(char day, int startH, int startM, int endH, int endM, String location){
        return tutLabTimeSlotList.add(new TimeSlot(day,startH,startM,endH,endM,location,"TUT"));
    }

    //-------Lab time slot methods--------

    public boolean addLabTimeSlot(char day, int startH, int startM, int endH, int endM, String location){
        return tutLabTimeSlotList.add(new TimeSlot(day,startH,startM,endH,endM,location,"LAB"));
    }

    //-------Student registered methods--------

    public int getNumberOfEnrolledStudent(){
        return numberOfStudent;
    }

    public int enrolStudent(String matricNo){
        if(numberOfStudent>=maxNumberOfStudent){
            if(studentWaitList.add(matricNo)){
                return -1;
            }
                return 0;
        }else if(studentsEnrolledList.add(matricNo)){
            numberOfStudent++;
            return 1;
        }

        return 0;
    }

    public String withdrawStudent(String matricNo){
        if(studentsEnrolledList.remove(matricNo)){
            numberOfStudent--;
            if((numberOfStudent + 1) == maxNumberOfStudent){
                String toEnroll = studentWaitList.get(0);
                if(enrolStudent(toEnroll)==1){
                    studentWaitList.remove(0);
                    return toEnroll;
                }
                return "ERROR";
            }
            return "SUCCESS";
        }
        return "ERROR";
    }

}
