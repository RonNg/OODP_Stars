package com.OODPAssn1;

import com.OODPAssn1.Entities.*;
import com.OODPAssn1.Managers.CourseManager;
import com.OODPAssn1.Managers.UserManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jonah on 15/3/2017.
 */
public class STARS
{
    private static STARS instance;
    private boolean isInitAlready = false;
    private User currentLogOnUser;
    private AccessPeriod currentAccessPeriod;
    private Notification studentNotification;


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


        studentNotification = new Notification();

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


//------------------------Method to check current date is within access period-----------------------------

    public boolean checkAccessPeriod()
    {

        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        Calendar currentDate = Calendar.getInstance();
        //System.out.println("Current date:" + dateFormat.format(currentDate.getTime()));
        //System.out.println("Start date: " + dateFormat.format(startDate.getTime()) + "   " + "End date: " + dateFormat.format(endDate.getTime()));
        return currentDate.after(UserManager.getInstance().getAccessPeriod().getStartDate()) && currentDate.before(UserManager.getInstance().getAccessPeriod().getEndDate());


    }
//----------------------------Method to check valid Date format---------------------------------------------------------

    public boolean checkDateFormat(String date)
    {

        try
        {

            Date dateObj = new SimpleDateFormat("dd/MM/yyyy").parse(date);

        } catch (ParseException e)
        {
            return false;
        }
        return true;
    }

//----------------------------Method to set access period---------------------------------------------------------------

    public String setAccessPeriod(String start, String end)
    {

        Date startDateObj, endDateObj;
        Calendar startDateCal = Calendar.getInstance();
        Calendar endDateCal = Calendar.getInstance();
        String startDate, endDate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");


        try
        {
            startDateObj = new SimpleDateFormat("dd/MM/yyyy").parse(start);
            System.out.println(startDateObj);
            startDateCal.setTime(startDateObj);
            endDateObj = new SimpleDateFormat("dd/MM/yyyy").parse(end);
            System.out.println(endDateObj);
            endDateCal.setTime(endDateObj);

        } catch (ParseException e)
        {
            System.out.println("Please enter format as shown!");
        }

        if (UserManager.getInstance().changeAccessPeriod(startDateCal, endDateCal))
        {
            this.saveData();
            return "Start date: " + sdf.format(startDateCal.getTime()) + "   " + "End date: " + sdf.format(endDateCal.getTime());
        } else
            return "Error in setting access period!";


    }

//----------------------------Method to get access period as formatted string-------------------------------------------

    public String getAccessPeriod()
    {

        String rtrStr;
        AccessPeriod accessPeriod;
        accessPeriod = UserManager.getInstance().getAccessPeriod();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");

        rtrStr = "Start date: " + sdf.format(accessPeriod.getStartDate().getTime()) + "   " +
                 "End date: " + sdf.format(accessPeriod.getEndDate().getTime());
        return rtrStr;
    }

    public boolean doesCourseExist(String courseId)
    {
        List<Course> courseList = CourseManager.getInstance().getCourseList();
        for (int i = 0; i < courseList.size(); ++i)
        {

            //System.out.println(courseList.get(i).getCourseId());
            if (courseList.get(i).getCourseId().equalsIgnoreCase(courseId))

                return true;
        }

        return false;
    }

    public boolean doesIndexExist(int indexNo)
    {
        List<Course> courseList = CourseManager.getInstance().getCourseList();
        for (int i = 0; i < courseList.size(); ++i)
        {
            List<Index> indexList = courseList.get(i).getIndexList();
            if (indexList == null)
                continue; //Go to next course

            for (int j = 0; j < indexList.size(); ++j)
            {
                if (indexList.get(j).getIndexNum() == indexNo)
                    return true; //index number is found in entire course list
            }
        }
        return false;

    }

    public String checkIfIndexClash(Index index1, Index index2)
    {
        //Check Lecture clash
        Course course1 = CourseManager.getInstance().getCourseByIndexNo(index1.getIndexNum());
        Course course2 = CourseManager.getInstance().getCourseByIndexNo(index2.getIndexNum());

        List<TimeSlot> course1LectList = course1.getLecTimeSlotList();
        List<TimeSlot> course2LectList = course2.getLecTimeSlotList();

        for (int i = 0; i < course1LectList.size(); ++i)
        {
            for (int x = 0; x < course2LectList.size(); ++x)
            {
                //If the days match
                if (course1LectList.get(i).getDay().equals(course2LectList.get(x).getDay()))
                {
                    //if Course 2's Lect start time is in between Course 1's Lecture time, there's a clash
                    int course1StartTime = Integer.parseInt(course1LectList.get(i).getStartTime().substring(0,2) + course1LectList.get(i).getStartTime().substring(3));
                    int course1EndTime = Integer.parseInt(course1LectList.get(i).getEndTime().substring(0,2) + course1LectList.get(i).getEndTime().substring(3));
                    int course2StartTime = Integer.parseInt(course2LectList.get(x).getStartTime().substring(0,2) + course2LectList.get(x).getStartTime().substring(3));
                    int course2EndTime = Integer.parseInt(course2LectList.get(x).getEndTime().substring(0,2) + course2LectList.get(x).getEndTime().substring(3));


                    //Start time must check if equals. If both start time is 1400, it clashes.
                    //But if start time of second course is 1400 but end time of first course is 1400, it doesn't clash
                    //Students can just run to their next class
                    if (course2StartTime >= course1StartTime && course2StartTime < course1EndTime)
                    {
                        String retStr = "";
                        retStr += course1.getCourseId() + " lecture time clashes with " + course2.getCourseId() + " on " + course1LectList.get(i).getDay().toString()
                                + "\n\n" + course1.getCourseId() + " lecture time: " + course1StartTime + " - " + course1EndTime + "\n"
                                + course2.getCourseId() + " lecture time: " + course2StartTime + " - " + course2EndTime + "\n";

                        return retStr;
                    }
                }
            }
        }

        //Check Tut clash_
        List<TimeSlot> index1TutLabList = index1.getTutLabTimeSlotList();
        List<TimeSlot> index2TutLabList = index2.getTutLabTimeSlotList();
        for (int i = 0; i < index1TutLabList.size(); ++i)
        {
            for (int x = 0; x < index2TutLabList.size(); ++x)
            {
                //Will only clash if same day
                if (index1TutLabList.get(i).getDay().equals(index2TutLabList.get(x).getDay()))
                {
                    //if Index 2's Tut start time is in between Index 1's Tut time, there's a clash
                    int index1TutLabStartTime = Integer.parseInt(index1TutLabList.get(i).getStartTime().substring(0,2) + index1TutLabList.get(i).getStartTime().substring(3));
                    int index1TutLabEndTime = Integer.parseInt(index1TutLabList.get(i).getEndTime().substring(0,2) + index1TutLabList.get(i).getEndTime().substring(3));

                    int index2TutLabStartTime = Integer.parseInt(index1TutLabList.get(x).getStartTime().substring(0,2) + index1TutLabList.get(x).getStartTime().substring(3));
                    int index2TutLabEndTime = Integer.parseInt(index1TutLabList.get(x).getEndTime().substring(0,2) + index1TutLabList.get(x).getEndTime().substring(3));

                    if (index2TutLabStartTime >= index1TutLabStartTime && index2TutLabStartTime < index1TutLabEndTime)
                    {
                        String retStr = "";
                        retStr += "Index " + index1.getIndexNum() + " LAB/TUT time clashes with " + " Index " + index2.getIndexNum() + " on " + index1TutLabList.get(i).getDay().toString()
                                + "\n\n" + course1.getCourseName() + " " + index1TutLabList.get(i).getType() + " time: " + index1TutLabStartTime + " - " + index1TutLabEndTime + "\n"
                                + course2.getCourseName() + " " + index2TutLabList.get(x).getType() + " time: " + index2TutLabStartTime + " - " + index2TutLabEndTime + "\n";

                        return retStr;
                    }
                }
            }
        }
        return "NO CLASH";
    }

      /*==================================================


                       STUDENT METHODS

     ==================================================*/

    /**
     * @param indexNo The waitlist's index
     * @return returns an int array of size 2 <br>
     * The first value in the array is the Student's position in the waitlist
     * The second value in the array is the total number of Students in the waitlist
     */
    public int[] student_getPositionInWaitlist(int indexNo)
    {
        if (doesIndexExist(indexNo) == false || currentLogOnUser.getType() != User.USER_TYPE.STUDENT)
            return null;

        int[] returnPos = new int[2];

        List<String> tempWaitList = CourseManager.getInstance().getIndexByIndexNo(indexNo).getWaitList();

        for (int i = 0; i < tempWaitList.size(); ++i)
        {
            if (((Student) currentLogOnUser).getMatricNo() == tempWaitList.get(i))
            {
                returnPos[0] = i + 1; //+1 since index starts at 0. If you're at index 0, you're in fact in position 1.
                returnPos[1] = tempWaitList.size();
                return returnPos;
            }
        }
        return null; //Student is not in the wait list.
    }

    /**
     * @param indexNo Index Number to enrol into
     * @return Error occured -> 0 <br>
     * Added into waitlist -> -1 <br>
     * Successfully enrolled into index -> 1 <br>
     * Already enrolled in index -> 2 <br>
     * Already in waitlist of the index -> 3
     */
    public int student_EnrolIndex(int indexNo)
    {
        if (currentLogOnUser.getType() != User.USER_TYPE.STUDENT) //only students can enroll into indexes
            return 0;

        Student tempStud = (Student) currentLogOnUser;


        //TODO: Switch index if same course and got vacancy


        //This code chunk checks for Index Clash
        List<Integer> currentUserIndexList = tempStud.getCourseIndexList();
        Index indexToJoin = CourseManager.getInstance().getIndexByIndexNo(indexNo);
        for (int i = 0; i < currentUserIndexList.size(); ++i)
        {
            Index currUserIndex = CourseManager.getInstance().getIndexByIndexNo(currentUserIndexList.get(i));


            String result = checkIfIndexClash(currUserIndex, indexToJoin);

            if(result.equals("No clash") == false)
            {
                System.out.println(result);
                return 4; //Index clashes
            }
        }

        //If it goes beyond this point, it means its not an index switch/index doesn't clash.
        //Enroll into course
        int result = CourseManager.getInstance().enrolInIndex(tempStud.getMatricNo(), indexNo);

        switch (result)
        {
             /*======================================
                     ADDED INTO INDEX/WAITLIST
            =======================================*/
            //Successfully enrolled
            case 1:
                //Enroll into student index list
                tempStud.addCourseIndex(indexNo); //Student is enrolled in Index list. Update student's course index information
                break;
            //Added into waitlist
            case -1:
                break;
            //Cases 0, 2 and 3 do not do anything as nothing is being changed in CourseManager. See error code.

        }
        saveData();
        return result;
    }

    /**
     * Withdraw from the Index number specified by the current logged in student
     *
     * @param indexNo
     * @return returns 1 if index succesfully dropped <br>
     * returns 0 if error occured
     */
    public int student_DropIndex(int indexNo)
    {
        if (currentLogOnUser.getType() != User.USER_TYPE.STUDENT) //only students can enroll into indexes
            return 0;

        Student currentLogOnStud = (Student) currentLogOnUser;

        //Remove student from index in CourseManager
        String[] result = CourseManager.getInstance().dropFromIndex(currentLogOnStud.getMatricNo(), indexNo);


        if (result[0] != "ERROR")
        {
            //Remove index from Student
            currentLogOnStud.getCourseIndexList().remove((Integer) indexNo);

            if (result[0] == "SUCCESS")
            {
                saveData();
                return 1;
            }

            //HANDLE means that a student from the start of the waitlist has been added into the course index in CourseManager.
            //STARS has to update the UserManager side
            //Remove index from student
            if (result[0] == "HANDLE")
            {
                Student tempStudent = UserManager.getInstance().getStudentByMatricNo(result[1]);
                tempStudent.addCourseIndex(Integer.parseInt(result[2]));
                saveData();

                String courseId = CourseManager.getInstance().getCourseByIndexNo(indexNo).getCourseId();
                String courseName = CourseManager.getInstance().getCourseByIndexNo(indexNo).getCourseName();
                String subject = "Succesfully enrolled into Index " + indexNo;

                String message = "As a student has withdrawn from the index, you have been removed from the waitlist and enrolled into the Index " + indexNo
                        + " for " + courseId + " - " + courseName;

                studentNotification.sendMessage(tempStudent.getEmail(), subject, message);
                return 1;
            }
        }

        return 0;
    }

    /*==================================================


                       ADMIN METHODS


     ==================================================*/

//--------------------------------------Method to delete course from STARS----------------------------------------------
/*
1) Check for existance of course
2) Check if course contain index that are registered with students
    - Get course obj
    - Get indexList
    - Loop through indexList to get the studentsEnrolledList
    - If size > 0, run another loop to retrieve student obj to remove index id from it's courseIndexList
    - Save changes
3) Remove course obj from courseList
    - Save changes
4) Return string that contain names of student that are de-enrolled due to deletion
 */


    public String printStudentsInCourse(String courseId)
    {


        String retStr = "";
        String courseNotFoundErr = "Error! Course not found!";
        String deleteCourseErr = "Error in deletion of course!";

        //if course does not exists
        if (!this.doesCourseExist(courseId))
            return courseNotFoundErr;

        Course course = CourseManager.getInstance().getCourseByCourseId(courseId);

        Index index = null;
        Student student = null;
        List<Index> indexList = course.getIndexList();
        String matricNo; // Stores matrix number

        if (indexList.size() != 0)
        {//Check if Course contain index

            for (int i = 0; i < indexList.size(); i++)
            {

                index = indexList.get(i);
                //- Loop through indexList to get the studentsEnrolledList
                //- If size > 0, run another loop to retrieve student obj to remove index id from it's courseIndexList
                if (index.getEnrolledStudentList().size() > 0)
                {

                    for (int j = 0; j < index.getEnrolledStudentList().size(); j++)
                    {
                        matricNo = index.getEnrolledStudentList().get(j);
                        student = UserManager.getInstance().getStudentByMatricNo(matricNo);
                        student.deEnrollCourseIndex(index.getIndexNum());
                        retStr = retStr + "Matric no.: " + matricNo + "    Name:" + student.getName() +
                                "    From Index: " + index.getIndexNum() + "\n";
                    }
                    UserManager.getInstance().save();
                }
            }
        }
        if (!CourseManager.getInstance().deleteCourse(course))
            retStr = deleteCourseErr;
        else
            CourseManager.getInstance().save();

        return retStr;
    }

    public String deleteCourseViaCourseId(String cId)
    {

        String retStr = "";
        String courseNotFoundErr = "Error! Course not found!";
        String deleteCourseErr = "Error in deletion of course!";
        //if course does not exists
        if (!this.doesCourseExist(cId))
            return courseNotFoundErr;
        Course course = CourseManager.getInstance().getCourseByCourseId(cId);
        Index index = null;
        Student student = null;
        List<Index> indexList = course.getIndexList();
        String matricNo; // Stores matrix number

        if (indexList.size() != 0)
        {//Check if Course contain index

            for (int i = 0; i < indexList.size(); i++)
            {

                index = indexList.get(i);
                //- Loop through indexList to get the studentsEnrolledList
                //- If size > 0, run another loop to retrieve student obj to remove index id from it's courseIndexList
                if (index.getEnrolledStudentList().size() > 0)
                {

                    for (int j = 0; j < index.getEnrolledStudentList().size(); j++)
                    {
                        matricNo = index.getEnrolledStudentList().get(j);
                        student = UserManager.getInstance().getStudentByMatricNo(matricNo);
                        student.deEnrollCourseIndex(index.getIndexNum());
                        retStr = retStr + "Matric no.: " + matricNo + "    Name:" + student.getName() +
                                "    From Index: " + index.getIndexNum() + "\n";
                    }
                    UserManager.getInstance().save();
                }
            }
        }
        if (!CourseManager.getInstance().deleteCourse(course))
            retStr = deleteCourseErr;
        else
            CourseManager.getInstance().save();

        return retStr;
    }


    /*==================================================


                       ADMIN METHODS


     ==================================================*/


//------------------------Method for adding/updating of Course----------------------------------------------------------


    public void admin_AddCourse(String courseId, String courseName, String faculty)
    {
        if (CourseManager.getInstance().addCourse(courseId, courseName, faculty))
            CourseManager.getInstance().save(); //Save after adding
    }

    public boolean admin_AddLecTimeSlot(String courseId, TimeSlot.DAY timeSlotDay, int startTimeHH, int startTimeMM, int endTimeHH, int endTimeMM, String locationLT)
    {
        Course course = CourseManager.getInstance().getCourseByCourseId(courseId);

        if (course != null)
        {
            //Save after adding
            course.addLecTimeSlot(timeSlotDay, startTimeHH, startTimeMM, endTimeHH, endTimeMM, locationLT);
            CourseManager.getInstance().save();
            return true;
        } else
        {
            System.out.println("STARS: admin_AddLecTimeSlot CourseID not found");
            return false;
        }
    }

    public boolean admin_AddIndex(String courseId, int indexNoToAdd, int maxStudent)
    {
        boolean alreadyExists = false;
        Course tempCourse = CourseManager.getInstance().getCourseByCourseId(courseId);

        //Check whether this index already exists
        List<Index> courseIndexList = tempCourse.getIndexList();
        for (int z = 0; z < courseIndexList.size(); ++z)
        {
            //Index number already exists
            if (courseIndexList.get(z).getIndexNum() == indexNoToAdd)
                return false;
        }

        boolean success = tempCourse.addIndex(indexNoToAdd, maxStudent);

        if (success)
        {
            //Save after adding
            CourseManager.getInstance().save();
            return true;
        }

        return false;
    }


    //------------------------------------Method to delete index from course------------------------------------------------
/*
1) Check for existance of index
2) Check if index are registered with students
    - Get index obj
    - get the studentsEnrolledList
    - If size > 0, run loop to retrieve student obj using matric no. to remove index id from it's courseIndexList
    - Save changes
3) Remove index obj from indexList in course
    - Find course obj by index
    - Remove index from indexList
    - Save changes
4) Return string that contain names of student that are de-enrolled due to deletion
 */
    public String deleteIndexFromCourse(int indexNo)
    {
        String rtrStr = "";
        String indexNotFoundStr = "Error! Index not found in STARS!";
        String deleteIndexStr = "Error occured while deleting index!";
        List<String> studentsEnrolledList; // Stores matrix number
        Index index = CourseManager.getInstance().getIndexByIndexNo(indexNo);
        Student student;
        Course course;
        String matricNo;
        if (index == null)
            return indexNotFoundStr;

        if (index.getEnrolledStudentList().size() > 0)
        {

            for (int j = 0; j < index.getEnrolledStudentList().size(); j++)
            {
                matricNo = index.getEnrolledStudentList().get(j);
                student = UserManager.getInstance().getStudentByMatricNo(matricNo);
                student.deEnrollCourseIndex(index.getIndexNum());
                rtrStr = rtrStr + "Matric no.: " + matricNo + "    Name:" + student.getName() +
                        "    From Index: " + index.getIndexNum() + "\n";
            }
            UserManager.getInstance().save();
        }
        course = CourseManager.getInstance().getCourseByIndexNo(indexNo);
        if (!CourseManager.getInstance().deleteIndex(course, index))
            rtrStr = deleteIndexStr;
        else
            CourseManager.getInstance().save();

        return rtrStr;
    }


//------------------------------------Method to check vacancy of index--------------------------------------------------

    public String checkIndexVacancy(int indexNo)
    {

        String rtrStr = "";
        String indexNotFoundStr = "Error! Index not found in STARS!";
        Index index = CourseManager.getInstance().getIndexByIndexNo(indexNo);
        if (index == null)
            return indexNotFoundStr;
        rtrStr = "Number of vacancy in Index " + indexNo + ": " + index.getNumberOfVacancy();
        return rtrStr;
    }

    public boolean admin_AddIndexLabTimeSlot(int indexNo, TimeSlot.DAY day, int startH, int startM, int endH, int endM, String labLocation)
    {
        Index tempIndex = CourseManager.getInstance().getIndexByIndexNo(indexNo);
        //Can't find the index
        if (tempIndex == null)
            return false;

        boolean success = tempIndex.addLabTimeSlot(day, startH, startM, endH, endM, labLocation);

        if (success)
        {
            CourseManager.getInstance().save();
            return true;
        }

        return true;
    }

    public boolean admin_AddIndexTutTimeSlot(int indexNo, TimeSlot.DAY day, int startH, int startM, int endH, int endM, String tutLocation)
    {
        Index tempIndex = CourseManager.getInstance().getIndexByIndexNo(indexNo);
        //Can't find the index
        if (tempIndex == null)
            return false;

        boolean success = tempIndex.addTutTimeSlot(day, startH, startM, endH, endM, tutLocation);

        if (success)
        {
            CourseManager.getInstance().save();
            return true;
        }

        return true;
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

    public boolean saveData()
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

//------------------------Method to print list of students enrolled in index--------------------------------------------

    public String getStudentsInIndex(int indexNo)
    {

        String indexNotFoundStr = "Error! Index not found in system!";
        String indexIsEmptyStr = "No student has enrolled to index yet.";
        String retStr = "";
        Student student;
        Index index = CourseManager.getInstance().getIndexByIndexNo(indexNo);
        if (index == null)
            return indexNotFoundStr;
        List<String> indexList = index.getEnrolledStudentList();
        if (indexList.size() == 0)
            return indexIsEmptyStr;
        for (int i = 0; i < indexList.size(); i++)
        {

            student = UserManager.getInstance().getStudentByMatricNo(indexList.get(i));
            retStr = retStr + "Name: " + student.getName() + "  Gender: " + student.getGender() +
                    "  Nationality: " + student.getNationality() + "\n";

        }

        return retStr;

    }


//------------------------Method to print list of students enrolled in course-------------------------------------------

    public String getStudentsInCourse(String courseId)
    {

        String courseNotFoundStr = "Error! Course not found in system!";
        String CourseisEmptyStr = "No student has enrolled to course yet.";
        String retStr = "";
        Course course = CourseManager.getInstance().getCourseByCourseId(courseId);
        List<Index> indexList = null;
        List<String> studentMatric = new ArrayList<String>();
        List<Student> studentList = new ArrayList<Student>();

        int sizeOfIndex = 0;

        if (course == null)
            retStr = courseNotFoundStr;
        else
        {
            indexList = course.getIndexList();
            for (int i = 0; i < indexList.size(); ++i)
            {
                List<String> currIndexMatricList = indexList.get(i).getEnrolledStudentList();
                for (int j = 0; j < currIndexMatricList.size(); ++j)
                {
                    studentMatric.add(currIndexMatricList.get(j));
                }
            }
        }

        if (studentMatric.size() > 0)
        {
            for (int i = 0; i < studentMatric.size(); i++)
            {
                studentList.add(UserManager.getInstance().getStudentByMatricNo(studentMatric.get(i)));
                retStr = retStr + "Name: " + studentList.get(i).getName() + "  Gender: " + studentList.get(i).getGender() +
                        "  Nationality: " + studentList.get(i).getNationality() + "\n";

            }
        } else
            retStr = CourseisEmptyStr;

        return retStr;
    }


    /**
     * print entire index of a course
     *
     * @param courseId courseId to print index
     */
    public String getIndexListOfCourse(String courseId)
    {
        String retStr = "";
        List<Index> indexList = CourseManager.getInstance().getIndexList(CourseManager.getInstance().getCourseByCourseId(courseId));

        for (int i = 0; i < indexList.size(); ++i)
        {
            Index currIndex = indexList.get(i);
            retStr += "Index " + currIndex.getIndexNum() + " -> " + "Vacancies: " + currIndex.getNumberOfVacancy() + "|| Student(s) in waitlist: " + currIndex.getWaitList().size() + "\n";
        }

        return retStr;
        //TODO: Print out details of index e.g Time slot info etc...
    }

    /**
     * This method is called when you want to receive a formatted string containing timetable of the student for printing purposes.
     *
     * @param matricNo Matriculation number of the student. Leave empty if accessed by Student Menu
     * @return formatted string containing currently registered timetable of the student
     */
    public String getStudentTimeTable(String matricNo)//For student
    {
        String retStr = "";
        StringBuilder retStrBuild = new StringBuilder();
        Student tempStud = null;

        if (currentLogOnUser.getType() == User.USER_TYPE.STUDENT)
        {
            matricNo = ((Student) currentLogOnUser).getMatricNo();
            tempStud = (Student) currentLogOnUser;
        } else
        {
            tempStud = (Student) UserManager.getInstance().getStudentByMatricNo(matricNo);
            //Only admin has to handle this. This error will not occur if logged in user is student
            if (tempStud == null)
            {
                retStr = "ERROR";
                return retStr;
            }
        }

        List<Integer> indexList = tempStud.getCourseIndexList();

        //Check to see if student has registered for any courses
        if (indexList.size() <= 0)
        {
            retStr = "You are not registered in any course";
        }

        for (int i = 0; i < indexList.size(); ++i)
        {
            //Get Course by Index No will never return null as the indexList exists in some course
            Course tempCourse = CourseManager.getInstance().getCourseByIndexNo(indexList.get(i));
            Index tempIndex = tempCourse.getIndex(indexList.get(i));
            List<TimeSlot> tempList = tempIndex.getTutLabTimeSlotList();

            Formatter formatter = new Formatter(retStrBuild, Locale.ENGLISH);

            for (int n = 0; n < tempList.size(); n++)
            {
                TimeSlot tempTimeSlot = tempList.get(n);
                if (n <= 0)
                    formatter.format("%-10s | %-8d | %-5s | %-5s | %s-%-7s | %s %n", tempCourse.getCourseId(), indexList.get(i), tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
                else
                    formatter.format("%-10s | %-8d | %-5s | %-5s | %s-%-7s | %s %n", "", indexList.get(i), tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
            }

            //retStr += "Index: " + indexList.get(i) + " - " + CourseManager.getInstance().getCourseByIndexNo(indexList.get(i)).getCourseName() + "\n";
        }
        retStr += retStrBuild.toString();
        return retStr;
    }

    /**
     * This method is called when you want to receive a formatted string containing currently registered indexes of the student for printing purposes.
     *
     * @param matricNo Matriculation number of the student. Leave empty if accessed by Student Menu
     * @return formatted string containing currently registered indexes of the student
     */
    public String getStudentRegisteredIndex(String matricNo)//For student
    {
        String retStr = "";
        Student tempStud = null;

        if (currentLogOnUser.getType() == User.USER_TYPE.STUDENT)
        {
            matricNo = ((Student) currentLogOnUser).getMatricNo();
            tempStud = (Student) currentLogOnUser;
        } else
        {
            tempStud = (Student) UserManager.getInstance().getStudentByMatricNo(matricNo);
            //Only admin has to handle this. This error will not occur if logged in user is student
            if (tempStud == null)
            {
                retStr = "ERROR";
                return retStr;
            }
        }

        List<Integer> indexList = tempStud.getCourseIndexList();

        //Check to see if student has registered for any courses
        if (indexList.size() <= 0)
        {
            retStr = "You are not registered in any course";
        }

        for (int i = 0; i < indexList.size(); ++i)
        {
            //Get Course by Index No will never return null as the indexList exists in some course
            Course tempCourse = CourseManager.getInstance().getCourseByIndexNo(indexList.get(i));
            retStr += "Index: " + indexList.get(i) + " - " + tempCourse.getCourseName() + "\n";
        }
        return retStr;
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

        System.out.println("\n\n===============================");
        List<Integer> indexList = UserManager.getInstance().getStudentByMatricNo("U1111111B").getCourseIndexList();
        if (indexList.size() > 0)
        {
            System.out.println("Index registered by Qinghui: ");
            for (int i = 0; i < indexList.size(); i++)
            {
                System.out.println(indexList.get(i));
            }
        } else
            System.out.println("No Index registered by Qinghui.");
        System.out.println("\n\n===============================");
    }

    public void populateTestWaitlist()
    {
        UserManager.getInstance().addStudent("qinghui", "qlai002@e.ntu.edu.sg", "U1111111B", 93874270, Student.GENDER.MALE, "Singaporean", "qinghui", "password");
        UserManager.getInstance().addStudent("ron", "c160144@e.ntu.edu.sg", "U333333B", 93874270, Student.GENDER.MALE, "Singaporean", "c160144", "password");
        UserManager.getInstance().addAdmin("doug", "doug@e.ntu", "doug123", "doug123");


        /*=============================
                Create DSD course
         =============================*/
        CourseManager.getInstance().addCourse("CE2003", "Digital System Design", "SCE");
        CourseManager.getInstance().createIndex(CourseManager.getInstance().getCourseByCourseId("CE2003"), 10042, 1);
        CourseManager.getInstance().createIndex(CourseManager.getInstance().getCourseByCourseId("CE2003"), 10043, 1);

        CourseManager.getInstance().addLecTimeSlot(CourseManager.getInstance().getCourseByIndexNo(10042), TimeSlot.DAY.MON, 14, 00, 15, 00, "LT4");
        CourseManager.getInstance().getCourseByIndexNo(10042).getIndex(10042).addTutTimeSlot(TimeSlot.DAY.THU, 11, 00, 13, 00, "TR45");
        CourseManager.getInstance().getCourseByIndexNo(10042).getIndex(10042).addLabTimeSlot(TimeSlot.DAY.WED, 11, 00, 15, 00, "LAB 2");

        CourseManager.getInstance().getCourseByIndexNo(10043).getIndex(10043).addTutTimeSlot(TimeSlot.DAY.THU, 11, 00, 13, 00, "TR30");
        CourseManager.getInstance().getCourseByIndexNo(10043).getIndex(10043).addLabTimeSlot(TimeSlot.DAY.WED, 11, 00, 15, 00, "LAB 1");

        /*=============================
                Create OODP course
         =============================*/
        CourseManager.getInstance().addCourse("CE2002", "Object Oriented Design & Programming", "SCE");
        CourseManager.getInstance().createIndex(CourseManager.getInstance().getCourseByCourseId("CE2002"), 10052, 1);
        CourseManager.getInstance().createIndex(CourseManager.getInstance().getCourseByCourseId("CE2002"), 10053, 1);

        CourseManager.getInstance().addLecTimeSlot(CourseManager.getInstance().getCourseByIndexNo(10052), TimeSlot.DAY.MON, 11, 00, 12, 00, "LT4");
        CourseManager.getInstance().getCourseByIndexNo(10052).getIndex(10052).addTutTimeSlot(TimeSlot.DAY.THU, 11, 00, 13, 00, "TR45");
        CourseManager.getInstance().getCourseByIndexNo(10052).getIndex(10052).addLabTimeSlot(TimeSlot.DAY.WED, 11, 00, 15, 00, "LAB 2");

        CourseManager.getInstance().getCourseByIndexNo(10053).getIndex(10053).addTutTimeSlot(TimeSlot.DAY.THU, 11, 00, 13, 00, "TR30");
        CourseManager.getInstance().getCourseByIndexNo(10053).getIndex(10053).addLabTimeSlot(TimeSlot.DAY.WED, 11, 00, 15, 00, "LAB 1");


        //Adds bob into CourseManager and UserManager
        CourseManager.getInstance().enrolInIndex("U1111111B", 10042);
        UserManager.getInstance().getStudentByMatricNo("U1111111B").addCourseIndex(10042);

        //Adds ron into waitlist
        CourseManager.getInstance().enrolInIndex("U333333B", 10042);

        saveData();
    }

    public void populateDatabase()
    {
        UserManager.getInstance().addStudent("qingru", "c160144@e.ntu", "U222222B", 92298224, Student.GENDER.FEMALE, "Singaporean", "qingru", "password");
        UserManager.getInstance().addStudent("qinghui", "qlai002@e.ntu.edu.sg", "U1111111B", 93874270, Student.GENDER.MALE, "Singaporean", "qinghui", "password");
        UserManager.getInstance().addStudent("ron", "c160144@e.ntu", "U333333B", 93874270, Student.GENDER.MALE, "Singaporean", "c160144", "password");
        UserManager.getInstance().addAdmin("doug", "doug@e.ntu", "doug123", "doug123");
        CourseManager.getInstance().addCourse("CE2003", "DSD", "SCE");
        CourseManager.getInstance().createIndex(CourseManager.getInstance().getCourseByCourseId("CE2003"), 10042, 1);
        CourseManager.getInstance().createIndex(CourseManager.getInstance().getCourseByCourseId("CE2003"), 10043, 1);
    }
}
