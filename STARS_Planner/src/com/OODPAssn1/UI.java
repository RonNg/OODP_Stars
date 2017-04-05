package com.OODPAssn1;

import com.OODPAssn1.Entities.*;
import com.OODPAssn1.Managers.CourseManager;

import java.io.Console;
import java.util.List;
import java.util.Scanner;


public class UI
{

    private static Scanner s = new Scanner(System.in);
    private static Console c = System.console();
    private static User.USER_TYPE loggedOnUserType = null;

    public static void main(String[] args)
    {
        while (true)
        {//Main UI loop

            loggedOnUserType = loginScreen();
            if (loggedOnUserType == User.USER_TYPE.ADMIN)
                adminMenu();
            else if (loggedOnUserType == User.USER_TYPE.STUDENT)
                studentMenu();

            if (loggedOnUserType == null)//Quit STARS Program
                return;
        }
        //TODO: Method to check if a password is encrypted

    }
//----------------------------------------Method to display Login screen----------------------------------------------

    public static User.USER_TYPE loginScreen()
    {

        String userName;
        String passWord;
        User.USER_TYPE userType = null;

        System.out.println("Welcome to STARS! Login to continue.\n" +
                "------------------------------------");
        while (userType == null)
        {//Loop for login
            System.out.print("Please enter Username: ");
            userName = s.next();
            System.out.print("Please enter Password: ");
            passWord = s.next();
            /* Code to hide password. Only works in console not in IDE
            //char[] passString = c.readPassword();
            //passWord = new String(passString );
            */
            userType = STARS.getInstance().loginToStars(userName, passWord);
        }

        return userType;

    }


//------------------------------------Method to display Student's menu--------------------------------------------------

    public static void studentMenu()
    {
        int choice;
        while (true)
        {
            System.out.println("\nWhat will you like to do? \n" +
                    "-------------------------\n" +
                    "1) Add Course\n" +
                    "2) Drop Course\n" +
                    "3) Check/Print Courses Registered\n" +
                    "4) Check Vacancies Available\n" +
                    "5) Change Index Number of Course\n" +
                    "6) Swap Index Number with Another Student\n" +
                    "7) Log out and save all changes\n" +
                    "8) Quit STARS and save all changes");

            choice = s.nextInt();

            switch (choice)
            {

                case 1://Add course
                    System.out.println("Course available to enroll: \n" +
                            "--------------------------");
                    STARS.getInstance().printCourseList();
                    System.out.print("Please input the course ID of the course you wished to enroll in: ");
                    String courseId = s.next();
                    System.out.println("Index available to enroll in according to course selected: \n" +
                            "---------------------------------------------------------");
                    STARS.getInstance().printIndexListOfCourse(courseId);
                    //TODO: Enroll into index with checking of vacancy and wait list

                    break;

                case 2://Drop course

                    break;

                case 3://Check/Print Courses Registered

                    break;

                case 4://Check Vacancies Available

                    break;

                case 5://Change Index Number of Course

                    break;

                case 6://Swap Index Number with Another Student

                    break;

                case 7://Save changes and return to login menu
                    STARS.getInstance().writeToDB();
                    return;

                case 8://Save changes and quit program
                    STARS.getInstance().writeToDB();
                    loggedOnUserType = null;
                    return;

                default:
                    System.out.println("Invalid selection!");

            }//end of switch

        }//end of while

    }


//------------------------------------Method to display Admin's menu--------------------------------------------------

    public static void adminMenu()
    {

        int choice;

        while (true)
        {//Loop to show the menu until 7 is choosen

            System.out.println("\nWhat will you like to do? \n" +
                    "-------------------------\n" +
                    "1) Edit student access period\n" +
                    "2) Add a student\n" +
                    "3) Add/Update a course\n" +
                    "4) Check available slot for an index number (vacancy in a class)\n" +
                    "5) Print student list by index number.\n" +
                    "6) Print student list by course (all students registered for the selected course).\n" +
                    "7) Log out and save all changes\n" +
                    "8) Quit STARS and save all changes\n" +
                    "9) Debug");


            choice = s.nextInt();

            switch (choice)
            {

                case 1://Edit student access period

                    break;

                case 2://Add a Student

                    System.out.println("Please enter name of student:");
                    String name = s.next();
                    System.out.println("Please enter email of student:");
                    String email = s.next();
                    System.out.println("Please enter Matric no. of student: ");
                    String matricNo = s.next();
                    System.out.println("Please enter conatct No. of student");
                    int contact = s.nextInt();
                    System.out.println("Please enter gender of student(m for male, f for female): ");
                    String genderStr = s.next();
                    System.out.println("Please enter nationality of student: ");
                    String nationality = s.next();
                    System.out.println("Please enter username of student: ");
                    String username = s.next();
                    System.out.println("Please enter password of student: ");
                    String password = s.next();
                    /*Code to hide password. Only works in console not in IDE
                      char[] passString = c.readPassword();
                      String password = new String(passString );
                    */
                    if (genderStr.equals("m"))
                        STARS.getInstance().enrollStudent(name, email, matricNo,
                                contact, Student.GENDER.MALE, nationality, username, password);
                    else
                        STARS.getInstance().enrollStudent(name, email, matricNo,
                                contact, Student.GENDER.FEMALE, nationality, username, password);

                    //STARS.getInstance().enrollStudent("dude", "dude@e.ntu.edu.sg", "U1625639G",
                    //98245937, Student.GENDER.MALE, "SG", "dude123", "dude123");
                    break;

                //TODO: Add Index after course creation
                case 3://Add a Course and proceed to add index after if user chooses so


                String courseIdAdded = admin_AddCourse(); //Goes to the UI menu for adding course.

                CourseManager.getInstance().save(); //Saves the added course into the database

                System.out.println("Do you want to add Course Index(s) to " + courseIdAdded + "?");
                System.out.println("Y/N");
                String addIndexChoice = s.next();

                switch (addIndexChoice)
                {
                    case "y":
                    case "Y":
                        admin_AddIndex(courseIdAdded);
                        break;

                    case "n":
                    case "N":
                    default: //TODO: Add default case to restart the choice
                        return;
                }
                break;

                case 4://Check available slot for an index number (vacancy in a class)

                    break;

                case 5://Print student list by index number

                    break;

                case 6://Print student list by course (all students registered for the selected course)

                    break;

                case 7://Save changes and return to login menu
                    STARS.getInstance().writeToDB();
                    return;

                case 8://Save changes and quit program
                    STARS.getInstance().writeToDB();
                    loggedOnUserType = null;
                    return;

                case 9://Use for debugging purposes
                    STARS.getInstance().printAllList();
                    break;
                default:
                    System.out.println("Invalid selection!");

            }//end of switch

        }//end of while loop
    }

    /**
     *
     * @return courseId if Course is successfully added into the system
     */
    public static String admin_AddCourse()
    {
        //TODO: Check whether course already exists

        System.out.println("Please enter Course ID");
        String courseId = s.next();
        System.out.println("Please enter Course Name");

        s.nextLine();
        String courseName = s.nextLine();
        System.out.println("Please enter Faculty");
        String faculty = s.next();

        //Adds the course and returns the course object so that we can use it to add the lecture time
        STARS.getInstance().admin_AddCourse(courseId, courseName, faculty);

        System.out.println("");
        System.out.println("Please enter the number of lectures per week for Course " + courseId);
        int noOfLect = s.nextInt();

        //Add the lectures here
        for ( int i = 1; i <= noOfLect; ++ i )
        {
            String lectureNo = null;
            if(i == 1)
                lectureNo = i + "st";
            else if (i == 2)
                lectureNo = i + "nd";
            else if (i == 3)
                lectureNo = i + "rd";
            else
                lectureNo = i + "th";

            //lectureNo displays 1st, 2nd, 3rd, or 4th etc. etc. depending on the variable "i" in the loop
            System.out.println("Options for the days of the week : M, T, W, Th, F, S, Su");
            TimeSlot.DAY timeSlotDay = null;
            boolean validDay = false;
            while(!validDay)
            {
                System.out.println("Enter the day for the " + lectureNo + " of the week: ");

                String day = s.next();

                switch (day)
                {
                    //Monday
                    case "m":
                    case "M":
                        timeSlotDay = TimeSlot.DAY.MON;
                        validDay = true;
                        break;

                    //Tuesday
                    case "t":
                    case "T":
                        timeSlotDay = TimeSlot.DAY.TUE;
                        validDay = true;
                        break;

                    //Wednesday
                    case "w":
                    case "W":
                        timeSlotDay = TimeSlot.DAY.WED;
                        validDay = true;
                        break;

                    //Thursday
                    case "th":
                    case "TH":
                    case "Th":
                    case "tH":
                        timeSlotDay = TimeSlot.DAY.THU;
                        validDay = true;
                        break;

                    //Friday
                    case "f":
                    case "F":
                        timeSlotDay = TimeSlot.DAY.FRI;
                        validDay = true;
                        break;

                    //Saturday
                    case "s":
                    case "S":
                        timeSlotDay = TimeSlot.DAY.SAT;
                        validDay = true;
                        break;

                    //Sunday
                    case "su":
                    case "SU":
                    case "Su":
                    case "sU":
                        timeSlotDay = TimeSlot.DAY.SUN;
                        validDay = true;
                        break;

                    default:
                        System.out.println("Invalid day is entered please try again.");
                        break;
                }
            } //while loop end for validDay

            System.out.println("Enter the lecture START time in 24hrs format for " + timeSlotDay.name());
            String time = s.next();
            int startTimeHH = Integer.parseInt(time.substring(0, time.length()/2));
            int startTimeMM = Integer.parseInt(time.substring(time.length()/2));

            System.out.println("Enter the lecture END time in 24hrs format for " + timeSlotDay.name());
            time = s.next();
            int endTimeHH = Integer.parseInt(time.substring(0, time.length()/2));
            int endTimeMM = Integer.parseInt(time.substring(time.length()/2));

            System.out.println("Please enter the LT number for the Lecture  " + timeSlotDay.name());
            String locationLT = s.next();

            boolean success = STARS.getInstance().admin_AddLecTimeSlot(courseId, timeSlotDay, startTimeHH, startTimeMM, endTimeHH, endTimeMM,locationLT);

            if(success)
                System.out.println("Lecture on " + timeSlotDay.name() + " succesfully added!");
        } //finish loop for entering Lecture

        return courseId;
    }

    /**
     * This function adds index(s) to the course object arg
     * @param course Adds index(s) to this course
     */
    public static void admin_AddIndex(String courseId)
    {
        System.out.println("How many index(s) do you want to add for " + courseId + "?");
        int numberOfIndexToAdd = s.nextInt();

        //Loop to add as many indexes as specified by numberOfIndexToAdd
        for(int i = 0; i < numberOfIndexToAdd; ++ i)
        {

            System.out.println("Please enter the index number to add: " );
            int indexNoToAdd = s.nextInt();

            if(STARS.getInstance().admin_AddIndex(courseId, indexNoToAdd) == false ) //Failed to add
            {
                System.out.println("The index already exists, please try again");
                -- i;  //Start the loop again while preserving the current iteration. since continue makes the loop go to the next iteration, --i cancels out ++i
                continue;
            }




        }
    }



}
