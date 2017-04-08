package com.OODPAssn1;

import com.OODPAssn1.Entities.Student;
import com.OODPAssn1.Entities.TimeSlot;
import com.OODPAssn1.Entities.User;

import java.io.Console;
import java.util.Scanner;


public class UI
{

    private static Scanner s = new Scanner(System.in);
    private static Console c = System.console();
    private static STARS stars = STARS.getInstance();
    private static User.USER_TYPE loggedOnUserType = null;

    public static void main(String[] args)
    {
        //stars.populateDatabase();
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
            userName = getString();
            System.out.print("Please enter Password: ");
            passWord = getString();
            /* Code to hide password. Only works in console not in IDE
            //char[] passString = c.readPassword();
            //passWord = new String(passString );
            */

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


//------------------------------------Method to display Student's menu--------------------------------------------------

    public static void studentMenu()
    {
        int choice;
        printTitle("STARS");

        if (!stars.checkAccessPeriod())
        {

            System.out.println("\nSTARS can only be access within this period: ");
            System.out.println("--------------------------------------------");
            System.out.println(stars.getAccessPeriod() + "\n");
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

            choice = getInt();

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

                    student_printCourseRegistered();

                    break;

                case 4://Check Vacancies Available
                   student_checkVacancies();
                    break;

                case 5://Change Index Number of Course

                    break;

                case 6://Swap Index Number with Another Student

                    break;

                case 7://Save changes and return to login menu
                    stars.saveData();
                    return;

                case 8://Save changes and quit program
                    stars.saveData();
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
            else if (stars.doesCourseExist(courseId) == false)
            {
                System.out.println("\n\nThe course does not exist, please try again.\n\n");
                continue;
            }

            System.out.println("\n\nPlease enter the index to enroll in according to course selected (or enter -1 to quit): \n" +
                    "---------------------------------------------------------");

            String indexInCourse = stars.getIndexListOfCourse(courseId);
            System.out.println(indexInCourse);
            String indexToEnrollInput;
            int indexToEnroll = 0;
            indexToEnrollInput = getString();
            if(indexToEnroll == -1)
            {
                break;
            }

            //Checks if index exists else restart
            if (stars.doesIndexExist(indexToEnroll) == false)
            {
                System.out.println("\n\nThe index does not exist, please try again.\n\n");
                continue;
            }


            //Enrols student into index
            int result = stars.student_EnrolIndex(indexToEnroll);
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
                    studentPosInWaitList = stars.student_getPositionInWaitlist(indexToEnroll); //Gets the student's position in the waitlist
                    System.out.println("\n\n\nYou have been placed into the wait list of " + indexToEnroll);
                    System.out.println("You are currently position " + studentPosInWaitList[0] + " out of " + studentPosInWaitList[1] + " in the waitlist");
                    break;

                case 112://Succesfull switched index
                    System.out.println("\n\n\nYou have successfully switched to Index " + indexToEnroll);
                    break;


               /*======================================
                   FAIL TO BE ADDED INTO INDEX/WAITLIST
               =======================================*/
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

            }
            addFinish = true;
        }
    }

    public static void student_DropIndex()
    {
        boolean dropFinish = false;
        boolean inputCheck = false;
        printTitle("Drop Index");
        while(!dropFinish)
        {
            System.out.println("List of Index(s) registered: \n" +
                                "--------------------------");
            String toPrint = stars.getStudentRegisteredIndex("");
            System.out.println(toPrint);

            //s.nextLine();

            System.out.print("\n\nPlease input the index you wish to drop or type 'quit' to go back to main menu: ");
            String indexNoToDrop = null;
            indexNoToDrop = getString();
            if (indexNoToDrop.equals("quit"))
            {
                break;
            }
            else if (stars.doesIndexExist(Integer.parseInt(indexNoToDrop)) == false)
            {
                System.out.println("\n\nIncorrect index entered. Please try again.\n\n");
                continue;
            }
            //STARS will handle removing index from student and removing student from index
            int result = stars.student_DropIndex(Integer.parseInt(indexNoToDrop));

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

    public static void student_printCourseRegistered()
    {
        printTitle("Course Registered");
        String toPrint = stars.getStudentTimeTable("");
        System.out.println(toPrint);
    }

    public static void student_checkVacancies()
    {
        printTitle("Check Vacancies of Index");
        System.out.print("Please enter index no. that you wish to check: ");
        int indexNo = getInt();
        admin_CheckVacancy(indexNo);
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


            choice = getInt();

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

                case 5://Check available slot for an index number (vacancy in a class)
                    System.out.println("\n =============================================== " +
                                       "\n               Check Vacancy of Index             " +
                                       "\n =============================================== ");
                    System.out.print("Please enter index no. that you wish to check: ");
                    int indexNo = getInt();
                    admin_CheckVacancy(indexNo);
                    break;

                case 6://Print student list by index number
                    System.out.println("\n =============================================== " +
                                       "\n        Print student list by Index Number             " +
                                       "\n =============================================== ");
                    System.out.println("Please enter Index No. for printing: " );
                    int ino = getInt();
                    System.out.println("Student registered in index " + ino + ":");
                    System.out.println("-----------------------------------------");
                    System.out.println(stars.getStudentsInIndex(ino));
                    break;

                case 7://Print student list by course (all students registered for the selected course)
                    System.out.println("\n =============================================== " +
                            "\n                 Print student list by course            " +
                            "\n =============================================== ");
                    System.out.println("Please enter course ID for printing: " );
                    String cId = getString();
                    System.out.println("Student registered in course " + cId + ":");
                    System.out.println("-----------------------------------------");
                    String outputStr = stars.getStudentsInCourse(cId);
                    System.out.println(outputStr);
                    break;

                case 8://Save changes and return to login menu
                    stars.saveData();
                    return;

                case 9://Save changes and quit program
                    stars.saveData();
                    loggedOnUserType = null;
                    return;

                case 10://Use for debugging purposes
                    stars.printAllList();
                    break;
                default:
                    System.out.println("Invalid selection!");

            }//end of switch

        }//end of while loop
    }



    /**
     * @return courseId if Course is successfully added into the system
     */
    public static void admin_AddCourse()
    {
        printTitle("Add Course");
        String courseId;
        while(true) {
            System.out.println("Please enter Course ID");

            courseId = getString();
            if(!stars.doesCourseExist(courseId))

                break;
            else System.out.println("Course ID already taken! Please input again");
        }
        System.out.println("Please enter Course Name");

        //s.nextLine();
        String courseName = getString();
        System.out.println("Please enter Faculty");
        String faculty = getString();

        //Adds the course and returns the course object so that we can use it to add the lecture time
        if(!stars.admin_AddCourse(courseId, courseName, faculty)){
            System.out.println("Something went wrong. Course not added.Exiting.. ");
            return;
        }
        
        System.out.println("");
        System.out.println("Please enter the number of lectures per week for Course " + courseId);
        int noOfLect = getInt();

        //Add the lectures here
        for (int i = 1; i <= noOfLect; ++i)
        {
            System.out.println("Options for the days of the week : M, T, W, Th, F, S, Su");
            System.out.println("Enter the day for the lecture:");
            TimeSlot.DAY timeSlotDay = null;
            timeSlotDay = getDay();

            System.out.println("Enter the lecture START time in 24hrs format for " + timeSlotDay.name());
            int startTime = getTime();

            System.out.println("Enter the lecture END time in 24hrs format for " + timeSlotDay.name());
            int endTime = getTime();

            System.out.println("Please enter the LT number for the Lecture  " + timeSlotDay.name());
            String locationLT = getString();

            boolean success = stars.admin_AddLecTimeSlot(courseId, timeSlotDay, startTime/100, startTime%100, endTime/100, endTime%100, locationLT);

            if (success)
                System.out.println("Lecture on " + timeSlotDay.name() + " successfully added!");
            else
                System.out.println("Lecture on " + timeSlotDay.name() + " failed to be added!");

        } //finish loop for entering Lecture

        System.out.println("\nDo you want to continue to add Index for the Course you just added?");
        System.out.println("-------------------------------------------------------------------");
        if(getYesNo()){
            admin_AddIndex(courseId);
        }


        return;
    }

    public static void admin_EditStudentAccessPeriod()
    {
        printTitle("Edit Access Period");
        System.out.println("\nCurrent Access period: ");
        System.out.println("------------------------");
        System.out.println(stars.getAccessPeriod() + "\n");

        String startDate;
        String endDate;

        while(true){
            System.out.print("Please input new start date(dd/mm/yyyy): ");
            startDate = getString();
            if(stars.checkDateFormat(startDate))
                break;
            else System.out.println("Please enter in the format as shown! e.g. 01/04/2017");
        }

        while(true){
            System.out.print("Please input new end date(dd/mm/yyyy): ");
            endDate = getString();
            if(stars.checkDateFormat(endDate))
                break;
            else System.out.println("Please enter in the format as shown! e.g. 30/04/2017");
        }
        

        System.out.println("Updated access period: ");
        System.out.println("--------------------------");
        System.out.println(stars.setAccessPeriod(startDate, endDate));
    }

    public static void admin_AddStudent()
    {
        printTitle("Add Student");

        System.out.println("Please enter name of student:");
        String name = getString();

        System.out.println("Please enter email of student:");
        String email = getString();

        System.out.println("Please enter Matric no. of student: ");
        String matricNo;
        while(true){
            matricNo = getString();
            if(stars.checkStudentExist(matricNo))
                System.out.println("Matic no. already taken. Please enter other matric no.");
            else
                break;
        }

        System.out.println("Please enter contact No. of student");
        int contact = getInt();

        System.out.println("Please enter gender of student(m for male, f for female): ");
        Student.GENDER stGender;
        stGender = getGender();

        System.out.println("Please enter nationality of student: ");
        String nationality = getString();

        System.out.println("Please enter username of student: ");
        String username = getString();

        System.out.println("Please enter password of student: ");
        String password = getString();

                    /*Code to hide password. Only works in console not in IDE
                      char[] passString = c.readPassword();
                      String password = new String(passString );
                    */
        boolean result = stars.admin_addStudent(name, email, matricNo, contact, stGender, nationality, username, password);
        if(result)
            System.out.println(name + "successfully added to STARS");
        else {
            System.out.println("Another student with " + matricNo + " already exist in STARS!\nPlease re-enter another Matric No: ");
            matricNo = getString();
        }

    }

    public static void admin_UpdateCourse()
    {
        printTitle("Update Course");
        System.out.println(stars.printCourseList()); //prints out all course for selection
        System.out.println("\nEnter the Course ID for the course which you would you like to update: ");
        String courseId = getString();

        if (stars.doesCourseExist(courseId) == false);
        {

            System.out.println("What would you like to edit for " + courseId + "?");

            System.out.println("1) Add Index To Course\n"
                    +   "2) Delete Index from Course\n"
                    +   "3) Delete Course" );

            int updateChoice = getInt();

            switch(updateChoice)
            {
                //Add Index to Course
                case 1:
                    admin_AddIndex(courseId);
                    break;

                case 2:
                    System.out.println("Please input the index no. that you wish to remove from course: " );
                    int indexNo = getInt();

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

    }

//--------------------------------------Method to delete course from STARS----------------------------------------------

    public static void admin_DeleteCourse(String courseId)
    {
        String toBePrint = stars.deleteCourseViaCourseId(courseId);
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

        System.out.println(stars.checkIndexVacancy(indexNo));

    }

    /**
     * This function adds index(s) to the course object arg
     * @param courseId Adds index(s) to this course
     */
    public static void admin_AddIndex(String courseId)
    {
        System.out.println("How many index(s) do you want to add for " + courseId + "?");
        int numberOfIndexToAdd = getInt();

        //Loop to add as many indexes as specified by numberOfIndexToAdd
        for(int i = 0; i < numberOfIndexToAdd; ++ i)
        {
            System.out.println("Please enter the index number to add: " );
            int indexNoToAdd = getInt();

            System.out.println("Please enter the maximum number of students per class: " );
            int maxStudent = getInt();

            if(stars.admin_AddIndex(courseId, indexNoToAdd, maxStudent) == false ) //Failed to add
            {
                System.out.println("The index already exists, please try again");
                -- i;  //Start the loop again while preserving the current iteration. since continue makes the loop go to the next iteration, --i cancels out ++i
                continue;
            }

            TimeSlot.DAY timeSlotDay = null;
            int startTime;
            int endTime;

            /*===================================
                          ADD LAB
            ====================================*/

            System.out.println("Options for the days of the week : M, T, W, Th, F, S, Su");
            System.out.println("Enter the day for the LAB for Index " + indexNoToAdd + ":");
            timeSlotDay = getDay();

            System.out.println("Enter the LAB location for Index " + indexNoToAdd + ":");
            String labLocation = getString();

            System.out.println("Enter the LAB START time in 24hrs format(HHMM): ");
            startTime = getTime();

            System.out.println("Enter the LAB END time in 24hrs format(HHMM): " );
            endTime = getTime();
            
            stars.admin_AddIndexLabTimeSlot(indexNoToAdd, timeSlotDay, startTime/100, startTime%100, endTime/100, endTime%100, labLocation );

            /*===================================
                          ADD TUT
            ====================================*/

            System.out.println("Options for the days of the week : M, T, W, Th, F, S, Su");
            System.out.println("Enter the day for the TUT for Index " + indexNoToAdd + ":");
            timeSlotDay = getDay();

            System.out.println("Enter the TUT location for Index " + indexNoToAdd + ":");
            String tutLocation = getString();

            System.out.println("Enter the TUT START time in 24hrs format(HHMM): ");
            startTime = getTime();

            System.out.println("Enter the TUT END time in 24hrs format(HHMM): " );
            endTime = getTime();
            
            stars.admin_AddIndexTutTimeSlot(indexNoToAdd, timeSlotDay, startTime/100, startTime%100, endTime/100, endTime%100, tutLocation );

        }//end of index list add
    }

    private static void admin_DeleteIndex(int indexNo) {


        String toBePrint = stars.deleteIndexFromCourse(indexNo);
        if(!toBePrint.equals("Error! Index not found!") && !toBePrint.equals("Error occured while deleting index!")) {
            System.out.println("\nCourse " + indexNo + " deletion is successful!");
            System.out.println("Students that are de-enrolled from index due to index deletion:");
            System.out.println("--------------------------------------------------------------");
        }
        System.out.println(toBePrint);
        System.out.println("--------------------------------------------------------------");

    }


    //--------------------------------------Helper Methods----------------------------------------------

    public static int getInt(){
        boolean inputCheck = false;
        String input ="";
        while(!inputCheck){
            try{
                input = s.nextLine();
                if(input.length() == 0)
                    continue;
                Integer.parseInt(input);
                inputCheck = true;
            }catch(Exception e){
                System.out.println("Invalid Input. Please try again.");
            }
        }
        return Integer.parseInt(input);
    }

    public static String getString(){
        boolean inputCheck = false;
        String input ="";
        while (!inputCheck){
            try{
                input = s.nextLine();
                if(input.length() == 0)
                    continue;
                inputCheck = true;
            }catch(Exception e){
                System.out.println("Invalid Input. Please try again.");
            }
        }
        return input;
    }

    public static boolean getYesNo(){
        String input;
        while(true){
            try{
                input = s.nextLine();
                if(input.length() == 0)
                    continue;
                switch (input.toUpperCase()){
                    case "Y":
                    case "YES":
                        return true;
                    case "N":
                    case "NO":
                        return false;
                }
                throw new Exception();
            }catch(Exception e){
                System.out.println("Invalid Input. Please try again.");
            }
        }
    }

    public static int getTime(){
        boolean inputCheck = false;
        String input;
        int output = 0;
        while(inputCheck==false){
            try{
                input = s.nextLine();
                if(input.length() == 0)
                    continue;
                input = input.replaceAll(":","");
                input = input.replaceAll("-","");
                output = Integer.parseInt(input);
                inputCheck = true;
                if(output > 2359 || output < 0000)
                    throw new Exception();
            }catch(Exception e){
                System.out.println("Invalid Input. Please try again.");
                inputCheck = false;
            }
        }
        return output;
    }

    public static TimeSlot.DAY getDay(){
        boolean inputCheck = false;
        String input ="";
        TimeSlot.DAY output = null;
        while(inputCheck==false){
            try{
                input = s.nextLine();
                if(input.length() == 0)
                    continue;
                switch (input.trim().toUpperCase()){
                    case "M":
                    case "MON":
                    case "MONDAY":
                        output = TimeSlot.DAY.MON;
                        inputCheck = true;
                        break;
                    case "T":
                    case "TUE":
                    case "TUES":
                    case "TUESDAY":
                        output = TimeSlot.DAY.TUE;
                        inputCheck = true;
                        break;
                    case "W":
                    case "WED":
                    case "WEDNESDAY":
                        output = TimeSlot.DAY.WED;
                        inputCheck = true;
                        break;
                    case "TH":
                    case "THUR":
                    case "THURS":
                    case "THURSDAY":
                        output = TimeSlot.DAY.THU;
                        inputCheck = true;
                        break;
                    case "F":
                    case "FRI":
                    case "FRIDAY":
                        output = TimeSlot.DAY.FRI;
                        inputCheck = true;
                        break;
                    case "S":
                    case "SAT":
                    case "SATURDAY":
                        output = TimeSlot.DAY.SAT;
                        inputCheck = true;
                        break;
                    case "SU":
                    case "SUN":
                    case "SUNDAY":
                        output = TimeSlot.DAY.SUN;
                        inputCheck = true;
                        break;
                }
                if(inputCheck == false)
                    throw new Exception();
            }catch(Exception e){
                System.out.println("Invalid Input. Please try again.");
            }
        }
        return output;
    }

    public static Student.GENDER getGender(){
        String input;
        while(true){
            try{
                input = s.nextLine();
                if(input.length() == 0)
                    continue;
                if(input.length()!=1)
                    throw new Exception();
                if(input.toUpperCase().charAt(0) == 'M')
                    return Student.GENDER.MALE;
                if(input.toUpperCase().charAt(0) == 'F')
                    return Student.GENDER.FEMALE;
                throw new Exception();
            }catch(Exception e){
                System.out.println("Invalid Input. Please try again.");
            }
        }
    }

    public static void printTitle(String title){
        String seperator = "=======================================================";
        System.out.printf("%s%n%" + ((seperator.length()+title.length())/2) + "s%n%s%n%n",seperator,title,seperator);
    }




}
