package com.OODPAssn1.Managers;

import com.OODPAssn1.Entities.AccessPeriod;
import com.OODPAssn1.Entities.Admin;
import com.OODPAssn1.Entities.Student;
import com.OODPAssn1.Entities.User;
import com.OODPAssn1.MD5Hasher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * This class is incharge of handling everything {@link Student} related including encryption of the password.
 *
 * @author Tweakisher
 */
public class UserManager extends DataManager
{
    private static UserManager instance;

    public final static String USER_PATH = "user.dat";
    public final static String ACCESS_Period_PATH = "accessPeriod.dat";
    private List<User> userList = null;
    private AccessPeriod acccessPeriod = null;

    private boolean isInitAlready = false;

    private UserManager ()
    {
        //super(USER_PATH);
        super(USER_PATH, ACCESS_Period_PATH);

        //Retrieves the user lists from the database
        userList = (ArrayList) this.read(USER_PATH);
        if (userList == null)
            userList = new ArrayList();

        //Retrieves the user lists from the database
        acccessPeriod = (AccessPeriod) this.read(ACCESS_Period_PATH);
        if (acccessPeriod == null)
            acccessPeriod = new AccessPeriod();
    }


    /*=================================
                 ACCESSORS
     ==================================*/
    public static UserManager getInstance ()
    {
        if (instance == null)
        {
            instance = new UserManager();

            return instance;
        }
        return instance;
    }

    /**
     * Searches {@link List} for {@link Student} by name and returns the index of the list where the student was found
     * @param name Name of the student to search
     * @return index of the student if found in List. <br>
     *         returns -1 if student could not be found
     */
    public int getStudentByName (String name)
    {
        Student temp = null;
        for (int i = 0; i < userList.size(); ++i)
        {
            if (userList.get(i) instanceof Student)
            {
                temp = (Student) userList.get(i);
                if (name == temp.getName())
                    return i;
            }
        }
        //Could not be found
        return -1;
    }

    /**
     * @param matricNumber matriculation number of the student to retrieve
     * @return Student object. <br>
     *         <i>null</i> if student not found
     */
    public Student getStudentByMatricNo (String matricNumber)
    {

        for (int i = 0; i < userList.size(); i++)
        {
            User user = userList.get(i);
            //Downcasting will work for second half of the statement as it got through the first half
            if (userList.get(i).getType() == User.USER_TYPE.STUDENT && ((Student) user).getMatricNo().equals(matricNumber))
                return (Student) user;

        }
        return null;
    }

    /**
     * Authenticates the user and logs him/her in if username and password is correct
     * @param username username of user
     * @param password password of user
     * @return User object if succesfully logged in <br>
     *         <i>null</i> if user not found
     */
    public User authenticateUser (String username, String password)
    {
        password = MD5Hasher.hash(password); //Need to hash the user input password as we are comparing hashes and not plaintext password
        User user;
        for (int i = 0; i < userList.size(); ++i)
        {
            user = userList.get(i);
            if (username.equals(user.getUsername()))
            {
                if (!user.getPassword().equals(password))
                {
                    System.out.println("Password incorrect!");
                    return null;
                }
                else
                    return user;
            }
        }
        System.out.println("User not found in database!");
        return null;//

    }

    /**
     * @return returns the {@link AccessPeriod} object
     */
    public AccessPeriod getAccessPeriod ()
    {
        return acccessPeriod;
    }

    /**
     * Gets the entire student list
     * @return List of students.
     */
    public List<Student> getAllStudent(){
        List<Student> stList = new ArrayList<>();
        for(int n = 0; n < userList.size(); n++){
            if(userList.get(n).getType() == User.USER_TYPE.STUDENT){
                stList.add((Student)userList.get(n));
            }
        }
        return stList;
    }

    /**
     * Check if the username already exists
     * @param username username to check
     * @return true if username already exists
     */
    public boolean doesUserExist(String username)
    {
        for(int n = 0; n < userList.size(); n++){
            if(userList.get(n).getUsername().equals(username))
                return true;
        }
        return false;
    }


    /*==================================

                 MUTATORS

     ==================================*/

    /**
     * Saves the current {@link #userList} into the database file
     * @return true if successful
     */
    public boolean save ()
    {//Return true if save succeed
        return (this.write(userList, USER_PATH) && this.write(acccessPeriod, ACCESS_Period_PATH));
    }

    /**
     * Adds a student into the {@link #userList}
     * @param name               name of the student
     * @param email              email of the student
     * @param matricNo           matriculation number of the student
     * @param contact            contact number of the student
     * @param gender             gender of the student
     * @param nationality        nationality of the student
     * @param username           username of the student
     * @param password           password of the student
     * @return true if successful
     */
    public boolean addStudent (String name, String email, String matricNo,
                               int contact, Student.GENDER gender, String nationality,
                               String username, String password)
    {

        if (this.getStudentByMatricNo(matricNo) != null)
        {
            return false;
        }

        try
        {
            String hashedPass = MD5Hasher.hash(password);
            userList.add(new Student(name, email, matricNo,
                    contact, gender, nationality,
                    username, hashedPass));
        } catch (Exception e)
        {
            System.out.println("addStudent() Exception caught: " + e.getMessage());
        }

        return true;
    }

    /**
     * This method adds an admin user into the database
     * @param name          name of the admin
     * @param email         email of the admin
     * @param username      username of the admin
     * @param password      passwordof the admin
     * @return              true if successfully added
     */
    public boolean addAdmin (final String name, final String email, final String username, final String password)
    {
        try
        {
            String hashedPass = MD5Hasher.hash(password); //We need to hash this as we can't store password as plaintext
            userList.add(new Admin(name, email, username, hashedPass));
        } catch (Exception e)
        {
            return false;
        }


        return this.save();

    }

    /**
     * Changes the access period
     * @param start start date of the access period
     * @param end   end date of the access period
     * @return trueif successful
     */
    public boolean changeAccessPeriod (Calendar start, Calendar end)
    {
        return acccessPeriod.setAccessPeriod(start, end);

    }


//----------------------Methods for debugging purpose only. Remove for production--------------------------------------



    public int printAllUser()
    {
        if (userList == null)
        {
            System.out.println("printAll(): List is empty");
            return 0;
        }
        System.out.println("userList.size(): " + userList.size());
        User temp = null;
        for (int i = 0; i < userList.size(); ++i)
        {

            temp = userList.get(i);
            System.out.println("Name: " + temp.getUsername() + "   Password: " + temp.getPassword());
        }
        return 1;
    }

    /**
     * Prints information of every Student in the {@link #userList}
     *
     * @return returns 0 if List is empty a
     * <p>returns 1 if print successful</p>
     */
    public int print ()
    {
        if (userList == null)
        {
            System.out.println("printAll(): List is empty");
            return 0;
        }
        System.out.println("userList.size(): " + userList.size());
        Student temp = null;
        for (int i = 0; i < userList.size(); ++i)
        {

            if (userList.get(i).getType() == User.USER_TYPE.STUDENT)
            {

                temp = (Student) userList.get(i);
                System.out.println("Name: " + temp.getName() + "   Password: " + temp.getPassword());
            }
        }

        return 1;
    }


}
