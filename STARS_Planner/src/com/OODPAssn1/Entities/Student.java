package com.OODPAssn1.Entities;



import java.io.Serializable;

import java.util.ArrayList;

import java.util.List;

public class Student extends User implements Serializable
{
    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;

    public enum GENDER { MALE, FEMALE }

    private String matricNo;
    protected String name;
    protected int contact;
    protected GENDER gender;
    protected String nationality;

    private List<Integer> courseIndexList; //Stores the courses that this student is taking

    public Student(String name, String email, String matricNo,
				   int contact, GENDER gender, String nationality,
				   String username, String password)
    {
        this.name = name; this.email = email; this.matricNo = matricNo;
        this.contact = contact; this.gender = gender; this.nationality = nationality;

        this.username = username;
        this.password = password;

        type = USER_TYPE.STUDENT;

        courseIndexList = new ArrayList<Integer>();
    }

    //Accessors
    public String getName(){return this.name;}
    public String getMatricNo(){return this.matricNo;}
    public String getGender(){return this.gender.toString();}
    public String getNationality(){return this.nationality;}
    public List<Integer> getCourseIndexList(){return courseIndexList;}



    //Mutator
    public void addCourseIndex(int courseIndex)
    {
        courseIndexList.add(courseIndex);
    }



    public void deEnrollCourseIndex(int indexNo)
    {
        for(int j =0; j < courseIndexList.size(); j++){

            if(courseIndexList.get(j) == indexNo)
                courseIndexList.remove(j);
        }

    }



}
