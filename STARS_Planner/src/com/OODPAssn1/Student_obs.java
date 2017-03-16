package com.OODPAssn1;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student_obs implements Serializable
{
    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;

    //Basic Info
    private String name;
    private String matricNo;
    private String email;
    private int contact;
    private String gender;
    private String nationality;

    //Login Info
    private String username;
    private String password;

    //Courses info
    private List<Integer> courseIndexList; //Stores the courses that this student is taking

    public Student_obs(String name, String email, String matricNo,
                       int contact, String gender, String nationality,
                       String username, String password)
    {
        this.name = name; this.email = email; this.matricNo = matricNo;
        this.contact = contact; this.gender = gender; this.nationality = nationality;

        //@TODO
        this.username = "default";
        this.password = "password";
    }



    //Accessors
    public String getName(){return this.name;}
    public String getMatricNo(){return this.matricNo;}
    public String getEmail(){return this.email;}

    //Mutator
    public void addCourse(int courseIndex)
    {
        if(courseIndexList == null)
            courseIndexList = new ArrayList<>();

        courseIndexList.add(courseIndex);
    }





}