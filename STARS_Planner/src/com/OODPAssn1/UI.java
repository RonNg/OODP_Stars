package com.OODPAssn1;

import com.OODPAssn1.Entities.Course;
import com.OODPAssn1.Entities.Student;
import com.OODPAssn1.Entities.TimeSlot;
import com.OODPAssn1.Entities.User;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        if(!STARS.getInstance().checkAccessPeriod()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
            Calendar[] accessPeriod;
            accessPeriod = STARS.getInstance().getAccessPeriod();
            System.out.println("You can only access STARS from " + dateFormat.format(accessPeriod[0].getTime()) +
                               " to " + dateFormat.format(accessPeriod[1].getTime()));
            System.out.println("Logging you out now...\n");
            return;
        }

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

                    Date startDateobj, endDateobj;
                    Calendar startDateCal = Calendar.getInstance(); Calendar endDateCal = Calendar.getInstance();
                    String startDate, endDate;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");

                    while(true){
                        try{
                            System.out.print("Please input starting date(dd/mm/yyyy): ");
                            startDate = s.next();
                            startDateobj = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                            System.out.println(startDateobj);
                            startDateCal.setTime(startDateobj);
                            break;
                        }catch(ParseException e){
                            System.out.println("Please enter format as shown!");
                        }

                    }


                    while(true){
                        try{
                            System.out.print("Please input ending date(dd/mm/yyyy): ");
                            endDate = s.next();
                            endDateobj = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                            System.out.println(endDateobj);
                            endDateCal.setTime(endDateobj);
                            break;

                        }catch(ParseException e){
                            System.out.println("Please enter format as shown!");
                        }

                    }

                    STARS.getInstance().setAccessPeriod(startDateCal, endDateCal);
                    Calendar[] c = STARS.getInstance().getAccessPeriod();
                    System.out.println("Updated access period: ");
                    System.out.println("--------------------------");
                    System.out.println("Start date: " + sdf.format(startDateCal.getTime()) + "   " + "End date: " + sdf.format(endDateCal.getTime()));


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

                //TODO: Update course
                case 3://Add a Course
                    System.out.println("Please enter Course ID");
                    String courseID = s.next();
                    System.out.println("Please enter Course Name");

                    s.nextLine();
                    String courseName = s.nextLine();
                    System.out.println("Please enter Faculty");
                    String faculty = s.next();


                    //Adds the course and returns the course object so that we can use it to add the lecture time
                    Course tempCourse = STARS.getInstance().addCourse(courseID, courseName, faculty);

                    System.out.println("");
                    System.out.println("Please enter the number of lectures per week for Course " + tempCourse.getCourseId());
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

                        boolean success = tempCourse.addlecTimeSlot(timeSlotDay, startTimeHH, startTimeMM, endTimeHH, endTimeMM, locationLT);

                        if(success)
                            System.out.println("Lecture on " + timeSlotDay.name() + " succesfully added!");
                    } //loop for entering Lecture


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




    }






