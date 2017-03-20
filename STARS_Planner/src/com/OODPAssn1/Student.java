package com.OODPAssn1;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student extends User
{
    //Courses info
    private List<Integer> courseIndexList; //Stores the courses that this student is taking

    public Student(String name, String email, String matricNo,
				   int contact, GENDER gender, String nationality,
				   String username, String password)
    {
        this.name = name; this.email = email; this.matricNo = matricNo;
        this.contact = contact; this.gender = gender; this.nationality = nationality;

        this.username = username;
        this.password = password;
    }

    //Accessors
    public String getName(){return this.name;}
    public String getMatricNo(){return this.matricNo;}
    public String getEmail(){return this.email;}

    //Mutator
    public void addCourse(int courseIndex)
    {
    }





}
