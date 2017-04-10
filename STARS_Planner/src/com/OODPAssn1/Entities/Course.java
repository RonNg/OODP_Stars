package com.OODPAssn1.Entities;

import java.io.Serializable;
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

    /*=====================
           ACCESSORS
    =======================*/

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

    /**
     * Get the list of lecture timeslots in this {@link Course}
     * @return {@link List} of TimeSlot objects of this {@link Course}
     */
    public List<TimeSlot> getLecTimeSlotList()
    {
        return lecTimeSlotList;
    }


    /**
     * Get the faculty of this Course
     * @return Faculty in which this Course belongs to
     */
    public String getFaculty()
    {
        return faculty;
    }


    /*=====================
           MUTATORS
    =======================*/

    /**
     * Sets the Course name
     * @param cName Course name
     */
    public void setCourseName(String cName)
    {
        courseName = cName;
    }

    /**
     * Sets the Course ID
     * @param cId Course ID
     */
    public void setCourseId(String cId)
    {
        courseId = cId;
    }


    /**
     * Adds an index to this Course
     * @param indexNum Unique index number of the index
     * @param maxNumOfStudents Maximum number of students that this index can accomodate
     * @return true if index is added
     */
    public boolean addIndex(int indexNum, int maxNumOfStudents)
    {
        return indexList.add(new Index(indexNum, maxNumOfStudents));
    }

    /**
     * Deletes an index from this Course
     * @param index {@link Index} object to remove
     * @return true if index is removed
     */
    public boolean deleteIndex(Index index)
    {
        return indexList.remove(index);
    }

    /**
     * Adds a lecture time slot into this Course
     * @param day       Day of the lecture
     * @param startH    Start time (hour) (in 24 hours) of this lecture
     * @param startM    Start time (minutes) of this lecture
     * @param endH      End time (hour) (in 24 hours) of this lecture
     * @param endM      End time (minutes) of this lecture
     * @param location  Location of the lecture
     * @return true if lecture is successfully added
     */
    public boolean addLecTimeSlot(TimeSlot.DAY day, int startH, int startM, int endH, int endM, String location)
    {
        return lecTimeSlotList.add(new TimeSlot(day, startH, startM, endH, endM, location, "LEC"));
    }

    /**
     * Deletes the lecture occupying the specified timeslot
     * @param dTimeSlot Timeslot of the lecture to delete
     * @return true if succesfully deleted
     */
    public boolean deleteLectTimeSlot(TimeSlot dTimeSlot)
    {
        return lecTimeSlotList.remove(dTimeSlot);
    }

    /**
     * Sets faculty name of this Course
     * @param f Faculty name
     */
    public void setFaculty(String f)
    {
        faculty = f;
    }


}
