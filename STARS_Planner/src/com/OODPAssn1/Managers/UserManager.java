package com.OODPAssn1.Managers;

import com.OODPAssn1.Entities.Admin;
import com.OODPAssn1.Entities.Student;
import com.OODPAssn1.Entities.User;
import com.OODPAssn1.MD5Hasher;

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

    public final static String USER_PATH = "user.dat";
    private List<User> userList = null;

    private boolean isInitAlready = false;

    private UserManager()
    {
        super(USER_PATH);

        //Retrieves the user lists from the database
        userList = (ArrayList)this.read(USER_PATH);
        if(userList == null)
            userList = new ArrayList();
    }

    public static UserManager getInstance()
    {
        if (instance == null)
        {
            instance = new UserManager();

            return instance;
        }
        return instance;
    }

    /**
     * Prints information of every Student in the {@link #userList}
     *
     * @return returns 0 if List is empty a
     * <p>returns 1 if print successful</p>
     */
    public int print()
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

    public boolean save()
    {//Return true if save succeed
        return this.write(userList, USER_PATH);
    }


    /**
     * Searches {@link List} for {@link Student} by name and returns the {@link List} index if matched
     *
     * @return index of the student if found in List.
     * <p>returns -1 if student could not be found</p>
     */
    public int findStudent(String name)
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

//---------------------------------Method for authentication into STARS-------------------------------------------------
// - Return the logged-on user if authentication succeeds
// - Return null if authentication fail or User not found in DB

    public User authenticateUser(String userName, String password)
    {
        password = MD5Hasher.hash(password); //Need to hash the user input password as we are comparing hashes and not plaintext password
        User user;
        for (int i = 0; i < userList.size(); ++i)
        {
            user = userList.get(i);
            if (userName.equals(user.getUsername()))
            {
                if (!user.getPassword().equals(password))
                {
                    System.out.println("Password incorrect!");
                    return null;
                } else
                    return user;
            }
        }
        System.out.println("User not found in database!");
        return null;//

    }

//-------------------Method to add student into UserList-------------------------------
//Note that writing of new data into DB is not done here!


    public void addStudent(String name, String email, String matricNo,
                           int contact, Student.GENDER gender, String nationality,
                           String username, String password)
    {
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

    }


    public void addAdmin(final String name, final String email, final String username, final String password)
    {
        try
        {
            String hashedPass = MD5Hasher.hash(password); //We need to hash this as we can't store password as plaintext
            userList.add(new Admin(name, email, username, hashedPass));
        } catch (Exception e)
        {
            System.out.println("addAdmin() Exception caught: " + e.getMessage());
        }
    }


    public void addCourseIndex(User student)
    {

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


}
