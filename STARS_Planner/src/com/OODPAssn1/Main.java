package com.OODPAssn1;


import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tweakisher
 */
public class Main
{
    public static void main (String[] args)
    {
        StudentManager studentMag = new StudentManager();

//        studentMag.addStudent(new Student("Ron", "c160144@e.ntu.edu.sg", "U1622393B", 12313, "male", "Singaporean", "user1", "pass"));
//        studentMag.addStudent(new Student("Bob", "qh64@e.ntu.edu.sg", "U1644362B", 12313, "male", "Singaporean", "user2", "pass"));
//        studentMag.addStudent(new Student("Qin Yao", "qy@e.ntu.edu.sg", "U164428B", 12313, "male", "Singaporean", "user3", "pass"));



        studentMag.printAll();
    }
}
