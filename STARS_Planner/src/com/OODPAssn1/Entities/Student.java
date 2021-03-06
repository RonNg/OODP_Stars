package com.OODPAssn1.Entities;


import java.util.List;

public class Student extends User
{
    public enum GENDER { MALE, FEMALE }
    public enum PROGRAMME { CE, CS, BU  }

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
    }

    //Accessors
    public String getName(){return this.name;}
    public String getMatricNo(){return this.matricNo;}


    //Mutator
    public void addCourseIndex(int courseIndex)
    {
    }


    public void deenrollCourseIndex()
    {
    }


}
