package com.OODPAssn1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonah on 15/3/2017.
 */
 public class UserManager {

    private static List<User> userList; //User Database: Stores the references of all Users(Students & Admin)

    public static void addStudent( String idNum, String password, String name, char gender, int year, String nationality){

        if(userList == null)
            userList = new ArrayList();
        Student s = new Student(idNum, password, name, gender, year, nationality);
        userList.add(s);

    }

    public static int printAllUser()
    {
        if(userList == null)
        {
            System.out.println("printAll(): List is empty");
            return 0;
        }
        User temp;
        for(int i = 0; i < userList.size(); ++ i)
        {
            temp = userList.get(i);
            System.out.println(temp.getName() + "\n");
        }

        return 1;
    }

}
