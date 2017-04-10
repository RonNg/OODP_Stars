package com.OODPAssn1.Entities;


import java.io.Serializable;

import java.util.ArrayList;

import java.util.List;

/**
 * <i>Student</i> is a serializable class containing student information such as matriculation number, email, contact number
 */
public class Student extends User implements Serializable
{
    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;

    public enum GENDER
    {
        MALE, FEMALE
    }

    private String matricNo;
    protected String name;
    protected int contact;
    protected GENDER gender;
    protected String nationality;

    private List<Integer> courseIndexList; //Stores the courses that this student is taking

    public Student (String name, String email, String matricNo,
                    int contact, GENDER gender, String nationality,
                    String username, String password)
    {
        this.name = name;
        this.email = email;
        this.matricNo = matricNo;
        this.contact = contact;
        this.gender = gender;
        this.nationality = nationality;

        this.username = username;
        this.password = password;

        type = USER_TYPE.STUDENT;

        courseIndexList = new ArrayList<Integer>();
    }

    /*=====================
           ACCESSOR
    =======================*/

    /**
     * @return Name of student
     */
    public String getName ()
    {
        return this.name;
    }

    /**
     * @return Matriculation number of student
     */
    public String getMatricNo ()
    {
        return this.matricNo;
    }

    /**
     * @return Gender of student
     */
    public String getGender ()
    {
        return this.gender.toString();
    }

    /**
     * @return Nationality of student
     */
    public String getNationality ()
    {
        return this.nationality;
    }

    /**
     * @return returns the list of indexes this student is enrolled in
     */
    public List<Integer> getCourseIndexList ()
    {
        return courseIndexList;
    }


    /*=====================
            MUTATOR
    =======================*/

    /**
     * @param courseIndex index to add to this student's list of enrolled indexes
     */
    public void addCourseIndex (int courseIndex)
    {
        courseIndexList.add(courseIndex);
    }

    /**
     * @param indexNo index number to remove from this student's index list
     */
    public boolean removeCourseIndex (int indexNo)
    {
        for (int j = 0; j < courseIndexList.size(); j++)
        {

            if (courseIndexList.get(j) == indexNo)
            {
                courseIndexList.remove(j);
                return true;
            }
        }
        return false;
    }
}
