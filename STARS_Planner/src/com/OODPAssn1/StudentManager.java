package com.OODPAssn1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is incharge of handling everything {@link Student} related including encryption of the password.
 *
 * @author Tweakisher
 */
public class StudentManager extends DataManager
{
    public final static String STUDENT_PATH = "database/student.dat";
    private List<Student> studentList = null;

    private boolean isInitAlready = false;

    public StudentManager() { init(); }

    public int init ()
    {
        if(isInitAlready)
        {
            System.out.println("StudentManager is being reinitialised again!");
            return -1;
        }

        File file = new File(STUDENT_PATH);
        try
        {

            // createNewFile() returns...
            // true if the named file does not exist and was successfully created;
            // false if the named file already exists

            if(file.createNewFile())
                System.out.println("Student Database was created");
            else
                System.out.println("Student Database already exists.");

            //Initialise our studentList here
            studentList = (ArrayList)this.read(STUDENT_PATH);

            if(studentList == null)
                studentList = new ArrayList();
        }
        catch (IOException i)
        {
            return -1;
        }


        //Set Encryption Key
        this.setEncryptionKey("StudentKStudentK");
        this.setIV("NotPokemonIVFour");

        return 1;
    }

    /**
     * Searches {@link List} for {@link Student} by name and returns the {@link List} index if matched
     * @return index of the student if found in List.
     *         <p>returns -1 if student could not be found</p>
     */
    public int find(String name)
    {
        Student temp = null;
        for(int i = 0; i < studentList.size(); ++ i)
        {
            temp = (Student)studentList.get(i);

            if(name == temp.getName())
            {
                return i;
            }
        }

        //Could not be found
        return -1;
    }
    
    public void add(final Student student)
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

    public void edit(int index)
    {
        //studentList.get(index).addCourse();
    }

    /**
     * Prints information of every Student in the {@link #studentList}
     * @return returns 0 if List is empty a
     *         <p>returns 1 if print successful</p>
     */
    public int printAll()
    {
        if(studentList == null)
        {
            System.out.println("printAll(): List is empty");
            return 0;
        }

        Student temp = null;
        for(int i = 0; i < studentList.size(); ++ i)
        {
            temp = (Student)studentList.get(i);
            System.out.println("Name: " + temp.getName() + "   Password: " + temp.getPassword());
        }

        return 1;
    }

    public void encrypt ()
    {

        for(int i = 0; i < studentList.size(); ++ i )
        {
            //Student's password is already encrypted
            if(studentList.get(i).getIsPasswordEncrpyted())
                continue;

            String tempPass = studentList.get(i).getPassword();

            try
            {
                String encryptedPass = AESEncrypter.encrypt(tempPass, this.getEncryptionKey(), this.getIV());
                System.out.println("Encrypted Password: " + encryptedPass);

                studentList.get(i).setPassword(encryptedPass);
                studentList.get(i).toggleEncrypted();
            }

            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    public void decrypt ()
    {
        for(int i = 0; i < studentList.size(); ++ i )
        {
            //Student's password is already decrypted
            if(!studentList.get(i).getIsPasswordEncrpyted())
                continue;

            String tempPass = studentList.get(i).getPassword();

            try
            {
                String decryptedPass = AESEncrypter.decrypt(tempPass, this.getEncryptionKey(), this.getIV());
                System.out.println("Dencrypted Password: " + decryptedPass);

                studentList.get(i).setPassword(decryptedPass);
                studentList.get(i).toggleEncrypted();
            }

            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        //After everything is decrypted, save the password.
        //studentList.
    }

    public void write()
    {
        this.write(studentList, STUDENT_PATH);
    }

    public List getList()
    {
        return studentList;
    }

}
