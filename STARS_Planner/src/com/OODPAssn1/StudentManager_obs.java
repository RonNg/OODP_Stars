package com.OODPAssn1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
public class StudentManager_obs
{
    public final static String STUDENT_PATH = "database/student.dat";
    private List<Student_obs> studentList = null;

    private boolean isInitAlready = false;

    public StudentManager_obs() { init(); }

    public int init ()
    {
        if(isInitAlready)
        {
            System.out.println("StudentManager_obs is being reinitialised again!");
            return -1;
        }

        File file = new File(STUDENT_PATH);
        try
        {
            /*
                Returns:
                true if the named file does not exist and was successfully created;
                false if the named file already exists
            */
            if(file.createNewFile())
                System.out.println("Student_obs Database was created");
            else
                System.out.println("Student_obs Database already exists.");

            //Initialise our studentList here
            studentList = (ArrayList)this.readSerializedObject();

            if(studentList == null)
                studentList = new ArrayList();
        }
        catch (IOException i)
        {
            return -1;
        }
        return 1;
    }

    /**
     * Retrieve List index of student byname
     * @return
     */
    public int retrieve(String name)
    {
        Student_obs temp = null;
        for(int i = 0; i < studentList.size(); ++ i)
        {
            temp = (Student_obs)studentList.get(i);

            if(name == temp.getName())
            {
                return i;
            }
        }

        //Could not be found
        return -1;
    }

    public int printAll()
    {
        if(studentList == null)
        {
            System.out.println("printAll(): List is empty");
            return 0;
        }

        Student_obs temp = null;
        for(int i = 0; i < studentList.size(); ++ i)
        {
            temp = (Student_obs)studentList.get(i);
            System.out.println(temp.getName() + "\n");
        }

        return 1;
    }

    public void addStudent(final Student_obs student)
    {
        try
        {
            studentList.add(student);
        }
        catch (Exception e)
        {
            System.out.println("addStudent() Exception caught: " + e.getMessage());
        }
    }

    public void editStudent(int index)
    {
        //studentList.get(index).addCourse();
    }

    public static List readSerializedObject()
    {
        //Checks to see if the file is empty.
        //if it is, return a null studentList.
        File file = new File(STUDENT_PATH);
        if(file.length() == 0)
            return null;

        List pDetails = null;
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try
        {
            fis = new FileInputStream(STUDENT_PATH);
            in = new ObjectInputStream(fis);
            pDetails = (ArrayList) in.readObject();
            in.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        // print out the size
        //System.out.println(" Details Size: " + pDetails.size());
        //System.out.println();
        return pDetails;
    }

    public void save()
    {
        if(this.studentList != null)
        {
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            try
            {
                fos = new FileOutputStream(STUDENT_PATH);
                out = new ObjectOutputStream(fos);

                out.writeObject(studentList); //Write entire studentList into file

                out.close();

            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public List getList()
    {
        return studentList;
    }

}
