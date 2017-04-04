package com.OODPAssn1.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonah on 15/3/2017.
 */
public class Course {

    private String courseId;
    private String courseName;
    private String faculty;
    private List<Integer> indexNumberList;
    private List<TimeSlot> lecTimeSlotList;

    public Course(String courseId, String courseName, String faculty){
        this.courseId = courseId;
        this.courseName = courseName;
        this.faculty = faculty;
        indexNumberList = new ArrayList<Integer>();
        lecTimeSlotList = new ArrayList<TimeSlot>();
    }

    //-------Course ID methods--------

    public void setCourseId(String cId){
        courseId = cId;
    }

    public String getCourseId(){
        return courseId;
    }

    //-------Course Name methods--------

    public void setCourseName(String cName){
        courseName = cName;
    }

    public String getCourseName(){
        return courseName;
    }

    //-------Course Faculty methods--------

    public void setFaculty(String f){
        faculty = f;
    }

    public String getFaculty(){
        return faculty;
    }

    //-------Course Indexes methods--------

    public boolean addIndex(Integer iNum){
        return indexNumberList.add(iNum);
    }

    public boolean deleteIndex(Integer iNum){
        return indexNumberList.remove(iNum);
    }

    public List<Integer> getIndexNumberList(){
        return indexNumberList;
    }

    //TODO = modify index

    //-------Lecture time slot methods--------

    public boolean addlecTimeSlot(TimeSlot.DAY day, int startH, int startM, int endH, int endM, String location){
        return lecTimeSlotList.add(new TimeSlot(day,startH,startM,endH,endM,location,"LECT"));
    }

    public boolean deleteLectTimeSlot(TimeSlot dTimeSlot){
        return lecTimeSlotList.remove(dTimeSlot);
    }

    public List<TimeSlot> getlecTimeSlotList(){
        return lecTimeSlotList;
    }

    //TODO = modify & delete lecture time slot



}
