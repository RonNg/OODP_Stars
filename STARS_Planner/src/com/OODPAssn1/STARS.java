package com.OODPAssn1;

import com.OODPAssn1.Entities.*;
import com.OODPAssn1.Managers.CourseManager;
import com.OODPAssn1.Managers.UserManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
            System.out.println(user.getType() + " " + user.getUsername() + " is now logged on to Stars!");
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




    public boolean doesCourseExist(String courseId)
    {
        List<Course> courseList = CourseManager.getInstance().getCourseList();
        for(int i = 0; i < courseList.size(); ++ i)
        {

            System.out.println(courseList.get(i).getCourseId());
            if(courseList.get(i).getCourseId().equalsIgnoreCase(courseId))

                return true;
        }

        return false;
    }


    public boolean doesIndexExist (int indexNo)
    {
        List<Course> courseList = CourseManager.getInstance().getCourseList();
        for(int i = 0; i < courseList.size(); ++ i)
        {
            List<Index> indexList = courseList.get(i).getIndexList();
            if(indexList == null) continue; //Go to next course

            for(int j = 0;  j < indexList.size(); ++ j)
            {
                if(indexList.get(i).getIndexNum() == indexNo)
                    return true; //index number is found in entire course list
            }
        }

        return false;

    }

      /*==================================================


                       STUDENT METHODS

     ==================================================*/

    /**
     * @param indexNo The waitlist's index
     * @return returns an int array of size 2 <br>
     *         The first value in the array is the Student's position in the waitlist
     *         The second value in the array is the total number of Students in the waitlist
     */
    public int [] student_getPositionInWaitlist ( int indexNo )
    {
        if (doesIndexExist(indexNo) == false || currentLogOnUser.getType() != User.USER_TYPE.STUDENT)
            return null;

        int [] returnPos = new int[2];

        List<String> tempWaitList = CourseManager.getInstance().findByIndex(indexNo).getWaitList();

        for( int i = 0; i < tempWaitList.size(); ++ i )
        {
            if (((Student)currentLogOnUser).getMatricNo() == tempWaitList.get(i))
            {
                returnPos[0] = i + 1; //+1 since index starts at 0. If you're at index 0, you're in fact in position 1.
                returnPos[1] = tempWaitList.size();
                return returnPos;
            }
        }
        return null; //Student is not in the wait list.
    }

    /**
     *
     * @param indexNo Index Number to enrol into
     * @return Added into waitlist - -1 <br>
     *         Successfully enrolled into index - 1 <br>
     *         Already enrolled in index - 2 <br>
     *         Error occured - 0
     */
    public int student_EnrolIndex(int indexNo)
    {
        if(currentLogOnUser.getType() != User.USER_TYPE.STUDENT) //only students can enroll into indexes
            return 0;

        Student tempStud = (Student)currentLogOnUser;
        int result = CourseManager.getInstance().enrolInIndex(tempStud.getMatricNo(), indexNo);

        switch(result)
        {
            case 1:
                tempStud.addCourseIndex(indexNo); //Student is enrolled in Index list. Update student's course index information

                //Save only if succesfully enrolled into wait list
                CourseManager.getInstance().save();

                UserManager.getInstance().save();
                break;
            case -1:
                CourseManager.getInstance().save(); //Save since student added into waitlist. Do not need to save UserManager as nothing in student is added
                break;

        }
        return result;
    }


    /*==================================================


                       ADMIN METHODS


     ==================================================*/

//------------------------Method to print list of students enrolled in course-------------------------------------------

    public String printStudentsInCourse(String courseId){

        String courseNotFoundStr = "Error! Course not found in system!";
        String CourseisEmptyStr = "No student has enrolled to course yet.";
        String retStr ="";
        Course course = CourseManager.getInstance().findCourseById(courseId);
        List<Index> indexList = null;
        List<String> studentMatric = null;
        List<Student> studentList = null;
        int indexListCount = 0;
        int matricListCount = 0;
        int sizeOfIndex = 0;

        if(course == null)
            retStr = courseNotFoundStr;
        else
        {
            indexList = course.getIndexList();
            while(indexListCount < indexList.size()){

                sizeOfIndex = indexList.get(indexListCount).getEnrolledStudentList().size();//Get size of students enrolled in the index
                while(matricListCount < sizeOfIndex){//Get matric number of students enrolled in the various index
                    studentMatric.add(indexList.get(indexListCount).getEnrolledStudentList().get(matricListCount));
                    matricListCount++;
                }
                matricListCount = 0;
                indexListCount++;
            }
        }

        if(studentMatric != null) {
            for (int i = 0; i < studentMatric.size(); i++) {

                studentList.add(UserManager.getInstance().getStudentObj(studentMatric.get(i)));
                retStr = retStr + "Name: " + studentList.get(i).getName() + "  Gender: " + studentList.get(i).getGender() +
                        "  Nationality: " + studentList.get(i).getNationality() + "\n";

            }
        }else retStr = CourseisEmptyStr;

        return retStr;
    }



//------------------------Method for adding/updating of Course----------------------------------------------------------


    public void admin_AddCourse(String courseId, String courseName, String faculty)
    {
       if (CourseManager.getInstance().addCourse(courseId, courseName, faculty))
           CourseManager.getInstance().save(); //Save after adding
    }

    public boolean admin_AddLecTimeSlot (String courseId, TimeSlot.DAY timeSlotDay, int startTimeHH, int startTimeMM, int endTimeHH, int endTimeMM, String locationLT )
    {
        Course course = CourseManager.getInstance().findCourseById(courseId);

        if(course != null)
        {
            //Save after adding
            course.addlecTimeSlot(timeSlotDay, startTimeHH, startTimeMM, endTimeHH, endTimeMM, locationLT);
            CourseManager.getInstance().save();
            return true;
        }
        else
        {
            System.out.println("STARS: admin_AddLecTimeSlot CourseID not found");
            return false;
        }
    }

    public boolean admin_AddIndex(String courseId, int indexNoToAdd, int maxStudent)
    {
        boolean alreadyExists = false;
        Course tempCourse = CourseManager.getInstance().findCourseById(courseId);

        //Check whether this index already exists
        List<Index> courseIndexList = tempCourse.getIndexList();
        for(int z = 0; z < courseIndexList.size(); ++ z)
        {
            //Index number already exists
            if(courseIndexList.get(z).getIndexNum() == indexNoToAdd)
                return false;
        }

        boolean success = tempCourse.addIndex(indexNoToAdd, maxStudent);

        if(success)
        {
            //Save after adding
            CourseManager.getInstance().save();
            return true;
        }

        return false;
    }

    public boolean admin_AddIndexLabTimeSlot (int indexNo, TimeSlot.DAY day,  int startH, int startM, int endH, int endM, String labLocation)
    {
        Index tempIndex = CourseManager.getInstance().findByIndex(indexNo);
        //Can't find the index
        if(tempIndex == null)
            return false;

        boolean success = tempIndex.addLabTimeSlot(day, startH, startM, endH, endM, labLocation);

        if(success)
        {
            CourseManager.getInstance().save();
            return true;
        }

        return true;
    }

    public boolean admin_AddIndexTutTimeSlot (int indexNo, TimeSlot.DAY day,  int startH, int startM, int endH, int endM, String tutLocation)
    {
        Index tempIndex = CourseManager.getInstance().findByIndex(indexNo);
        //Can't find the index
        if(tempIndex == null)
            return false;

        boolean success = tempIndex.addTutTimeSlot(day, startH, startM, endH, endM, tutLocation);

        if(success)
        {
            CourseManager.getInstance().save();
            return true;
        }

        return true;
    }

    public boolean admin_DeleteCourse(String courseId)
    {
        return false;
    }

//-------------------------------Method for enrolling of Student into STARS---------------------------------------------
//This method will make use of UserManager to create student and write it into database.

    public void admin_addStudent(String name, String email, String matricNo,
                                 int contact, Student.GENDER gender, String nationality,
                                 String username, String password)
    {

        UserManager.getInstance().addStudent(name, email, matricNo, contact, gender,
                nationality, username, password);

    }


//-------------------------------Method to write all list back to database----------------------------------------------

    public boolean writeToDB()
    {
        if (UserManager.getInstance().save() && CourseManager.getInstance().save())
        {
            //When we save the data of UserManager, the password is encrypted again. We need to decrypt it for login to work again.
            return true;
        } else
            return false;
    }
//------------------------------Method to print all course available for registration-----------------------------------

    public void printCourseList()
    {
        CourseManager.getInstance().printAllCourse();
    }


   /*==================================================

                       PRINT METHODS

     ==================================================*/

    /**
     * print entire index of a course
     *
     * @param courseId courseId to print index
     */
    public void printIndexListOfCourse(String courseId)
    {
        List<Index> indexList = CourseManager.getInstance().getIndexList( CourseManager.getInstance().findCourseById(courseId));

        for ( int i = 0; i < indexList.size(); ++ i )
        {
            Index currIndex = indexList.get(i);
            System.out.println("Index " + currIndex.getIndexNum() + " -> " + "Vacancies: " + currIndex.getNumberOfVacancy() +  "|| Student(s) in waitlist: " + currIndex.getWaitList().size());
        }

        //TODO: Print out details of index e.g Time slot info etc...
    }


//----------------Methods for debugging purposes only, remove for production-------------------------------------------
//Remember to remove import statements for Student and Admin when ready for production

    public void printAllList()
    {
        System.out.println("===============================");
        UserManager.getInstance().printAllUser();

        System.out.println("\n\n===============================");
        CourseManager.getInstance().printAllCourse();

        System.out.println("\n\n===============================");
        CourseManager.getInstance().printAllIndexDetails();
    }

    public void populateDatabase()
    {
        //UserManager.getInstance().addStudent("qinghui", "c160144@e.ntu", "U1111111B", 93874270, Student.GENDER.MALE, "Singaporean", "qinghui", "password");
        //UserManager.getInstance().addStudent("bob", "c160144@e.ntu", "U222222B", 93874270, Student.GENDER.MALE, "Singaporean", "bob", "password");
        //UserManager.getInstance().addStudent("ron", "c160144@e.ntu", "U333333B", 93874270, Student.GENDER.MALE, "Singaporean", "ron", "password");
        UserManager.getInstance().addAdmin("doug", "doug@e.ntu", "doug123",  "doug123");
        CourseManager.getInstance().addCourse("CE2003", "DSD", "SCE");
        CourseManager.getInstance().createIndex(CourseManager.getInstance().findCourseById("CE2003"), 10042, 50);
    }


}
