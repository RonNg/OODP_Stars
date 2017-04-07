package com.OODPAssn1;

import com.OODPAssn1.Entities.Student;
import com.OODPAssn1.Entities.TimeSlot;
import com.OODPAssn1.Entities.User;

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
        //STARS.getInstance().populateDatabase();
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

        //Test MD5 Hasher here
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

            if (passWord.equals("debug"))
            {
                STARS.getInstance().populateDatabase();
                System.out.println("Database populated with following: \n");
                STARS.getInstance().printAllList();
                continue;
            }
            else if (passWord.equals("waitlist"))
            {
                STARS.getInstance().populateTestWaitlist();
                System.out.println("Test waitlist populated: \n");
                STARS.getInstance().printAllList();
                continue;
            }
            userType = STARS.getInstance().loginToStars(userName, passWord);
        }

        return userType;

    }


//------------------------------------Method to display Student's menu--------------------------------------------------

    public static void studentMenu()
    {
        int choice;
        if (!STARS.getInstance().checkAccessPeriod())
        {
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
                    "1) Add Course Index\n" +//done
                    "2) Drop Course Index\n" +//done
                    "3) Check/Print Courses Registered\n" +//done
                    "4) Check Vacancies Available\n" +//done
                    "5) Change Index Number of Course\n" +
                    "6) Swap Index Number with Another Student\n" +
                    "7) Log out and save all changes\n" +//done
                    "8) Quit STARS and save all changes");//done

            choice = s.nextInt();

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
                    String toPrint = STARS.getInstance().getStudentRegisteredIndex("");
                    System.out.println(toPrint);
                    break;

                case 4://Check Vacancies Available
                    System.out.println("\n =============================================== " +
                            "\n                 Check Vacancy of Index             " +
                            "\n =============================================== ");
                    System.out.print("Please enter index no. that you wish to check: ");
                    int indexNo = s.nextInt();
                    admin_CheckVacancy(indexNo);
                    break;

                case 5://Change Index Number of Course

                    break;

                case 6://Swap Index Number with Another Student

                    break;

                case 7://Save changes and return to login menu
                    STARS.getInstance().saveData();
                    return;

                case 8://Save changes and quit program
                    STARS.getInstance().saveData();
                    loggedOnUserType = null;
                    return;

                default:
                    System.out.println("Invalid selection!");

            }//end of switch

        }//end of while
    }


    public static void student_AddCourse()
    {
        boolean addFinish = false;
        boolean inputCheck = false;
        while (!addFinish)
        {
            System.out.println("Course available to enroll: \n" +
                    "--------------------------");
            STARS.getInstance().printCourseList();

            System.out.print("\n\nPlease input the course ID of the course you wished to enroll in or type 'quit' to go back to main menu: ");
            String courseId = s.next();

            //Checks if quit and then checks if the course exists, else restart.
            if (courseId.equals("quit"))
            {
                break;
            }
            else if (STARS.getInstance().doesCourseExist(courseId) == false)
            {
                System.out.println("\n\nThe course does not exist, please try again.\n\n");
                continue;
            }

            System.out.println("\n\nPlease enter the index to enroll in according to course selected (or enter -1 to quit): \n" +
                    "---------------------------------------------------------");

            String indexInCourse = STARS.getInstance().getIndexListOfCourse(courseId);
            System.out.println(indexInCourse);
            String indexToEnrollInput;
            int indexToEnroll = 0;
            while(inputCheck==false){
                indexToEnrollInput = s.next();
                try{
                    indexToEnroll = Integer.parseInt(indexToEnrollInput);
                    inputCheck = true;
                }catch (Exception e){
                    System.out.println("Invalid Input. Please try again");
                }
            }
            inputCheck = false;
            if(indexToEnroll == -1)
            {
                break;
            }

            //Checks if index exists else restart
            if (STARS.getInstance().doesIndexExist(indexToEnroll) == false)
            {
                System.out.println("\n\nThe index does not exist, please try again.\n\n");
                continue;
            }


            //Enrols student into index
            int result = STARS.getInstance().student_EnrolIndex(indexToEnroll);
            int[] studentPosInWaitList;
            switch (result)
            {
               /*======================================
                   SUCCESSFULLY ADDED INTO INDEX/WAITLIST
               =======================================*/
                case 1: //Succesfull enrolled into index
                    System.out.println("\n\n\nYou have successfully enrolled into the Index " + indexToEnroll);
                    break;
                case -1: //Added into waitlist
                    studentPosInWaitList = STARS.getInstance().student_getPositionInWaitlist(indexToEnroll); //Gets the student's position in the waitlist
                    System.out.println("\n\n\nYou have been placed into the wait list of " + indexToEnroll);
                    System.out.println("You are currently position " + studentPosInWaitList[0] + " out of " + studentPosInWaitList[1] + " in the waitlist");
                    break;

               /*======================================
                   FAIL TO BE ADDED INTO INDEX/WAITLIST
               =======================================*/
                //Already enrolled in Index
                case 2:
                    System.out.println("\n\n\nYou are already enrolled in Index " + indexToEnroll + "\n");
                    break;

                //Already in the index of the waitlist
                case 3:
                    System.out.println("\n\n\nYou are already in the waitlist of Index " + indexToEnroll + "\n");
                    studentPosInWaitList = STARS.getInstance().student_getPositionInWaitlist(indexToEnroll); //Gets the student's position in the waitlist
                    System.out.println("You are currently position " + studentPosInWaitList[0] + "out of " + studentPosInWaitList[1] + "in the waitlist");
                    break;
            }
            addFinish = true;
        }
    }

    public static void student_DropIndex()
    {
        boolean dropFinish = false;
        boolean inputCheck = false;
        dropLoop:
        while(!dropFinish)
        {
            System.out.println("List of Index(s) registered: \n" +
                                "--------------------------");
            String toPrint = STARS.getInstance().getStudentRegisteredIndex("");
            System.out.println(toPrint);

            s.nextLine();

            System.out.print("\n\nPlease input the index you wish to drop or type 'quit' to go back to main menu: ");
            String indexNoToDrop = null;
            while(inputCheck == false){
                indexNoToDrop = s.next();
                //Checks if quit and then checks if the course exists, else restart.
                try{
                    if (indexNoToDrop.equals("quit"))
                    {
                        break dropLoop;
                    }
                    else if (STARS.getInstance().doesIndexExist(Integer.parseInt(indexNoToDrop)) == false)
                    {
                        System.out.println("\n\nIncorrect index entered. Please try again.\n\n");
                        continue;
                    }
                    inputCheck = true;
                }catch (Exception e){
                    System.out.println("Invalid Input. Please try again");
                    inputCheck = false;
                }
            }

            //STARS will handle removing index from student and removing student from index
            int result = STARS.getInstance().student_DropIndex(Integer.parseInt(indexNoToDrop));

            if(result == 1)
            {
                System.out.println("You have succesfully dropped Index " + indexNoToDrop);
            }
            else
            {
                System.out.println("An error occured. You did not drop your index");
            }

            dropFinish = true;
        }
    }



//------------------------------------Method to display Admin's menu--------------------------------------------------

    public static void adminMenu()
    {

        int choice;

        while (true)
        {//Loop to show the menu until 9 is choosen

            System.out.println("\nWhat will you like to do? \n" +
                    "-------------------------\n" +
                    "1) Edit student access period\n" +//done
                    "2) Add a student\n" +//done
                    "3) Add a course\n" +//done
                    "4) Update a course\n" +//done
                    "5) Check available slot for an index number (vacancy in a class)\n" +//done
                    "6) Print student list by index number.\n" +//done
                    "7) Print student list by course (all students registered for the selected course).\n" +//done
                    "8) Log out and save all changes\n" +//done
                    "9) Quit STARS and save all changes\n" +//done
                    "10) Debug");


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
                        STARS.getInstance().admin_addStudent(name, email, matricNo,
                                contact, Student.GENDER.MALE, nationality, username, password);
                    else
                        STARS.getInstance().admin_addStudent(name, email, matricNo,
                                contact, Student.GENDER.FEMALE, nationality, username, password);

                    //STARS.getInstance().admin_addStudent("dude", "dude@e.ntu.edu.sg", "U1625639G",
                    //98245937, Student.GENDER.MALE, "SG", "dude123", "dude123");
                    break;


                case 3://Add a Course and proceed to add index after if user chooses so

                    System.out.println("\n =============================================== " +
                                       "\n                 Add new course             " +
                                       "\n =============================================== ");

                    String courseIdAdded = admin_AddCourse(); //Goes to the UI menu for adding course.

                        String ans ="";
                        while(!ans.equals("1") && !ans.equals("2")) {
                            System.out.println("Do you want to continue to add Index for the Course you just added?");
                            System.out.println("-------------------------------------------------------------------");
                            System.out.println("1) Yes\n" +
                                    "2) No");
                            ans = s.next();
                        }
                        if(ans.equals("1"))
                            admin_AddIndex(courseIdAdded);

                    break;

                case 4://Update Course

                    System.out.println("\n =============================================== " +
                                       "\n                 Update a course             " +
                                       "\n =============================================== ");


                    STARS.getInstance().printCourseList(); //prints out all course for selection
                    System.out.println("\nEnter the Course ID for the course which you would you like to update: ");
                    String courseId = s.next();

                    if (STARS.getInstance().doesCourseExist(courseId) == false);
                    {

                        System.out.println("What would you like to edit for " + courseId + "?");
                        System.out.println("1) Add Index To Course\n"
                                       +   "2) Delete Index from Course\n"
                                       +   "3) Delete Course" );

                        int updateChoice = s.nextInt();

                        switch(updateChoice)
                        {
                            //Add Index to Course
                            case 1:
                                admin_AddIndex(courseId);
                                break;

                            case 2:
                                System.out.println("Please input the index no. that you wish to remove from course: " );
                                int indexNo = s.nextInt();

                                admin_DeleteIndex(indexNo);
                                break;

                            //Delete Course
                            case 3:
                                admin_DeleteCourse(courseId);
                                break;

                            default:
                                System.out.println("Invalid choice. Returning to main menu");
                                break;
                        }
                    }
                break;

                case 5://Check available slot for an index number (vacancy in a class)
                    System.out.println("\n =============================================== " +
                                       "\n               Check Vacancy of Index             " +
                                       "\n =============================================== ");
                    System.out.print("Please enter index no. that you wish to check: ");
                    int indexNo = s.nextInt();
                    admin_CheckVacancy(indexNo);
                    break;

                case 6://Print student list by index number
                    System.out.println("\n =============================================== " +
                                       "\n        Print student list by Index Number             " +
                                       "\n =============================================== ");
                    System.out.println("Please enter Index No. for printing: " );
                    int ino = s.nextInt();
                    System.out.println("Student registered in index " + ino + ":");
                    System.out.println("-----------------------------------------");
                    System.out.println(STARS.getInstance().getStudentsInIndex(ino));
                    break;

                case 7://Print student list by course (all students registered for the selected course)
                    System.out.println("\n =============================================== " +
                            "\n                 Print student list by course            " +
                            "\n =============================================== ");
                    System.out.println("Please enter course ID for printing: " );
                    String cId = s.next();
                    System.out.println("Student registered in course " + cId + ":");
                    System.out.println("-----------------------------------------");
                    System.out.println(STARS.getInstance().getStudentsInCourse(cId));
                    break;

                case 8://Save changes and return to login menu
                    STARS.getInstance().saveData();
                    return;

                case 9://Save changes and quit program
                    STARS.getInstance().saveData();
                    loggedOnUserType = null;
                    return;

                case 10://Use for debugging purposes
                    STARS.getInstance().printAllList();
                    break;
                default:
                    System.out.println("Invalid selection!");

            }//end of switch

        }//end of while loop
    }



    /**
     * @return courseId if Course is successfully added into the system
     */
    public static String admin_AddCourse()
    {

        String courseId;
        while(true) {
            System.out.println("Please enter Course ID");
            courseId = s.next();
            if(!STARS.getInstance().doesCourseExist(courseId))
                break;
            else System.out.println("Course ID already taken! Please input again");
        }
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
        for (int i = 1; i <= noOfLect; ++i)
        {
            String lectureNo = null;
            if (i == 1)
                lectureNo = i + "st";
            else if (i == 2)
                lectureNo = i + "nd";
            else if (i == 3)
                lectureNo = i + "rd";
            else
                lectureNo = i + "th";

            //lectureNo displays 1st, 2nd, 3rd, or 4th etc. etc. depending on the variable "i" in the loop

            TimeSlot.DAY timeSlotDay = null;
            boolean validDay = false;
            while (!validDay)
            {
                System.out.println("Options for the days of the week : M, T, W, Th, F, S, Su");
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
            int startTimeHH = Integer.parseInt(time.substring(0, time.length() / 2));
            int startTimeMM = Integer.parseInt(time.substring(time.length() / 2));

            System.out.println("Enter the lecture END time in 24hrs format for " + timeSlotDay.name());
            time = s.next();
            int endTimeHH = Integer.parseInt(time.substring(0, time.length() / 2));
            int endTimeMM = Integer.parseInt(time.substring(time.length() / 2));

            System.out.println("Please enter the LT number for the Lecture  " + timeSlotDay.name());
            String locationLT = s.next();

            boolean success = STARS.getInstance().admin_AddLecTimeSlot(courseId, timeSlotDay, startTimeHH, startTimeMM, endTimeHH, endTimeMM, locationLT);

            if (success)
                System.out.println("Lecture on " + timeSlotDay.name() + " succesfully added!");

        } //finish loop for entering Lecture

        return courseId;
    }

//--------------------------------------Method to delete course from STARS----------------------------------------------



    public static void admin_DeleteCourse(String courseId)
    {
        String toBePrint = STARS.getInstance().deleteCourseViaCourseId(courseId);
        if(!toBePrint.equals("Error! Course not found!") && !toBePrint.equals("Error in deletion of course!")) {
            System.out.println("\nCourse " + courseId + " deletion is successful!");
            System.out.println("Students that are de-enrolled from course due to course deletion:");
            System.out.println("--------------------------------------------------------------");
        }
        System.out.println(toBePrint);
        System.out.println("--------------------------------------------------------------");

    }

//--------------------------------------Method to check vacancy in a index----------------------------------------------

    public static void admin_CheckVacancy(int indexNo){

        System.out.println(STARS.getInstance().checkIndexVacancy(indexNo));

    }

    /**
     * This function adds index(s) to the course object arg
     * @param courseId Adds index(s) to this course
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

            System.out.println("Please enter the maximum number of students per class: " );
            int maxStudent = s.nextInt();

            if(STARS.getInstance().admin_AddIndex(courseId, indexNoToAdd, maxStudent) == false ) //Failed to add
            {
                System.out.println("The index already exists, please try again");
                -- i;  //Start the loop again while preserving the current iteration. since continue makes the loop go to the next iteration, --i cancels out ++i
                continue;
            }

            TimeSlot.DAY timeSlotDay = TimeSlot.DAY.SUN;

            /*===================================
                          ADD LAB
            ====================================*/
            boolean validDay = false;
            while(!validDay)
            {
                System.out.println("Options for the days of the week : M, T, W, Th, F, S, Su");
                System.out.println("Enter the day for the LAB for Index " + indexNoToAdd + ":");
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

            System.out.println("Enter the LAB location for Index " + indexNoToAdd + ":");
            String labLocation = s.next();

            System.out.println("Enter the LAB START time in 24hrs format(HHMM): ");
            String time = s.next();
            int startTimeHH = Integer.parseInt(time.substring(0, time.length()/2));
            int startTimeMM = Integer.parseInt(time.substring(time.length()/2));

            System.out.println("Enter the LAB END time in 24hrs format(HHMM): " );
            time = s.next();
            int endTimeHH = Integer.parseInt(time.substring(0, time.length()/2));
            int endTimeMM = Integer.parseInt(time.substring(time.length()/2));

            STARS.getInstance().admin_AddIndexLabTimeSlot(indexNoToAdd, timeSlotDay, startTimeHH, startTimeMM, endTimeHH, endTimeMM, labLocation );

            /*===================================
                          ADD TUT
            ====================================*/
            validDay = false;
            while(!validDay)
            {
                System.out.println("Options for the days of the week : M, T, W, Th, F, S, Su");
                System.out.println("Enter the day for the TUT for Index " + indexNoToAdd + ":");
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

            System.out.println("Enter the TUT location for Index " + indexNoToAdd + ":");
            String tutLocation = s.next();

            System.out.println("Enter the TUT START time in 24hrs format(HHMM): ");
            time = s.next();
            startTimeHH = Integer.parseInt(time.substring(0, time.length()/2));
            startTimeMM = Integer.parseInt(time.substring(time.length()/2));

            System.out.println("Enter the TUT END time in 24hrs format(HHMM): " );
            time = s.next();
            endTimeHH = Integer.parseInt(time.substring(0, time.length()/2));
            endTimeMM = Integer.parseInt(time.substring(time.length()/2));

            STARS.getInstance().admin_AddIndexTutTimeSlot(indexNoToAdd, timeSlotDay, startTimeHH, startTimeMM, endTimeHH, endTimeMM, tutLocation);

        }//end of index list add
    }

    private static void admin_DeleteIndex(int indexNo) {


        String toBePrint = STARS.getInstance().deleteIndexFromCourse(indexNo);
        if(!toBePrint.equals("Error! Index not found!") && !toBePrint.equals("Error occured while deleting index!")) {
            System.out.println("\nCourse " + indexNo + " deletion is successful!");
            System.out.println("Students that are de-enrolled from index due to index deletion:");
            System.out.println("--------------------------------------------------------------");
        }
        System.out.println(toBePrint);
        System.out.println("--------------------------------------------------------------");

    }


}
