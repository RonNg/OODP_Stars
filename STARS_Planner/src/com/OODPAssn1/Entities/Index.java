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
    private List<String> studentsEnrolledList; // Stores matrix number
    private List<TimeSlot> tutLabTimeSlotList; // Stores class times
    private List<String> studentWaitList; // Stores wait list

    public Index(int indexNum, int maxNumberOfStudent)
    {

        this.indexNum = indexNum;
        this.maxNumberOfStudent = maxNumberOfStudent;
        numberOfStudent = 0;

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
        return 0;
    }

    public String withdrawStudent(String matricNo)
    {
        if (studentsEnrolledList.remove(matricNo))
        {
            numberOfStudent--;
            if ((numberOfStudent + 1) == maxNumberOfStudent)
            {
                String toEnroll = studentWaitList.get(0);
                if (enrolStudent(toEnroll) == 1)
                {
                    studentWaitList.remove(0);
                    return toEnroll;
                }
                return "ERROR";
            }
            return "SUCCESS";
        }
        return "ERROR";
    }

    public boolean checkIfStudentEnrolled(String matricNo)
    {
        for (int n = 0; n < studentsEnrolledList.size(); n++)
        {
            if (studentsEnrolledList.get(n).equals(matricNo))
            {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfStudentInWaitList ( String matricNo )
    {
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
    public List getWaitList(){return studentWaitList;}

}
