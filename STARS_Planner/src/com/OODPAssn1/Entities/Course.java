package com.OODPAssn1.Entities;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <i>Course</i> is a Serializable container class that stores course information and indexes in the course.
 */
public class Course implements Serializable
{
    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;

    private String courseId;
    private String courseName;
    private String faculty;
    private List<TimeSlot> lecTimeSlotList;
    private List<Index> indexList = null;

    /**
     * Constructor that creates a <i>Course</i> object, requiring the Course ID, the Course Name and faculty of the Course
     * @param courseId Course ID number
     * @param courseName Course Name
     * @param faculty Faculty in which the course is held
     */
    public Course(String courseId, String courseName, String faculty)
    {
        this.courseId = courseId;
        this.courseName = courseName;
        this.faculty = faculty;
        lecTimeSlotList = new ArrayList<TimeSlot>();
        indexList = new ArrayList<Index>();
    }

    /**
     * Returns the course ID number.
     * @return Course ID number as a <i>String</i> object
     */
    public String getCourseId()
    {
        return courseId;
    }

    /**
     * Returns the course name.
     * @return Course name as a <i>String</i> object
     */
    public String getCourseName()
    {
        return courseName;
    }

    /**
     * Returns a List object containing the {@link Index} class that this course contains.
     * @return
     */
    public List<Index> getIndexList()
    {
        return indexList;
    }

    /**
     * Searches for an {@link Index} object in this course by its index number and returns it.
     * @param indexNum Index number to search for
     * @return {@link Index}
     */
    public Index getIndex(int indexNum)
    {
        for(int n = 0; n < indexList.size(); n++){
            if(indexList.get(n).getIndexNum() == indexNum)
                return indexList.get(n);
        }
        return null;
    }

    public List<TimeSlot> getLecTimeSlotList()
    {
        return lecTimeSlotList;
    }

    public String getFaculty()
    {
        return faculty;
    }




    //TODO = modify lecture time slot



    //Mutators
    public void setCourseName(String cName)
    {
        courseName = cName;
    }

    public void setCourseId(String cId)
    {
        courseId = cId;
    }


    public boolean addIndex(int indexNum, int maxNumOfStudetns)
    {
        return indexList.add(new Index(indexNum, maxNumOfStudetns));
    }

    public boolean deleteIndex(Index index)
    {
        return indexList.remove(index);
    }

    //TODO = modify index

    //-------Lecture time slot methods--------

    public boolean addLecTimeSlot(TimeSlot.DAY day, int startH, int startM, int endH, int endM, String location)
    {
        for(int i = 0; i < lecTimeSlotList.size(); i++){
            if(lecTimeSlotList.get(i).getDay() == day) {
                if (lecTimeSlotList.get(i).getStartTime().equals(LocalTime.of(startH, startM).toString()))
                    return false;
            }
        }
        return lecTimeSlotList.add(new TimeSlot(day, startH, startM, endH, endM, location, "LEC"));
    }

    public boolean deleteLectTimeSlot(TimeSlot dTimeSlot)
    {
        return lecTimeSlotList.remove(dTimeSlot);
    }

    public void setFaculty(String f)
    {
        faculty = f;
    }


}
