package com.OODPAssn1;

import com.OODPAssn1.Entities.Student;
import com.OODPAssn1.Entities.User;
import com.OODPAssn1.Managers.CourseManager;
import com.OODPAssn1.Managers.UserManager;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.sun.xml.internal.bind.v2.TODO;

import java.io.Console;
import java.util.Scanner;


public class UI {

    private static Scanner s = new Scanner(System.in);
    private static Console c = System.console();
    private static User.USER_TYPE loggedOnUserType = null;

    public static void main(String[] args) {

        while(true) {//Main UI loop

            loggedOnUserType = loginScreen();
            if (loggedOnUserType == User.USER_TYPE.ADMIN)
                adminMenu();
            else if(loggedOnUserType == User.USER_TYPE.STUDENT)
                studentMenu();

            if(loggedOnUserType == null)//Quit STARS Program
                return;
        }

        //TODO: Method to check if a password is encrypted

    }
//----------------------------------------Method to display Login screen----------------------------------------------

    public static User.USER_TYPE loginScreen(){

        String userName;
        String passWord;
        User.USER_TYPE userType = null;

        System.out.println("Welcome to STARS! Login to continue.\n" +
                            "------------------------------------");
        while (userType == null) {//Loop for login
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

    public static void studentMenu() {

        int choice;
        while(true) {
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

            switch (choice) {

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

    public static void adminMenu() {

        int choice;

        while(true) {//Loop to show the menu until 7 is choosen

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

            switch (choice) {

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

                case 3://Add/Update a Course
                    System.out.println("Please enter Course ID");
                    String courseID = s.next();
                    System.out.println("Please enter Course Name");
                    String courseName = s.next();
                    System.out.println("Please enter faculty");
                    String faculty = s.next();
                    STARS.getInstance().addCourse(courseID, courseName, faculty);
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
                    //STARS.getInstance().populateDatabase();
                    STARS.getInstance().printAllList();
                    break;
                default:
                    System.out.println("Invalid selection!");

            }//end of switch

        }//end of while loop
    }


}
