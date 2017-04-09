package com.OODPAssn1.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public Index(int indexNum, int maxNumberOfStudent)
    {

        this.indexNum = indexNum;
        this.maxNumberOfStudent = maxNumberOfStudent;
        numberOfStudent = 0;

        handleWaitList = false;

        studentsEnrolledList = new ArrayList<String>();
        tutLabTimeSlotList = new ArrayList<TimeSlot>();
        studentWaitList = new ArrayList<String>();

    }

    //-------Index number methods--------

    public void setIndexNumber(int indexNum)
    {
        this.indexNum = indexNum;
    }



    //-------number of students methods--------

    public boolean setMaxNumberOfStudent(int maxNumberOfStudent)
    {
        if (maxNumberOfStudent < numberOfStudent)
        {
            return false;
        }
        this.maxNumberOfStudent = maxNumberOfStudent;
        return true;
    }



    //-------Time slot methods--------



    public boolean deleteTutLabTimeSlot(TimeSlot dTimeSlot)
    {
        return tutLabTimeSlotList.remove(dTimeSlot);
    }

    //-------Tutorial time slot methods--------

    public boolean addTutTimeSlot(TimeSlot.DAY day, int startH, int startM, int endH, int endM, String location)
    {
        return tutLabTimeSlotList.add(new TimeSlot(day, startH, startM, endH, endM, location, "TUT"));
    }

    //-------Lab time slot methods--------

    public boolean addLabTimeSlot(TimeSlot.DAY day, int startH, int startM, int endH, int endM, String location)
    {
        return tutLabTimeSlotList.add(new TimeSlot(day, startH, startM, endH, endM, location, "LAB"));
    }

    //-------Student registered methods--------


    /**
     *
     * @param matricNo Enrolling Student's matriculation number
     * @return Added into waitlist - -1 <br>
     *         Successfully enrolled into index - 1 <br>
     *         Error occured - 0
     */
    public int enrolStudent(String matricNo)
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
     *
     * @param matricNo
     * @param bypassWaitlist pass in FALSE if we want to handle the waitlist.
     * @return
     */
    public boolean withdrawStudent (String matricNo, boolean bypassWaitlist)
    {
        boolean success = studentsEnrolledList.remove(matricNo);

        if(success)
        {
            -- numberOfStudent;
            if(getNumberOfVacancy() == 1 && !bypassWaitlist) //if when withdrawing from this index, the number of vacancies is exactly 1, it means it was previously full. Handle waitlist
            {
                //TODO: Handle waitlist here
                //Flag for handling waitlist. For Coursemanager use
                handleWaitList = true;
            }
        }
        return success;
    }


    public boolean removeStudentFromWaitlist ( String matricNo )
    {
        return studentWaitList.remove(matricNo);
    }

    public boolean checkIfStudentEnrolled(String matricNo)
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
     *
     * @param matricNo Matriculation number to check
     * @return true if student is in the list <br>
     *         false if student is not in the list
     */
    public boolean checkIfStudentInWaitList ( String matricNo )
    {
        //TODO: Check if student in waitlist
        for(int i = 0; i < studentWaitList.size(); ++ i)
        {
            if(studentWaitList.get(i).equalsIgnoreCase(matricNo))
            {
                return true;
            }
        }
        return false;
    }

    //Accessors
    public int getNumberOfEnrolledStudent()
    {
        return numberOfStudent;
    }
    public List<TimeSlot> getTutLabTimeSlotList()
    {
        return tutLabTimeSlotList;
    }
    public int getIndexNum()
    {
        return indexNum;
    }
    public int getMaxNumberOfStudent()
    {
        return maxNumberOfStudent;
    }
    public int getNumberOfVacancy()
    {
        return maxNumberOfStudent - numberOfStudent;
    }
    public boolean checkIfHandleWaitlist () { return handleWaitList; }

    public List getWaitList(){return studentWaitList;}
    public List<String> getEnrolledStudentList(){return studentsEnrolledList;}

}
