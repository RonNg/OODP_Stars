package com.OODPAssn1;

public class UI
{
    public static void main (String[] args)
    {
        StudentManager_obs studentMag = new StudentManager_obs();

//        studentMag.addStudent(new Student_obs("Ron", "c160144@e.ntu.edu.sg", "U1622393B", 12313, "male", "Singaporean", "user1", "pass"));
//        studentMag.addStudent(new Student_obs("Bob", "qh64@e.ntu.edu.sg", "U1644362B", 12313, "male", "Singaporean", "user2", "pass"));
//        studentMag.addStudent(new Student_obs("Qin Yao", "qy@e.ntu.edu.sg", "U164428B", 12313, "male", "Singaporean", "user3", "pass"));



        studentMag.printAll();
    }
}
