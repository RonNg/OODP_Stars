package com.OODPAssn1.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <i>Index</i> is a serializable class that contains the schedules of Tutorials and Labs associated with this index,
 * a list of students enrolled in this index and a waitlist of students waiting to enroll.
 */
public class Index implements Serializable
{

    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;

    private int indexNum;
    private int maxNumberOfStudent;
    private int numberOfStudent;
    private boolean handleWaitList; //To notify the CourseManager to add first index of waitlist into the index

    private List<String> studentsEnrolledList; // Stores matric number
    private List<TimeSlot> tutLabTimeSlotList; // Stores class times
    private List<String> studentWaitList; // Stores student matriculaton number

    public Index (int indexNum, int maxNumberOfStudent)
    {

        this.indexNum = indexNum;
        this.maxNumberOfStudent = maxNumberOfStudent;
        numberOfStudent = 0;

        handleWaitList = false;

        studentsEnrolledList = new ArrayList<String>();
        tutLabTimeSlotList = new ArrayList<TimeSlot>();
        studentWaitList = new ArrayList<String>();

    }

    /*=====================
           ACCESSORS
    =======================*/

    /**
     * @return Number of students in this index
     */
    public int getNumberOfEnrolledStudent ()
    {
        return numberOfStudent;
    }

    /**
     * @return Index number of this index
     */
    public int getIndexNum ()
    {
        return indexNum;
    }

    /**
     * @return Max number of students that this {@link Index} can accomodate
     */
    public int getMaxNumberOfStudent ()
    {
        return maxNumberOfStudent;
    }

    /**
     * @return the number of vacancies in this index
     */
    public int getNumberOfVacancy ()
    {
        return maxNumberOfStudent - numberOfStudent;
    }

    /**
     * @return a flag to check whether this index's waitlist must be handled <br>
     *         i.e. whether the first student in waitlist is to be added into index and an email be sent to him/her
     */
    public boolean checkIfHandleWaitlist ()
    {
        return handleWaitList;
    }

    /**
     * Checks whether a student is enrolled in this index by using his/her matriculation number
     * @param matricNo Matriculation number of student
     * @return <i>true </i>if student is enrolled in this index <br>
     *         <i>NOTE: If a student is in the waitlist of this index, this function will return false</i>
     *
     */
    public boolean checkIfStudentEnrolled (String matricNo)
    {
        for (int n = 0; n < studentsEnrolledList.size(); n++)
        {
            if (studentsEnrolledList.get(n).equalsIgnoreCase(matricNo))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @param matricNo Matriculation number to check
     * @return true if student is in the list <br>
     * false if student is not in the list
     */
    public boolean checkIfStudentInWaitList (String matricNo)
    {
        //TODO: Check if student in waitlist
        for (int i = 0; i < studentWaitList.size(); ++i)
        {
            if (studentWaitList.get(i).equalsIgnoreCase(matricNo))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @return Waitlist of this index
     */
    public List getWaitList ()
    {
        return studentWaitList;
    }

    /**
     * @return returns the Tutorial and Lab timelots for this index
     */
    public List<TimeSlot> getTutLabTimeSlotList ()
    {
        return tutLabTimeSlotList;
    }

    /**
     * @return returns list of enrolled students in this index
     */
    public List<String> getEnrolledStudentList ()
    {
        return studentsEnrolledList;
    }


    /*=====================
           MUTATOR
    =======================*/

    /**
     * @param indexNum Index number to change to
     */
    public void setIndexNumber (int indexNum)
    {
        this.indexNum = indexNum;
    }


    /**
     * @param matricNo Enrolling Student's matriculation number
     * @return Added into waitlist - -1 <br>
     * Successfully enrolled into index - 1 <br>
     * Error occured - 0
     */
    public int enrolStudent (String matricNo)
    {
        if (numberOfStudent >= maxNumberOfStudent)
        {
            if (studentWaitList.add(matricNo))
            {
                //Added into waitlist
                return -1;
            }
        }
        else if (studentsEnrolledList.add(matricNo))
        {
            numberOfStudent++;
            return 1; //Successfully enrolled into index
        }
        return 0; //error occured
    }


    /**
     * Sets the max number of student that this {@link Index} is able to accomodate
     * @param maxNumberOfStudent Number of max students to change
     * @return true if successful
     */
    public boolean setMaxNumberOfStudent (int maxNumberOfStudent)
    {
        if (maxNumberOfStudent < numberOfStudent)
        {
            return false;
        }
        this.maxNumberOfStudent = maxNumberOfStudent;
        return true;
    }

    /**
     * Deletes the specified timeslot from this Index
     * @param dTimeSlot timeslot to delete
     * @return true if successful
     */
    public boolean deleteTutLabTimeSlot (TimeSlot dTimeSlot)
    {
        return tutLabTimeSlotList.remove(dTimeSlot);
    }

    /**
     * Adds the timeslot for a tutorial into this index
     * @param day       Day of the tutorial
     * @param startH    Start time (hour) (in 24 hours) of this tutorial
     * @param startM    Start time (minutes) of this tutorial
     * @param endH      End time (hour) (in 24 hours) of this tutorial
     * @param endM      End time (minutes) of this tutorial
     * @param location  Location of the tutorial
     * @return true if tutorial is successfully added
     */
    public boolean addTutTimeSlot (TimeSlot.DAY day, int startH, int startM, int endH, int endM, String location)
    {
        return tutLabTimeSlotList.add(new TimeSlot(day, startH, startM, endH, endM, location, "TUT"));
    }

    /**
     * Adds the timeslot for a lab into this index
     * @param day       Day of the lab
     * @param startH    Start time (hour) (in 24 hours) of this lab
     * @param startM    Start time (minutes) of this lab
     * @param endH      End time (hour) (in 24 hours) of this lab
     * @param endM      End time (minutes) of this lab
     * @param location  Location of the lab
     * @return true if lab is successfully added
     */
    public boolean addLabTimeSlot (TimeSlot.DAY day, int startH, int startM, int endH, int endM, String location)
    {
        return tutLabTimeSlotList.add(new TimeSlot(day, startH, startM, endH, endM, location, "LAB"));
    }


    /**
     * Withdraws a student from this index
     * @param matricNo Matriculation number of the student
     * @param bypassWaitlist pass in FALSE if we want to handle the waitlist.
     * @return true if successful
     */
    public boolean withdrawStudent (String matricNo, boolean bypassWaitlist)
    {
        boolean success = studentsEnrolledList.remove(matricNo);

        if (success)
        {
            --numberOfStudent;
            if (getNumberOfVacancy() == 1 && !bypassWaitlist) //if when withdrawing from this index, the number of vacancies is exactly 1, it means it was previously full. Handle waitlist
            {
                //TODO: Handle waitlist here
                //Flag for handling waitlist. For Coursemanager use
                handleWaitList = true;
            }
        }
        return success;
    }

    /**
     * Removes student from the waitlist of this index
     * @param matricNo Matriculation number of the student
     * @return true if successful
     */
    public boolean removeStudentFromWaitlist (String matricNo)
    {
        return studentWaitList.remove(matricNo);
    }
}
