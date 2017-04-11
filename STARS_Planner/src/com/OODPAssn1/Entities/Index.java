package com.OODPAssn1.Entities;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Index implements Serializable {

    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;

    private int indexNum;
    private int maxNumberOfStudent;
    private int numberOfStudent;
    private boolean handleWaitList; //To notify the CourseManager to add first index of waitlist into the index

    private List<String> studentsEnrolledList; // Stores matric number
    private List<TimeSlot> tutLabTimeSlotList; // Stores class times
    private List<String> studentWaitList; // Stores student matriculaton number

    public Index(int indexNum, int maxNumberOfStudent) {

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
     * @return number of enrolled students in this index
     */
    public int getNumberOfEnrolledStudent() {
        return numberOfStudent;
    }

    /**
     * @return List of tutorial and lab {@link TimeSlot} in this index
     */
    public List<TimeSlot> getTutLabTimeSlotList() {
        return tutLabTimeSlotList;
    }

    /**
     * @return gets the index number of this index
     */
    public int getIndexNum() {
        return indexNum;
    }

    /**
     * @return get maximum numbers of student this index can accomodate
     */
    public int getMaxNumberOfStudent() {
        return maxNumberOfStudent;
    }

    /**
     * @return number of vacancies available in this index
     */
    public int getNumberOfVacancy() {
        return maxNumberOfStudent - numberOfStudent;
    }

    /**
     * @return true if waitlist must be handled
     */
    public boolean checkIfHandleWaitlist() {
        return handleWaitList;
    }

    /**
     * @return List of students in waitlist
     */
    public List getWaitList() {
        return studentWaitList;
    }

    /**
     * @return get list of enrolled students in the index
     */
    public List<String> getEnrolledStudentList() {
        return studentsEnrolledList;
    }

    /**
     * @param matricNo Matriculation number to check
     * @return true if student is enrolled int his index
     */
    public boolean checkIfStudentEnrolled(String matricNo) {
        for (int n = 0; n < studentsEnrolledList.size(); n++) {
            if (studentsEnrolledList.get(n).equalsIgnoreCase(matricNo)) {
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
    public boolean checkIfStudentInWaitList(String matricNo) {
        //TODO: Check if student in waitlist
        for (int i = 0; i < studentWaitList.size(); ++i) {
            if (studentWaitList.get(i).equalsIgnoreCase(matricNo)) {
                return true;
            }
        }
        return false;
    }


    /*=====================
           MUTATORS
    =======================*/

    /**
     * @param indexNum Index number to set
     */
    public void setIndexNumber(int indexNum) {
        this.indexNum = indexNum;
    }

    /**
     * @param maxNumberOfStudent set the maximum number of students that are able to enroll in this index
     * @return true if succesfully set
     */
    public boolean setMaxNumberOfStudent(int maxNumberOfStudent) {
        if (maxNumberOfStudent < numberOfStudent) {
            return false;
        }
        this.maxNumberOfStudent = maxNumberOfStudent;
        return true;
    }

    /**
     * @param dTimeSlot timeslot to delete
     * @return true if succesful
     */
    public boolean deleteTutLabTimeSlot(TimeSlot dTimeSlot) {
        return tutLabTimeSlotList.remove(dTimeSlot);
    }

    /**
     * Tutorial timeslot to delete in thie index
     *
     * @param day      Day of the tutorial
     * @param startH   Start time (hour) (in 24 hours) of this tutorial
     * @param startM   Start time (minutes) of this tutorial
     * @param endH     End time (hour) (in 24 hours) of this tutorial
     * @param endM     End time (minutes) of this tutorial
     * @param location Location of the tutorial
     * @return
     */
    public boolean addTutTimeSlot(TimeSlot.DAY day, int startH, int startM, int endH, int endM, String location) {
        for (int i = 0; i < tutLabTimeSlotList.size(); i++) {
            if (tutLabTimeSlotList.get(i).getDay() == day) {
                if (tutLabTimeSlotList.get(i).getStartTime().equals(LocalTime.of(startH, startM).toString()))
                    return false;
            }
        }

        return tutLabTimeSlotList.add(new TimeSlot(day, startH, startM, endH, endM, location, "TUT"));
    }

    /**
     * Lab timeslot to delete in thie index
     *
     * @param day      Day of the tutorial
     * @param startH   Start time (hour) (in 24 hours) of this lab
     * @param startM   Start time (minutes) of this lab
     * @param endH     End time (hour) (in 24 hours) of this lab
     * @param endM     End time (minutes) of this lab
     * @param location Location of the lab
     * @return
     */
    public boolean addLabTimeSlot(TimeSlot.DAY day, int startH, int startM, int endH, int endM, String location) {
        for (int i = 0; i < tutLabTimeSlotList.size(); i++) {
            if (tutLabTimeSlotList.get(i).getDay() == day) {
                if (tutLabTimeSlotList.get(i).getStartTime().equals(LocalTime.of(startH, startM).toString()))
                    return false;
            }
        }
        return tutLabTimeSlotList.add(new TimeSlot(day, startH, startM, endH, endM, location, "LAB"));
    }

    /**
     * @param matricNo Enrolling Student's matriculation number
     * @return Added into waitlist - -1 <br>
     * Successfully enrolled into index - 1 <br>
     * Error occured - 0
     */
    public int enrolStudent(String matricNo) {
        if (numberOfStudent >= maxNumberOfStudent) {
            if (studentWaitList.add(matricNo)) {
                //Added into waitlist
                return -1;
            }
        } else if (studentsEnrolledList.add(matricNo)) {
            numberOfStudent++;
            return 1; //Successfully enrolled into index
        }
        return 0; //error occured
    }

    /**
     * @param matricNo
     * @param bypassWaitlist pass in FALSE if we want to handle the waitlist.
     * @return
     */
    public boolean withdrawStudent(String matricNo, boolean bypassWaitlist) {
        boolean success = studentsEnrolledList.remove(matricNo);

        if (success) {
            --numberOfStudent;
            if (getNumberOfVacancy() == 1 && !bypassWaitlist) //if when withdrawing from this index, the number of vacancies is exactly 1, it means it was previously full. Handle waitlist
            {
                //TODO: Handle waitlist here
                //Flag for handling waitlist. For Coursemanager use
                handleWaitList = true;
            }else{
                handleWaitList = false;
            }
        }
        return success;
    }

    /**
     * @param matricNo matriculation number of the student to remove from the waitlist
     * @return true if succesfully removed
     */
    public boolean removeStudentFromWaitlist(String matricNo) {
        return studentWaitList.remove(matricNo);
    }


}
