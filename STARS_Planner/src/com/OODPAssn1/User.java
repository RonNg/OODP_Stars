package com.OODPAssn1;

/**
 * Created by jonah on 15/3/2017.
 */
public abstract class User {

    private String idNum;
    private String password;
    private String name;
    private char gender; // M = Male, F = Female
    private String nationality;
    private char type; // S = Student A = Admin


    public User(String idNum, String password, String name, char gender, String nationality, char type){

        this.idNum = idNum;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.nationality = nationality;
        this.type = type;

    }

    //TODO: Implement SET GET METHOD
    public String getName(){

        return name;

    }

}
