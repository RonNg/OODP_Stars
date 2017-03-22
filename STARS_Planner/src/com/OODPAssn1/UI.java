package com.OODPAssn1;

import com.OODPAssn1.Entities.Student;
import com.OODPAssn1.Managers.UserManager;

public class UI
{
    public static void main (String[] args)
    {
        //Example Code

        //UserManager.getInstance().addStudent( new Student("Ron", "c160144@e.ntu", "U1622393B", 93874270, Student.GENDER.MALE, "Singaporean", "c160144", "password"));
        //UserManager.getInstance().saveData(); //This method automatically encrypts the password when saving


        UserManager.getInstance().print();




        //TODO: Method to check if a password is encrypted

    }
}
