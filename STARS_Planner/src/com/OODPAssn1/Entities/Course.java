package com.OODPAssn1.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonah on 15/3/2017.
 */
public class Course implements Serializable {

    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;

    private String courseId;
    private String courseName;
    private String faculty;
    private List<TimeSlot> lecTimeSlotList;
    private List<Index> indexList = null;

    public Course(String courseId, String courseName, String faculty)
    {
        this.courseId = courseId;
        this.courseName = courseName;
        this.faculty = faculty;
        lecTimeSlotList = new ArrayList<TimeSlot>();
        indexList = new ArrayList<Index>();
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

    public List<Index> getIndexList(){
        return indexList;
    }

    public boolean addIndex(int indexNum, int maxNumOfStudetns){
        return indexList.add(new Index(indexNum,maxNumOfStudetns));
    }

    public boolean deleteIndex(Index index){
        return indexList.remove(index);
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
