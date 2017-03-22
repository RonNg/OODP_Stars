package com.OODPAssn1.Managers;

import com.OODPAssn1.AESEncrypter;
import com.OODPAssn1.Entities.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is incharge of handling everything {@link Student} related including encryption of the password.
 *
 * @author Tweakisher
 */
public class UserManager extends DataManager
{
    private static UserManager instance;

    public final static String USER_PATH = "database/user.dat";
    private List<User> userList = null;

    private boolean isInitAlready = false;

    private UserManager (){init();}

    public static UserManager getInstance()
    {
        if (instance == null)
        {
            instance = new UserManager();
            return instance;
        }
        return instance;
    }

    public int init ()
    {
        if(isInitAlready)
        {
            System.out.println("UserManager is being reinitialised again!");
            return -1;
        }

        File file = new File(USER_PATH);
        try
        {
            // createNewFile() returns...
            // true if the named file does not exist and was successfully created;
            // false if the named file already exists

            if(file.createNewFile())
                System.out.println("User Database was created");
            else
                System.out.println("User Database already exists.");

            //Initialise our userList here
            userList = (ArrayList)this.read(USER_PATH);
            if(userList == null)
                userList = new ArrayList();

            this.decryptPassword(); //Decrypts all passwords retrieved from database
        }
        catch (IOException i)
        {
            return -1;
        }

        //Set Encryption Key
        this.setEncryptionKey("StudentKStudentK"); //16 Bytes long
        this.setIV("NotPokemonIVFour"); //16 Bytes long

        return 1;
    }

    /**
     * Prints information of every Student in the {@link #userList}
     * @return returns 0 if List is empty a
     *         <p>returns 1 if print successful</p>
     */
    public int print()
    {
        if(userList == null)
        {
            System.out.println("printAll(): List is empty");
            return 0;
        }

        Student temp = null;
        for(int i = 0; i < userList.size(); ++ i)
        {

            if(userList.get(i).getType() == User.USER_TYPE.STUDENT)
            {
                temp = (Student) userList.get(i);
                System.out.println("Name: " + temp.getName() + "   Password: " + temp.getPassword());
            }
        }

        return 1;
    }


    public void saveData()
    {
        this.encryptPassword(); //Encrypt All Passwords before writing
        this.write(userList, USER_PATH);
    }

    private void encryptPassword ()
    {

        for(int i = 0; i < userList.size(); ++ i )
        {
            //Student's password is already encrypted
            if(userList.get(i).getIsPasswordEncrpyted())
                continue;

            String tempPass = userList.get(i).getPassword();

            try
            {
                String encryptedPass = AESEncrypter.encrypt(tempPass, this.getEncryptionKey(), this.getIV());

                userList.get(i).setPassword(encryptedPass);
                userList.get(i).toggleEncrypted();
            }

            catch (Exception e)
            {
                //System.out.println(e.getMessage());
                //Works but throws an error. Dunno why.
            }
        }
    }

    private void decryptPassword ()
    {
        for(int i = 0; i < userList.size(); ++ i )
        {
            //Student's password is already decrypted
            if(!userList.get(i).getIsPasswordEncrpyted())
                continue;

            String tempPass = userList.get(i).getPassword();

            try
            {
                String decryptedPass = AESEncrypter.decrypt(tempPass, this.getEncryptionKey(), this.getIV());

                userList.get(i).setPassword(decryptedPass);
                userList.get(i).toggleEncrypted();
            }

            catch (Exception e)
            {
                //System.out.println(e.getMessage());
                //Works but throws an error. Dunno why.
            }
        }

        //After everything is decrypted, save the password.
        //userList.
    }




    /**
     * Searches {@link List} for {@link Student} by name and returns the {@link List} index if matched
     * @return index of the student if found in List.
     *         <p>returns -1 if student could not be found</p>
     */
    public int findStudent(String name)
    {
        Student temp = null;
        for(int i = 0; i < userList.size(); ++ i)
        {
            if(userList.get(i) instanceof Student)
            {
                temp = (Student) userList.get(i);
                if (name == temp.getName())
                    return i;
            }
        }
        //Could not be found
        return -1;
    }

    public void addStudent(final Student student)
    {
        try
        {
            userList.add(student);
        }
        catch (Exception e)
        {
            System.out.println("addStudent() Exception caught: " + e.getMessage());
        }
    }

    public void addAdmin (final Admin admin)
    {
        try
        {
            userList.add(admin);
        }
        catch (Exception e)
        {
            System.out.println("addStudent() Exception caught: " + e.getMessage());
        }
    }


}
