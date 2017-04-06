package com.OODPAssn1;


import com.OODPAssn1.Entities.Course;
import com.OODPAssn1.Entities.Student;
import com.OODPAssn1.Entities.TimeSlot;
import com.OODPAssn1.Entities.User;
import com.OODPAssn1.Managers.CourseManager;
import com.OODPAssn1.Managers.UserManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by jonah on 15/3/2017.
 */
public class STARS
{
    private static STARS instance;
    private boolean isInitAlready = false;
    private User currentLogOnUser;
    private Calendar startDate;
    private Calendar endDate;



    private STARS(int i)
    {
        init();
    }//Not sure why constructor require argument for it to be recognize

    public static STARS getInstance()
    {
        if (instance == null)
        {
            instance = new STARS(1);
            return instance;
        }
        return instance;
    }

    public int init()
    {
        if (isInitAlready)
        {
            System.out.println("STARS is being reinitialised again!");
            return -1;
        }
        //Set default access period for student to current month and year
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 30);
        this.setAccessPeriod(startDate, endDate);

        return 1;

    }


//--------------------------Method for login to Stars---------------------------------------
/*
  - Return the logged-on User type enum to UI class to facilitate displaying of
    corresponding menu.
  - Return null if User not found in database or failed authentication
  - Invoke authenticateUser method in UserManager class for authentication process
  - Receive and store the logged-on User object to keep track of logged-on user identity
*/

    public User.USER_TYPE loginToStars(String userName, String password)
    {
        User user = UserManager.getInstance().authenticateUser(userName, password);
        if (user != null) //Login successful
        {
            //System.out.println(user.getType() + " " + user.getUsername() + " is now logged on to Stars!");
            currentLogOnUser = user;
            return user.getType();
        }
        return null;
    }

//------------------------Method to get access period-------------------------------------------------------

    public Calendar[] getAccessPeriod(){

        Calendar[] accessPeriod = new Calendar[2];
        accessPeriod[0] = this.startDate;
        accessPeriod[1] = this.endDate;


        return accessPeriod;

    }

//------------------------Method to set start date and end date of STARS availability-----------------------

    public void setAccessPeriod(Calendar startDate, Calendar endDate){


        this.startDate = startDate;
        this.endDate = endDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");

    }

//------------------------Method to check current date is within access period-----------------------------

    public boolean checkAccessPeriod(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        Calendar currentDate = Calendar.getInstance();
        //System.out.println("Current date:" + dateFormat.format(currentDate.getTime()));
        //System.out.println("Start date: " + dateFormat.format(startDate.getTime()) + "   " + "End date: " + dateFormat.format(endDate.getTime()));
        return currentDate.after(startDate) && currentDate.before(endDate);


    }

//------------------------Method for adding/updating of Course----------------------------------------------

    public Course addCourse(String courseId, String courseName, String faculty)
    {
       return CourseManager.getInstance().addCourse(courseId, courseName, faculty);
    }

//-------------------------------Method for enrolling of Student into STARS---------------------------------------------
//This method will make use of UserManager to create student and write it into database.


    public void enrollStudent(String name, String email, String matricNo,
                              int contact, Student.GENDER gender, String nationality,
                              String username, String password)
    {

        UserManager.getInstance().addStudent(name, email, matricNo, contact, gender,
                nationality, username, password);

    }


//-------------------------------Method to write all list back to database----------------------------------------------

    public boolean writeToDB()
    {
        if (UserManager.getInstance().saveData() && CourseManager.getInstance().saveAll())
        {
            //When we save the data of UserManager, the password is encrypted again. We need to decrypt it for login to work again.
            UserManager.getInstance().decryptPassForLogin();
            return true;
        } else
            return false;
    }
//------------------------------Method to print all course available for registration-----------------------------------

    public void printCourseList()
    {
        CourseManager.getInstance().printAllCourse();
    }

//------------------------------Method to print index available in a course---------------------------------------------

    /**
     * print entire index of a course
     *
     * @param courseId courseId to print index
     */
    public void printIndexListOfCourse(String courseId)
    {

        // if(!(CourseManager.getInstance().printIndexOfCourse(CourseManager.getInstance().findCourseById(courseId))))
        // {
        //     System.out.println("N/A");
        //     System.out.println("Please enter valid course id!");
        // }
        //TODO: Print out details of index e.g Time slot info etc...
    }


//----------------Methods for debugging purposes only, remove for production-------------------------------------------
//Remember to remove import statements for Student and Admin when ready for production

    public void printAllList()
    {
        UserManager.getInstance().printAllUser();
        CourseManager.getInstance().printAllCourse();
        CourseManager.getInstance().printAllIndex();
    }

    public void populateDatabase()
    {
        UserManager.getInstance().addStudent("Ron", "c160144@e.ntu", "U1622393B", 93874270, Student.GENDER.MALE, "Singaporean", "c160144", "password");
        UserManager.getInstance().addAdmin("doug", "doug@e.ntu", "doug123",  "doug123");
        //CourseManager.getInstance().createIndex(10032, 50);
        //CourseManager.getInstance().createIndex(CourseManager.getInstance().findCourseById("CE2003"), 10042, 50);
    }


}
