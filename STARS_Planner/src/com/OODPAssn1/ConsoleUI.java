package com.OODPAssn1;


import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.Console;
import java.util.Scanner;

/**
 * The <i>ConsoleUI</i> is the front end of the STARS application. No data is handled in this Class. <br>
 * Instead, user inputs are sent to the STARS class for processing.
 */
public class ConsoleUI implements UserInterface
{

    private enum DAY
    {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

    private enum GENDER
    {
        MALE, FEMALE
    }

    private Scanner s = new Scanner(System.in);
    private Console c = System.console();
    private STARS stars = STARS.getInstance();
    private int loggedOnUserType;

    /**
     * Starts the main loop of the UI
     */
    public void start ()
    {
        while (true)
        {//Main UI loop

            loggedOnUserType = loginScreen();
            if (loggedOnUserType == 1)
                adminMenu();
            else if (loggedOnUserType == 0)
                studentMenu();

            if (loggedOnUserType == -2)//Quit STARS Program
                return;
        }

        //Test MD5 Hasher here
        //TODO: Method to check if a password is encrypted

    }

    /**
     * Login screen of the UI
     *
     * @return the userType if successfully logged in
     */
    public int loginScreen ()
    {

        String userName;
        String passWord;
        int userType = -1;
        clearScreen();
        System.out.println("Welcome to STARS! Login to continue.\n" +
                "------------------------------------");
        while (userType == -1)
        {//Loop for login
            System.out.print("Please enter Username: ");
            userName = getString();
            System.out.print("Please enter Password: ");
            char[] passString;
            try
            {
                passString = c.readPassword();
                passWord = new String(passString);
            } catch (Exception e)
            {
                passWord = getString();
            }


            if (passWord.equals("debug"))
            {
                stars.populateDatabase();
                System.out.println("Database populated with following: \n");
                stars.printAllList();
                continue;
            }
            else if (passWord.equals("waitlist"))
            {
                stars.populateTestWaitlist();
                System.out.println("Test waitlist populated: \n");
                stars.printAllList();
                continue;
            }
            userType = stars.loginToStars(userName, passWord);
        }

        return userType;

    }

    /*=========================

               STUDENT

     ==========================*/

    /**
     * The main menu shown to logged in students
     */
    public void studentMenu ()
    {
        int choice;
        clearScreen();
        printTitle("STARS");

        if (!stars.checkAccessPeriod())
        {

            System.out.println("\nSTARS can only be access within this period: ");
            System.out.println("--------------------------------------------");
            System.out.println(stars.getAccessPeriod() + "\n");
            System.out.println("Logging you out now...\n");
            System.out.println("Press Enter to continue");
            while (!(new Scanner(System.in).hasNextLine()))
                ;

            return;
        }

        while (true)
        {

            System.out.println("\nWhat will you like to do? \n" +
                    "-------------------------\n" +
                    "1) Add Course Index\n" +//done
                    "2) Drop Course Index\n" +//done
                    "3) Check/Print Courses Registered\n" +//done
                    "4) Check Vacancies Available\n" +//done
                    "5) Switch Indexes in the Same Course\n" +
                    "6) Swap Index Number with Another Student\n" +
                    "7) Log out and save all changes\n" +//done
                    "8) Quit STARS and save all changes");//done

            choice = getInt();
            clearScreen();
            switch (choice)
            {

                case 1://Add course
                    student_AddCourse();
                    break;

                case 2://Drop course
                    student_DropIndex();
                    //TODO: Implement
                    break;

                case 3://Check/Print Courses Registered
                    student_PrintCourseRegistered();
                    break;

                case 4://Check Vacancies Available
                    student_CheckVacancies();
                    break;

                case 5://Switch Index Number of Course
                    student_SwitchIndex();
                    break;

                case 6://Swap Index Number with Another Student
                    student_SwapIndex();
                    break;

                case 7://Save changes and return to login menu
                    stars.saveData();
                    return;

                case 8://Save changes and quit program
                    stars.saveData();
                    loggedOnUserType = -2;
                    return;

                default:
                    System.out.println("Invalid selection!");

            }//end of switch

        }//end of while
    }

    /**
     * Menu shown to students who are adding courses
     */
    public void student_AddCourse ()
    {
        boolean addFinish = false;
        printTitle("Add Course");
        while (!addFinish)
        {
            System.out.println("Course available to enroll: \n" +
                    "--------------------------");
            System.out.println(stars.printCourseList());

            System.out.print("\n\nPlease input the course ID of the course you wished to enroll in or type 'quit' to go back to main menu: ");
            String courseId = getString();

            //Checks if quit and then checks if the course exists, else restart.
            if (courseId.equalsIgnoreCase("quit"))
            {
                break;
            }
            else if (stars.checkCourseExist(courseId) == false)
            {
                System.out.println("\n\nThe course does not exist, please try again.\n\n");
                continue;
            }

            System.out.println(stars.getIndexListOfCourse(courseId));
            System.out.println("\nPlease enter the index to enroll in according to course selected (or enter -1 to quit):");


            int indexToEnroll = getInt();
            if (indexToEnroll == -1)
            {
                break;
            }

            //Checks if index exists else restart
            if (stars.checkIndexExist(indexToEnroll) == false)
            {
                System.out.println("\n\nThe index does not exist, please try again.\n\n");
                continue;
            }


            //Enrols student into index
            int result = stars.student_EnrolIndex(indexToEnroll, "");
            int[] studentPosInWaitList;
            switch (result)
            {
                case 0:
                    System.out.println("AN ERROR OCCURED");
                    break;
                case 1:
                    System.out.println("You have successfully enrolled into the Index");
                    break;
                case 2:
                    studentPosInWaitList = stars.student_getPositionInWaitlist(indexToEnroll); //Gets the student's position in the waitlist
                    System.out.println("You have been placed into the wait list of this Index");
                    System.out.println("You are currently position " + studentPosInWaitList[0] + " out of " + studentPosInWaitList[1] + " in the waitlist");
                    break;
                case 3:
                    System.out.println("You are already enrolled in this Index");
                    break;
                case 4:
                    System.out.println("You are already in the waitlist of this Index");
                    break;
                case 5:
                    System.out.println("You are already enrolled in another Index of this course");
                    break;
                case 6:
                    System.out.println("You are already in the waitlist of another Index of this course");
                    break;
                case 7:
                    System.out.println("Cannot enrol in Index. Class timing clash with your other Indexes");
                    break;
                default:
                    System.out.println("AN ERROR OCCURRED");
                    break;
            }
            /*switch (result)
            {
               /*======================================
                   SUCCESSFULLY ADDED INTO INDEX/WAITLIST
               =======================================*//*
                case 1: //Succesfull enrolled into index
                    System.out.println("\n\n\nYou have successfully enrolled into the Index " + indexToEnroll);
                    break;
                case -1: //Added into waitlist
                    studentPosInWaitList = stars.student_getPositionInWaitlist(indexToEnroll); //Gets the student's position in the waitlist
                    System.out.println("\n\n\nYou have been placed into the wait list of " + indexToEnroll);
                    System.out.println("You are currently position " + studentPosInWaitList[0] + " out of " + studentPosInWaitList[1] + " in the waitlist");
                    break;

                case 112://Succesfull switched index
                    System.out.println("\n\n\nYou have successfully switched to Index " + indexToEnroll);
                    break;
               /*======================================
                     SUCCESSFUL 1-2, SWITCH!
               =======================================*//*
                case 6:
                    System.out.println("\n\n\nYou have succesfully... Switched! your index to Index " + indexToEnroll);
                    break;

                case 7:
                    System.out.println("\n\n\nYou have succesfully... Switched! But you have been placed into the waitlist of Index" + indexToEnroll);
                    break;

               /*======================================
                   FAIL TO BE ADDED INTO INDEX/WAITLIST
               =======================================*//*
                //Already enrolled in Index
                case 2:
                    System.out.println("\n\n\nYou are already enrolled in Index " + indexToEnroll + "\n");
                    break;
                //Already in the waitlist of the index
                case 3:
                    System.out.println("\n\n\nYou are already in the waitlist of Index " + indexToEnroll + "\n");
                    studentPosInWaitList = stars.student_getPositionInWaitlist(indexToEnroll); //Gets the student's position in the waitlist
                    System.out.println("You are currently position " + studentPosInWaitList[0] + "out of " + studentPosInWaitList[1] + "in the waitlist");
                    break;
                case 111:
                    System.out.println("You can't join the same waitlist");
                    break;
                case 4:
                    System.out.println("Unable to enroll in Index, timing clashed with your other Indexes");
                    break;
                case 5:
                    System.out.println("You are already in another Index of this course");
                    break;
                default:
                    System.out.println("AN ERROR OCCURED");
                    break;

            }*/
            addFinish = true;
        }
        return;
    }

    /**
     * Menu shown to students who are dropping an index
     */
    public void student_DropIndex ()
    {
        boolean dropFinish = false;
        boolean inputCheck = false;
        printTitle("Drop Index");
        while (!dropFinish)
        {
            System.out.println("List of Index(s) registered: \n" +
                    "--------------------------");
            String toPrint = stars.getStudentRegisteredIndex("");
            System.out.println(toPrint);

            //s.nextLine();

            System.out.print("\n\nPlease input the index you wish to drop or type '-1' to go back to main menu: ");
            int indexNoToDrop;
            indexNoToDrop = getInt();
            System.out.println("Please wait");
            if (indexNoToDrop == -1)
            {
                break;
            }
            else if (stars.checkIndexExist(indexNoToDrop) == false)
            {
                System.out.println("\nIncorrect index entered. Please try again.\n");
                continue;
            }
            //STARS will handle removing index from student and removing student from index
            int result = stars.student_DropIndex(indexNoToDrop);

            if (result == 1)
            {
                System.out.println("You have succesfully dropped Index " + indexNoToDrop);
            }
            else
            {
                System.out.println("You are not enrolled in the Index");
            }

            dropFinish = true;
        }
    }


    /**
     * Prints the list of courses registered by the student
     */
    public void student_PrintCourseRegistered ()
    {
        printTitle("Course Registered");
        String toPrint = stars.getStudentTimeTable("");
        System.out.println(toPrint);
    }

    /**
     * Prints the vacancies of specified indexes
     */
    public void student_CheckVacancies ()
    {
        printTitle("Check Vacancies of Index");
        System.out.println(stars.printCourseList());
        System.out.print("Please enter index no. that you wish to check: ");
        int indexNo = getInt();
        System.out.println(stars.checkIndexVacancy(indexNo));
    }

    /**
     * Menu shown when students want to switch indexes of the same course
     */
    public void student_SwitchIndex ()
    {

        int indexToSwitch = -1, indexToSwitchTo = -1;
        boolean loopCheck = false;

        printTitle("Switch Index");
        System.out.println(stars.getStudentTimeTable(""));
        System.out.println("Please enter current Index to switch(-1 to quit): ");
        while (loopCheck == false)
        {
            indexToSwitch = getInt();
            if (indexToSwitch == -1)
                return;
            switch (stars.checkIfEnrolled(indexToSwitch, ""))
            {
                case 0:
                    if (stars.checkIfInWaitList(indexToSwitch, "") == 1)
                    {
                        loopCheck = true;
                    }
                    else
                    {
                        System.out.println("You are not enrolled in the index");
                    }
                    break;
                case 1:
                    loopCheck = true;
                    break;
                default:
                    System.out.println("Invalid Input. Try again.");
                    break;
            }
        }
        loopCheck = false;
        System.out.println(stars.printCourseDetails(stars.getCourseOfIndex(indexToSwitch)));
        System.out.println("Please enter Index to switch to(-1 to quit): ");
        while (!loopCheck)
        {
            indexToSwitchTo = getInt();
            if (indexToSwitchTo == -1)
                return;
            if (stars.checkIndexExist(indexToSwitchTo))
            {
                if (stars.checkIfIndexIsInCourse(indexToSwitchTo, stars.getCourseOfIndex(indexToSwitch)))
                    loopCheck = true;
                else
                    System.out.println("The Index is not from the same course as your Index.");
            }
            else
            {
                System.out.println("Please enter a valid Index.");
            }
        }
        switch (stars.student_SwitchIndex(indexToSwitch, indexToSwitchTo))
        {
            case -1:
                System.out.println("Critical Error Has Occurred.");
                break;
            case 0:
                System.out.println("Failed to switch Index");
                break;
            case 1:
                System.out.println("Successfully switched Index");
                break;
            case 2:
                System.out.println("Successfully switched Index. You are placed in the waitlist of Index " + indexToSwitchTo + ".");
                break;
            default:
                System.out.println("Failed to switch Index");
                break;
        }


    }

    /**
     * Menu shown to students who want to swap indexes with another student
     */
    public void student_SwapIndex ()
    {
        boolean swapFinish = false;
        printTitle("Swap Index");
        while (!swapFinish)
        {
            System.out.println(stars.getStudentTimeTable(""));
            System.out.println("Please enter the index number that you want to swap ('-1' to exit): ");
            int currentUserIndex = getInt();
            if (currentUserIndex == -1)
                return;
            if (stars.checkIndexExist(currentUserIndex) == false)
            {
                System.out.println("Invalid index number entered. Please try again.");
                continue;
            }

            switch (stars.checkIfEnrolled(currentUserIndex, ""))
            {
                case 1: //Student is enrolled
                    System.out.println("Please enter your peer's username: ");
                    String peerUsername = getString();

                    System.out.println("Please enter your peer's password: ");
                    //Code to hide password. Only works in console not in IDE
                    String peerPassword;
                    //peerPassword = getString();
                    char[] passString;
                    try
                    {
                        passString = c.readPassword();
                        peerPassword = new String(passString);
                    } catch (Exception e)
                    {
                        peerPassword = getString();
                    }

                    String peerMatricNo = stars.validateStudentLogin(peerUsername, peerPassword);

                    if (peerMatricNo != null)
                    {
                        System.out.println("Please enter the index to swap: ");
                        int peerIndex = getInt();

                        if (stars.checkIfEnrolled(peerIndex, peerMatricNo) == 1)
                        {
                            if (stars.areIndexSameCourse(currentUserIndex, peerIndex))
                            {
                                //Do swap here
                                if (stars.student_SwapIndex(currentUserIndex, peerMatricNo, peerIndex) == true)
                                {
                                    System.out.println("Index successfully swapped with your peer!");
                                    swapFinish = true;
                                }
                                else
                                {
                                    System.out.println("Error in swapping");
                                    swapFinish = true;
                                    break;
                                }
                            }
                        }
                        else //Peer is not enrolled in index
                        {
                            System.out.println("Peer is not enrolled in this index. Please try again.");
                            continue;
                        }
                    }
                    else
                    {
                        System.out.println("Invalid username/password. Please try again.");
                        continue;
                    }
                    break;
                case 0:
                    System.out.println("Invalid index entered. Please try again.");
                    break;
            }
        }

    }

    /*=========================

               ADMIN

     ==========================*/

    /**
     * The main menu shown to logged in admins
     */
    public void adminMenu ()
    {

        int choice;

        clearScreen();
        while (true)
        {//Loop to show the menu until 9 is choosen

            System.out.println("\nWhat will you like to do? \n" +
                    "-------------------------\n" +
                    "1) Edit student access period\n" +//done
                    "2) Add a student\n" +//done
                    "3) Add a course\n" +//done
                    "4) Update a course\n" +//done
                    "5) Enrol student to Index\n" +
                    "6) Check available slot for an index number (vacancy in a class)\n" +//done
                    "7) Print student list by index number.\n" +//done
                    "8) Print student list by course (all students registered for the selected course).\n" +//done
                    "9) Print all student.\n" +
                    "10) Log out and save all changes\n" +//done
                    "11) Quit STARS and save all changes\n" +//done
                    "12) Debug");


            choice = getInt();
            clearScreen();
            switch (choice)
            {
                case 1://Edit student access period
                    admin_EditStudentAccessPeriod();
                    break;
                case 2://Add a Student
                    admin_AddStudent();
                    break;
                case 3://Add a Course and proceed to add index after if user chooses so
                    admin_AddCourse(); //Goes to the UI menu for adding course.
                    break;
                case 4://Update Course
                    admin_UpdateCourse();
                    break;
                case 5: //Enrol student to index
                    admin_EnrolStudent();
                    break;
                case 6://Check available slot for an index number (vacancy in a class)
                    admin_CheckVacancy();
                    break;

                case 7://Print student list by index number
                    printTitle("Print student list by index");
                    System.out.println("Please enter Index No. for printing: ");
                    int ino = getInt();
                    System.out.println("Student registered in index " + ino + ":");
                    System.out.println("-----------------------------------------");
                    System.out.println(stars.getStudentsInIndex(ino));
                    break;

                case 8://Print student list by course (all students registered for the selected course)
                    printTitle("Print student list by course");
                    System.out.println("Please enter course ID for printing: ");
                    String cId = getString();
                    System.out.println("Student registered in course " + cId + ":");
                    System.out.println("-----------------------------------------");
                    String outputStr = stars.getStudentsInCourse(cId);
                    System.out.println(outputStr);
                    break;
                case 9: //Print all student
                    printTitle("Print all student");
                    System.out.println(stars.admin_GetStudentList());
                    break;
                case 10://Save changes and return to login menu
                    stars.saveData();
                    return;

                case 11://Save changes and quit program
                    stars.saveData();
                    loggedOnUserType = -2;
                    return;

                case 12://Use for debugging purposes
                    stars.printAllList();
                    break;
                default:
                    System.out.println("Invalid selection!");

            }//end of switch

        }//end of while loop
    }

    /**
     * Menu to enrol students into an index
     */
    public void admin_EnrolStudent ()
    {
        String student = "";
        int index = -1;
        printTitle("Enrol Student");
        System.out.println(stars.printCourseList());
        System.out.println("Please enter Index to enrol student(-1 to exit): ");
        boolean indexCheck = false;
        while (indexCheck == false)
        {
            index = getInt();
            if (index == -1)
                return;
            if (stars.checkIndexExist(index))
                indexCheck = true;
            else
                System.out.println("Invalid Index. Please try again.");
        }
        System.out.println("Please enter Matric no of student to enroll(-1 to exit): ");
        boolean studentCheck = false;
        while (studentCheck == false)
        {
            student = getString();
            if (student.equals("-1"))
                return;
            if (stars.checkStudentExist(student))
                studentCheck = true;
            else
                System.out.println("Invalid Matric No. Please try again.");
        }
        switch (stars.student_EnrolIndex(index, student))
        {
            case 0:
                System.out.println("AN ERROR OCCURED");
                break;
            case 1:
                System.out.println("Student have successfully enrolled into the Index");
                break;
            case 2:
                int[] studentPosInWaitList = stars.student_getPositionInWaitlist(index); //Gets the student's position in the waitlist
                System.out.println("Student have been placed into the wait list of this Index");
                System.out.println("Student current position is " + studentPosInWaitList[0] + " out of " + studentPosInWaitList[1] + " in the waitlist");
                break;
            case 3:
                System.out.println("Student already enrolled in this Index");
                break;
            case 4:
                System.out.println("Student already in the waitlist of this Index");
                break;
            case 5:
                System.out.println("Student already enrolled in another Index of this course");
                break;
            case 6:
                System.out.println("Student already in the waitlist of another Index of this course");
                break;
            case 7:
                System.out.println("Cannot enrol in Index. Class timing clash with other Indexes of the student");
                break;
            default:
                System.out.println("AN ERROR OCCURRED");
                break;
        }

    }

    /**
     *
     */
    public void admin_AddStudent ()
    {
        printTitle("Add Student");

        System.out.println("Please enter name of student:");
        String name = getString();

        System.out.println("Please enter email of student:");
        String email = "";
        boolean emailCheck = false;
        while (!emailCheck)
        {
            try
            {
                email = getString();
                new InternetAddress(email).validate();
                emailCheck = true;
            } catch (AddressException e)
            {
                System.out.println("Invalid email format. Please try again.");
            }
        }

        System.out.println("Please enter Matric no. of student: ");
        String matricNo;
        while (true)
        {
            matricNo = getString();
            if (stars.checkStudentExist(matricNo))
                System.out.println("Matric no. already taken. Please enter other matric no.");
            else
                break;
        }

        System.out.println("Please enter contact No. of student");
        int contact = getInt();

        System.out.println("Please enter gender of student(m for male, f for female): ");
        GENDER stGender;
        stGender = getGender();

        System.out.println("Please enter nationality of student: ");
        String nationality = getString();

        System.out.println("Please enter username of student: ");
        boolean userNameCheck = false;
        String username = "";
        while (userNameCheck == false)
        {
            username = getString();
            if (!stars.checkUsernameExist(username))
                userNameCheck = true;
            else
                System.out.println("User Name already exist. Please enter other user name.");
        }

        System.out.println("Please enter password of student: ");
        String password = getString();

                    /*Code to hide password. Only works in console not in IDE
                      char[] passString = c.readPassword();
                      String password = new String(passString );
                    */
        boolean result = false;
        while (!result)
        {
            result = stars.admin_addStudent(name, email, matricNo, contact, stGender.toString(), nationality, username, password);
            if (result)
            {
                System.out.println(name + " successfully added to STARS");
                System.out.println(stars.admin_GetStudentList());
            }
            else
            {
                System.out.println("Another student with " + matricNo + " already exist in STARS!\nPlease re-enter another Matric No: ");
                matricNo = getString();
            }
        }

    }


    /**
     * Menu for adding courses into the course database
     */
    public void admin_AddCourse ()
    {
        printTitle("Add Course");
        String courseId;
        System.out.println("Please enter Course ID");
        courseId = getCourseId();
        System.out.println("Please enter Course Name");
        String courseName = getString();
        System.out.println("Please enter Faculty");
        String faculty = getString();

        //Adds the course and returns the course object so that we can use it to add the lecture time
        if (!stars.admin_AddCourse(courseId, courseName, faculty))
        {
            System.out.println("Something went wrong. Course not added.Exiting.. ");
            return;
        }

        System.out.println("");
        admin_AddLecture(courseId);

        System.out.println("\nDo you want to continue to add Index for the Course you just added(Yes/No)?");
        if (getYesNo())
        {
            admin_AddIndex(courseId);
        }

        return;
    }

    /**
     * Menu for editing students' access periods for STARS
     */
    public void admin_EditStudentAccessPeriod ()
    {
        printTitle("Edit Access Period");
        System.out.println("\nCurrent Access period: ");
        System.out.println("------------------------");
        System.out.println(stars.getAccessPeriod() + "\n");

        String startDate;
        String endDate;

        while (true)
        {
            System.out.print("Please input new start date(dd/mm/yyyy) or enter \"quit\" to quit: ");
            startDate = getString();
            if (startDate.equals("quit"))
                return;
            if (stars.checkDateFormat(startDate))
                //if (stars.checkDateFormat(startDate) && stars.checkIfStartDateValid(startDate))
                break;
            else
                System.out.println("Please enter in the format as shown! e.g. 01/04/2017 " +
                        "and make sure start date entered is today or after today!");
        }

        while (true)
        {
            System.out.print("Please input new end date(dd/mm/yyyy) or enter \"quit\" to quit: : ");
            endDate = getString();
            if (endDate.equals("quit"))
                return;
            if (stars.checkDateFormat(endDate) && stars.checkIfEndDateValid(startDate, endDate))
                break;
            else
                System.out.println("Please enter in the format as shown! e.g. 30/04/2017 " +
                        "and make sure end date is after start date!");
        }


        System.out.println("Updated access period: ");
        System.out.println("--------------------------");
        System.out.println(stars.setAccessPeriod(startDate, endDate));
    }


    /**
     * Menu for updating a course lecture timing or indexes
     */
    public void admin_UpdateCourse ()
    {
        printTitle("Update Course");
        String courseId;
        System.out.println(stars.printCourseList()); //prints out all course for selection
        while (true)
        {
            System.out.println("\nEnter the Course ID for the course which you would you like to update(Enter -1 to quit): ");
            courseId = getString();
            if (courseId.equals("quit"))
                return;
            else if (stars.checkCourseExist(courseId))
                break;
        }


        System.out.println("\n" + stars.printCourseDetails(courseId));
        System.out.println("What would you like to edit for " + courseId + "?");

        System.out.println("1) Add Index to Course\n"
                + "2) Delete Index from Course\n"
                + "3) Add Lecture to Index\n"
                + "4) Remove Lecture from Index\n"
                + "5) Add Tutorial to Index\n"
                + "6) Add Lab to Index\n"
                + "7) Remove Lab or Tutorial from Index\n"
                + "8) Delete Course\n"
                + "-1 = Quit");

        int updateChoice = getInt();

        switch (updateChoice)
        {
            case -1: //quit
                return;
            case 1://Add Index to Course
                admin_AddIndex(courseId);
                break;

            case 2://Delete index from course
                admin_DeleteIndex();
                break;

            case 3://add Lecture to course
                admin_AddLecture(courseId);
                break;

            case 4: //remove lecture from course
                admin_DeleteLecture(courseId);
                break;

            case 5: //Add Tutorial to index
                admin_AddTut(courseId, null);
                break;
            case 6: //Add Lab to index
                admin_AddLab(courseId, null);
                break;

            case 7: //Remove Lab from index
                admin_DeleteLabTuT(courseId);
                break;

            case 8: //Delete course
                admin_DeleteCourse(courseId);
                break;

            default:
                System.out.println("Invalid choice. Returning to main menu");
                break;
        }

    }

    /**
     * Menu for deleting course from the course database
     *
     * @param courseId Course ID to delete
     */
    public void admin_DeleteCourse (String courseId)
    {
        String toBePrint = stars.admin_DeleteCourse(courseId);
        if (!toBePrint.equals("Error! Course not found!") && !toBePrint.equals("Error in deletion of course!"))
        {
            System.out.println("\nCourse " + courseId + " deletion is successful!");
            System.out.println("Students that are de-enrolled from course due to course deletion:");
            System.out.println("--------------------------------------------------------------");
        }
        System.out.println(toBePrint);
        System.out.println("--------------------------------------------------------------");

    }

    /**
     * Menu for displaying the number of vacancies in a specified index
     */
    public void admin_CheckVacancy ()
    {
        printTitle("Check vacancy by Index number");
        System.out.println(stars.printCourseList());
        System.out.print("Please enter index no. that you wish to check: ");
        int indexNo = getInt();
        System.out.println(stars.checkIndexVacancy(indexNo));

    }

    /**
     * This function adds indexes to the course object arg
     *
     * @param courseId Adds indexes to this course
     */
    public void admin_AddIndex (String courseId)
    {

        System.out.println("\nHow many index(s) do you want to add for " + courseId + "(-1 to exit)?");
        int numberOfIndexToAdd = getInt();
        if (numberOfIndexToAdd == -1)
            return;

        //Loop to add as many indexes as specified by numberOfIndexToAdd
        for (int i = 0; i < numberOfIndexToAdd; ++i)
        {
            System.out.println("Please enter the index number to add(-1 to exit): ");
            int indexNoToAdd = getInt();
            while (indexNoToAdd <= 0)
            {
                indexNoToAdd = getInt();
                if (indexNoToAdd == -1)
                    return;
            }
            while (stars.checkIndexExist(indexNoToAdd))
            {
                System.out.println("Index " + "already in system! Please input again!");
                indexNoToAdd = getInt();
            }

            System.out.println("Please enter the maximum number of students for index " + indexNoToAdd + ": ");
            int maxStudent = getInt();

            if (stars.admin_AddIndex(courseId, indexNoToAdd, maxStudent) == false) //Failed to add
            {
                System.out.println("The index already exists, please try again");
                --i;  //Start the loop again while preserving the current iteration. since continue makes the loop go to the next iteration, --i cancels out ++i
                continue;
            }
            admin_AddLab(courseId, indexNoToAdd);
            admin_AddTut(courseId, indexNoToAdd);


        }//end of index list add
    }

    /**
     * Menu shown for deleting indexes
     */
    public void admin_DeleteIndex ()
    {
        System.out.println("Please input the index no. that you wish to remove from course: ");
        int indexNo = getInt();
        String toBePrint = stars.admin_deleteIndexFromCourse(indexNo);
        if (!toBePrint.equals("Error! Index not found!") && !toBePrint.equals("Error occured while deleting index!"))
        {
            System.out.println("\nCourse " + indexNo + " deletion is successful!");
            System.out.println("Students that are de-enrolled from index due to index deletion:");
            System.out.println("--------------------------------------------------------------");
        }
        System.out.println(toBePrint);
        System.out.println("--------------------------------------------------------------");

    }

    /**
     * Menu shown for adding a Lab session in an Index
     *
     * @param courseId   Course in which the index is in
     * @param indexToAdd Index to add the lab
     */
    public void admin_AddLab (String courseId, Integer indexToAdd)
    {
        int index = 0;
        if (indexToAdd == null)
        {
            System.out.println("Please enter index to add lecture");
            boolean inputCheck = false;
            while (!inputCheck)
            {
                index = getInt();
                if (stars.checkIndexExist(index) && stars.checkIfIndexIsInCourse(index, courseId))
                    inputCheck = true;
                else
                    System.out.println("Index does not exist. Please try again");
            }
        }
        else
        {
            index = indexToAdd;
        }

        int noOfLabs = 0;
        String labLocation;
        DAY timeSlotDay = null;
        int startTime;
        int endTime;
        System.out.println("How many Labs to add for " + index + "?: ");
        noOfLabs = getInt();
        for (int n = 1; noOfLabs != 0; n++, noOfLabs--)
        {
            System.out.println("Options for the days of the week : M, T, W, Th, F, S, Su");
            System.out.println("Enter the day for LAB" + n + ": ");
            timeSlotDay = getDay();
            System.out.println("Enter LAB" + n + " START time in 24hrs format(HHMM): ");
            startTime = getTime();
            System.out.println("Enter LAB" + n + " END time in 24hrs format(HHMM): ");
            endTime = getTime(startTime);
            System.out.println("Enter LAB" + n + " location: ");
            labLocation = getString();
            if (!stars.admin_AddIndexLabTimeSlot(index, timeSlotDay.toString(), startTime / 100, startTime % 100, endTime / 100, endTime % 100, labLocation))
            {

                System.out.println("TimeSlot for " + index + " on " + timeSlotDay.toString() + ", " + startTime + "-" + endTime + " failed to be added!");
                System.out.println("Please check there is no other Tut/Lab/Lec happening during that time slot!");
            }
            else
                System.out.println("TimeSlot for " + index + " on " + timeSlotDay.toString() + ", " + startTime + "-" + endTime + " added successfully!");


        }
    }

    /**
     * Menu for deleting a lab or tutorial timeslot in an index
     *
     * @param courseId Course to which the index belongs to for deleting of the lab/tutorial timeslots
     */
    public void admin_DeleteLabTuT (String courseId)
    {

        int indexToGet = -1;
        int choice = -1;
        boolean success = false;
        boolean inputCheck = false;

        System.out.println("Which Index is the Lab/Tutorial from: ");
        while (!inputCheck)
        {
            indexToGet = getInt();
            if (stars.checkIndexExist(indexToGet) && stars.checkIfIndexIsInCourse(indexToGet, courseId))
                inputCheck = true;
            else
                System.out.println("Index does not exist. Please try again");
        }
        System.out.println(stars.admin_GetLabTutList(courseId, indexToGet));
        System.out.println("\nPlease select which lab/tutorial to delete (1,2...) or type -1 to exit: ");
        while (!success)
        {
            choice = getInt();
            if (choice == -1)
                return;
            if (stars.admin_DeleteLabTutTimeSlot(courseId, indexToGet, choice))
            {
                success = true;
                System.out.println("Tutorial/Lab successfully deleted");
            }
            else
            {
                System.out.println("Invalid Input. Try again.");
            }
        }

    }

    /**
     * Menu for adding a tutorial timeslot into an index
     *
     * @param courseId   Course which the index belongs to
     * @param indexToAdd Index of course specified in the courseId argument
     */
    public void admin_AddTut (String courseId, Integer indexToAdd)
    {
        int index = 0;
        if (indexToAdd == null)
        {
            System.out.println("Please enter index to add tutorial");
            boolean inputCheck = false;
            while (!inputCheck)
            {
                index = getInt();
                if (stars.checkIndexExist(index) && stars.checkIfIndexIsInCourse(index, courseId))
                    inputCheck = true;
                else
                    System.out.println("Index does not exist. Please try again");
            }
        }
        else
        {
            index = indexToAdd;
        }

        int noOfTuts = 0;
        String tutLocation;
        DAY timeSlotDay = null;
        int startTime;
        int endTime;

        System.out.println("How many Tutorials to add for " + index + "?: ");
        noOfTuts = getInt();

        for (int n = 1; noOfTuts != 0; n++, noOfTuts--)
        {
            System.out.println("Options for the days of the week : M, T, W, Th, F, S, Su");
            System.out.println("Enter the day for the TUT" + n + ": ");
            timeSlotDay = getDay();
            System.out.println("Enter TUT" + n + " START time in 24hrs format(HHMM): ");
            startTime = getTime();
            System.out.println("Enter TUT" + n + " END time in 24hrs format(HHMM): ");
            endTime = getTime(startTime);
            System.out.println("Enter TUT" + n + " location: ");
            tutLocation = getString();

            if (!stars.admin_AddIndexTutTimeSlot(index, timeSlotDay.toString(), startTime / 100, startTime % 100, endTime / 100, endTime % 100, tutLocation))
            {

                System.out.println("TimeSlot for " + index + " on " + timeSlotDay.toString() + ", " + startTime + "-" + endTime + " failed to be added!");
                System.out.println("Please check there is no other Tut/Lab/Lec happening during that time slot!");
            }
            else
                System.out.println("TimeSlot for " + index + " on " + timeSlotDay.toString() + ", " + startTime + "-" + endTime + " added successfully!");
        }
    }


    /**
     * Menu for adding a lecture into a course
     *
     * @param courseId course to add lecture to
     */
    public void admin_AddLecture (String courseId)
    {
        System.out.println("Please enter the number of lectures to add for " + courseId);
        int noOfLect = getInt();

        //Add the lectures here
        for (int i = 1; i <= noOfLect; ++i)
        {
            System.out.println("Options for the days of the week : M, T, W, Th, F, S, Su");
            System.out.println("Enter the day for the lecture:");
            DAY timeSlotDay = null;
            timeSlotDay = getDay();

            System.out.println("Enter the lecture START time in 24hrs format for " + timeSlotDay.name());
            int startTime = getTime();

            System.out.println("Enter the lecture END time in 24hrs format for " + timeSlotDay.name());
            int endTime = getTime(startTime);

            System.out.println("Please enter the location for the Lecture on " + timeSlotDay.name());
            String locationLT = getString();

            boolean success = stars.admin_AddLecTimeSlot(courseId, timeSlotDay.toString(), startTime / 100, startTime % 100, endTime / 100, endTime % 100, locationLT);

            if (success)
                System.out.println("Lecture on " + timeSlotDay.name() + " successfully added!");
            else
            {
                System.out.println("Lecture on " + timeSlotDay.name() + " failed to be added!");
                System.out.println("Please ensure there is no lec/tut/lab starting at the same time!");
            }

        } //finish loop for entering Lecture
    }

    /**
     * Menu for deleting a lecture into a course
     *
     * @param courseId course to delete the lecture timeslot from
     */
    public void admin_DeleteLecture (String courseId)
    {
        int choice;
        boolean success = false;
        System.out.println(stars.admin_GetLecTimeList(courseId));
        System.out.println("Please select which lecture to delete (1,2...) or type -1 to exit: ");
        while (!success)
        {
            choice = getInt();
            if (choice == -1)
                return;
            if (stars.admin_DeleteLecTimeSlot(courseId, choice))
            {
                success = true;
                System.out.println("Lecture successfully deleted");
            }
            else
            {
                System.out.println("Invalid Input. Try again.");
            }
        }
    }


    //--------------------------------------Helper Methods----------------------------------------------

    /**
     * Checks if user input is a yes or no
     *
     * @return true if yes
     */
    public boolean getYesNo ()
    {
        String input;
        while (true)
        {
            try
            {
                input = s.nextLine();
                if (input.length() == 0)
                    continue;
                switch (input.toUpperCase())
                {
                    case "Y":
                    case "YES":
                        return true;
                    case "N":
                    case "NO":
                        return false;
                }
                throw new Exception();
            } catch (Exception e)
            {
                System.out.println("Invalid Input. Please try again.");
            }
        }
    }

    /**
     * Gets user's input for a time and converts it into an integer value
     *
     * @return integer form of the time that the user inputs <br>
     * e.g. if user input 14:00, the function returns 1400
     */
    public int getTime ()
    {
        boolean inputCheck = false;
        String input;
        int output = 0;
        while (inputCheck == false)
        {
            try
            {
                input = s.nextLine();
                if (input.length() == 0)
                    continue;
                if (!(input.contains(":") || input.contains("-")))
                {
                    if (input.length() != 4)
                        throw new Exception();
                }
                input = input.replaceAll(":", "");
                input = input.replaceAll("-", "");
                output = Integer.parseInt(input);
                inputCheck = true;
                if (output > 2359 || output < 0000)
                    throw new Exception();
            } catch (Exception e)
            {
                System.out.println("Invalid Input. Please try again.");
                inputCheck = false;
            }
        }
        return output;
    }

    /**
     * Gets user input for the end time and checks whether it is valid by comparing it with a start time
     *
     * @param startTime Start time to compare the end time to
     * @return the end time in int format
     */
    public int getTime (int startTime)
    {
        boolean inputCheck = false;
        String input;
        int output = 0;
        while (inputCheck == false)
        {
            try
            {
                input = s.nextLine();
                if (input.length() == 0)
                    continue;
                if (!(input.contains(":") || input.contains("-")))
                {
                    if (input.length() != 4)
                        throw new Exception();
                }
                input = input.replaceAll(":", "");
                input = input.replaceAll("-", "");
                output = Integer.parseInt(input);
                inputCheck = true;
                if (output > 2359 || output < 0000)
                    throw new Exception();
                if (output <= startTime)
                    throw new Exception();
            } catch (Exception e)
            {
                System.out.println("Invalid Input. Please try again.");
                inputCheck = false;
            }
        }
        return output;
    }

    /**
     * Gets user input for integer values and checks whether it is valid
     *
     * @return the user input as an int value
     */
    public int getInt ()
    {
        boolean inputCheck = false;
        String input = "";
        while (!inputCheck)
        {
            try
            {
                input = s.nextLine();
                if (input.length() == 0)
                    continue;
                Integer.parseInt(input);
                inputCheck = true;
            } catch (Exception e)
            {
                System.out.println("Invalid Input. Please try again.");
            }
        }
        return Integer.parseInt(input);
    }

    /**
     * Gets user input for String values and checks whether it is valid
     *
     * @return the user input in String format
     */
    public String getString ()
    {
        boolean inputCheck = false;
        String input = "";
        while (!inputCheck)
        {
            try
            {
                input = s.nextLine();
                if (input.length() == 0)
                    continue;
                inputCheck = true;
            } catch (Exception e)
            {
                System.out.println("Invalid Input. Please try again.");
            }
        }
        return input;
    }

    /**
     * Gets user input for a Course ID and checks whether the Course ID is valid and if the course exists
     *
     * @return The Course ID as in String format
     */
    public String getCourseId ()
    {

        String courseId;
        boolean format = false;

        while (true)
        {
            courseId = getString();
            if (courseId.length() == 6 && Character.isLetter(courseId.charAt(0)) && Character.isLetter(courseId.charAt(1)))
            {

                for (int i = 2; i < 6; i++)
                {
                    if (!Character.isDigit(courseId.charAt(i)))
                    {
                        //System.out.println("Invalid format entered!");
                        format = false;
                        break;
                    }
                    else
                        format = true;
                }

                if (format)
                    if (!stars.checkCourseExist(courseId))
                        break;
                    else
                    {

                        System.out.println(courseId + " already exist in STARS!");
                    }

            }
            if (!format)
                System.out.println("Invalid format entered. Length of course code should be 6." +
                        " First two digit is alphanumeric follow by four digits.");
            System.out.println("Please input again!");
        }

        //System.out.println("Format is fine!");

        return courseId;
    }

    /**
     * Gets the user input for the DAY enums and checks whether the input is valid
     *
     * @return DAY enum based on input
     */
    public DAY getDay ()
    {
        boolean inputCheck = false;
        String input = "";

        DAY output = null;
        while (inputCheck == false)
        {
            try
            {
                input = s.nextLine();
                if (input.length() == 0)
                    continue;
                switch (input.trim().toUpperCase())
                {
                    case "M":
                    case "MON":
                    case "MONDAY":
                        output = DAY.MON;
                        inputCheck = true;
                        break;
                    case "T":
                    case "TUE":
                    case "TUES":
                    case "TUESDAY":
                        output = DAY.TUE;
                        inputCheck = true;
                        break;
                    case "W":
                    case "WED":
                    case "WEDNESDAY":
                        output = DAY.WED;
                        inputCheck = true;
                        break;
                    case "TH":
                    case "THUR":
                    case "THURS":
                    case "THURSDAY":
                        output = DAY.THU;
                        inputCheck = true;
                        break;
                    case "F":
                    case "FRI":
                    case "FRIDAY":
                        output = DAY.FRI;
                        inputCheck = true;
                        break;
                    case "S":
                    case "SAT":
                    case "SATURDAY":
                        output = DAY.SAT;
                        inputCheck = true;
                        break;
                    case "SU":
                    case "SUN":
                    case "SUNDAY":
                        output = DAY.SUN;
                        inputCheck = true;
                        break;
                }
                if (inputCheck == false)
                    throw new Exception();
            } catch (Exception e)
            {
                System.out.println("Invalid Input. Please try again.");
            }
        }
        return output;
    }

    /**
     * Gets the user input for GENDER enum and checks whether the input is valid
     *
     * @return GENDER enum based on input
     */
    public GENDER getGender ()
    {
        String input;
        while (true)
        {
            try
            {
                input = s.nextLine();
                if (input.length() == 0)
                    continue;
                if (input.length() != 1)
                    throw new Exception();
                if (input.toUpperCase().charAt(0) == 'M')
                    return GENDER.MALE;
                if (input.toUpperCase().charAt(0) == 'F')
                    return GENDER.FEMALE;
                throw new Exception();
            } catch (Exception e)
            {
                System.out.println("Invalid Input. Please try again.");
            }
        }
    }


    /**
     * Prints a formatted title based on the function argument
     *
     * @param title Title to print
     */
    public void printTitle (String title)
    {
        String seperator = "=======================================================";
        System.out.printf("%s%n%" + ((seperator.length() + title.length()) / 2) + "s%n%s%n%n", seperator, title, seperator);
    }

    /**
     * Clears the console screen
     */
    public void clearScreen ()
    {
        final String os = System.getProperty("os.name");
        try
        {
            if (os.contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (Exception e)
        {

        }

    }
}
