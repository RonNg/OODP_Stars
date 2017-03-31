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

    public Course(String courseId, String courseName, String faculty){
        this.courseId = courseId;
        this.courseName = courseName;
        this.faculty = faculty;
        indexNumberList = new ArrayList<Integer>();
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

    public void addIndex(Integer iNum){
        indexNumberList.add(iNum);
    }

    public boolean deleteIndex(Integer iNum){
        return indexNumberList.remove(iNum);
    }

    public List<Integer> getIndexNumberList(){
        return indexNumberList;
    }


}
