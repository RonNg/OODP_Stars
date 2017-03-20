package com.OODPAssn1;

import java.util.List;

/**
 * Created by jonah on 15/3/2017.
 */
public class Student extends User {


    private int year;
    private List<String> indexNumberList;

    public Student(String idNum, String password, String name, char gender, int year, String nationality){

        super(idNum, password, name, gender, nationality,'s');
        this.year = year;

    }

    //TODO: Implement SET GET METHOD

}
