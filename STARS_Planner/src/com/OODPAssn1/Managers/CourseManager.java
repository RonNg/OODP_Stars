package com.OODPAssn1.Managers;

import com.OODPAssn1.Entities.Course;
import com.OODPAssn1.Entities.Index;
import com.OODPAssn1.Entities.TimeSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * <i>CourseManager</i> handles every {@link Course } object and by extension the list of {@link Index} in each Course object. <br><br>
 * <p>
 * <i>CourseManager</i> is a <b>Singleton</b> class and hence to access it you must call the {@link #getInstance()} function.
 */
public class CourseManager extends DataManager {
    public static final String COURSE_PATH = "course.dat";
    private static CourseManager cMInstance;
    private List<Course> courseList = null;


    /**
     * Constructor for CourseManager. When CourseManager is first instantiated, it reads the list of courses from the database and stores it in an {@link ArrayList}
     */
    private CourseManager() {
        super(COURSE_PATH);

        courseList = (ArrayList<Course>) this.read(COURSE_PATH);

        if (courseList == null)
            courseList = new ArrayList<Course>();
    }

    /**
     * Gets an instance of CourseManager
     *
     * @return CourseManager instance
     */
    public static CourseManager getInstance() {
        if (cMInstance == null) {
            cMInstance = new CourseManager();
            return cMInstance;
        }
        return cMInstance;
    }

    /**
     * Writes the {@link #courseList} into the database
     * @return true if successful
     */
    public boolean save() {
        return this.write(courseList, COURSE_PATH);
    }


    /*=================================
                 ACCESSORS
     ==================================*/

    /**
     * @return All courses available for students to enroll in
     */
    public List getCourseList() {
        return courseList;
    }

    /**
     * @param courseId Gets the {@link Course} object by it's ID
     * @return {@link Course} object. <br>
     * null if not found
     */
    public Course getCourseByCourseId(String courseId) {
        if (courseList == null) {
            System.out.println("printAll(): List is empty");
            return null;
        }
        //System.out.println("indexList.size(): " + indexList.size());
        Course temp = null;
        for (int i = 0; i < courseList.size(); ++i) {

            temp = courseList.get(i);
            if (temp.getCourseId().equalsIgnoreCase(courseId))
                return temp;

        }
        return null;

    }

    /**
     * @param indexNo Gets the {@link Course} object by the index number it contains
     * @return {@link Course} object. <br>
     * null if not found
     */
    public Course getCourseByIndexNo(int indexNo) {
        if (courseList == null) {
            System.out.println("printAll(): List is empty");
            return null;
        }
        Course temp;
        for (int i = 0; i < courseList.size(); ++i) {
            temp = courseList.get(i);
            for (int j = 0; j < temp.getIndexList().size(); j++) {

                if (temp.getIndexList().get(j).getIndexNum() == indexNo)
                    return temp;
            }
        }
        return null;
    }

    /**
     * @param indexNo Gets the {@link Index} object by its index number
     * @return {@link Index} object. <br>
     * null if not found
     */
    public Index getIndexByIndexNo(int indexNo) {
        for (int i = 0; i < courseList.size(); ++i) {
            List<Index> tempIndexList = courseList.get(i).getIndexList();
            for (int j = 0; j < tempIndexList.size(); ++j) {
                if (indexNo == tempIndexList.get(j).getIndexNum())
                    return tempIndexList.get(j); //found
            }
        }
        return null; //can't find
    }

    /**
     * Gets the lecture timeslots of the Course specified
     *
     * @param course Course to retrieve the timeslots for
     * @return List of lecture {@link TimeSlot} for the course
     */
    public List<TimeSlot> getLecTimeSlot(Course course) {
        return course.getLecTimeSlotList();
    }

    /**
     * Get the list of indexes for the specified course
     *
     * @param course Course to get the list of indexes for
     * @return List of {@link Index} in the course
     */
    public List<Index> getIndexList(Course course) {
        return courseList.get(courseList.indexOf(course)).getIndexList();
    }

    /*=================================
                 MUTATORS
     ==================================*/

    /**
     * @param matricNo Matriculation number of the student who is enrolling into this index
     * @param indexNo  Index Number of the index the student is attempting to enrol into
     * @return returns 0 if an Error occured <br>
     *         returns -1 if added into waitlist <br>
     *         returns 1 if successfully enrolled into index <br>
     *         returns 2 if already enrolled in index<br>
     *         returns 3 if already in waitlist of the index
     */
    public int enrolInIndex(String matricNo, int indexNo) {
        //-1 - index full
        // 0 - something wrong
        // 1 - success
        // 2 - already in index
        // 3 - already in waitlist
        Index tempIndex = getIndexByIndexNo(indexNo);

        if (tempIndex == null)
            return 0;

        //Student already enrolled in this index
        if (tempIndex.checkIfStudentEnrolled(matricNo)) {
            return 2;
        } else if (tempIndex.checkIfStudentInWaitList(matricNo)) //Check if student is in waitlist
        {

            return 3;
            //TODO: Check if student is already in waitlist
        }
        int holdRes = tempIndex.enrolStudent(matricNo);
        save();
        return holdRes; //enrols the student into the index by Matric No

    }

    /**
     * Adds a course into the database
     *
     * @param courseId   Unique Course ID number
     * @param courseName Course name
     * @param faculty    Faculty in which the course is held
     * @return true if succesfully added
     */
    public boolean addCourse(String courseId, String courseName, String faculty)//for admin
    {
        //Note that writing of new data into DB is not done here!
        if (this.getCourseByCourseId(courseId) == null)
            if (courseList.add(new Course(courseId, courseName, faculty))) {
                save();
                return true;
            }
        return false;
    }

    /**
     * Adds a lecture time slot into this Course
     * @param course   Course to add timeslot into
     * @param day      Day of the lecture
     * @param startH   Start time (hour) (in 24 hours) of this lecture
     * @param startM   Start time (minutes) of this lecture
     * @param endH     End time (hour) (in 24 hours) of this lecture
     * @param endM     End time (minutes) of this lecture
     * @param location Location of the lecture
     * @return true if lecture is successfully added
     */
    public boolean addLecTimeSlot(Course course, TimeSlot.DAY day, int startH, int startM, int endH, int endM, String location) {
        if (courseList.get(courseList.indexOf(course)).addLecTimeSlot(day, startH, startM, endH, endM, location)) {
            save();
            return true;
        }
        return false;
    }


    /**
     * @param dCourse course to delete
     * @return true if successful
     */
    public boolean deleteCourse(Course dCourse) {
        if (courseList.remove(dCourse)) {
            save();
            return true;
        }
        return false;
    }

    /**
     * @param course   course to delete the lecture from
     * @param timeSlot timeslot of the lecture to delete
     * @return true if suceessful
     */
    public boolean deleteLecTimeSlot(Course course, TimeSlot timeSlot) {
        return course.deleteLectTimeSlot(timeSlot);
    }

    /**
     * @param course           course to add the index to
     * @param indexNum         index to create
     * @param maxNumOfStudetns maximum number of students the index can accomodate
     * @return true if successful
     */
    public boolean createIndex(Course course, int indexNum, int maxNumOfStudetns) {
        Course tempCourse = courseList.get(courseList.indexOf(course));
        if (tempCourse.addIndex(indexNum, maxNumOfStudetns)) {
            save();
            return true;
        }
        return false;
    }

    /**
     * @param course course to delete the index from
     * @param index  index number to delete
     * @return true if successful
     */
    public boolean deleteIndex(Course course, Index index) {
        if (courseList.get(courseList.indexOf(course)).deleteIndex(index)) {
            save();
            return true;
        }
        return false;
    }

    /**
     * Enrols the first student in waitlist
     *
     * @param indexNo index number containing the waitlist
     * @return matriculation number of the student that was enrolled
     * <i>null</i> if not successful
     */
    public String enrolFirstStudentInWaitlist(int indexNo) {
        Index tempIndex = getIndexByIndexNo(indexNo);

        if (tempIndex == null)
            return null;

        //If the first student in waitlist matches the matric no passed in


        String tempMatricNo = (String) tempIndex.getWaitList().get(0);

        if (tempMatricNo != null) {
            //remove student from waitlist
            tempIndex.getWaitList().remove(0);

            tempIndex.enrolStudent(tempMatricNo);

            return tempMatricNo; //Returns the matricNo of the student added into the index to STARS
        } else
            return null;
    }

    /**
     * This function drops a student from the index
     * @param matricNo      matriculation number of the student to drop
     * @param indexNo       index to drop student from
     * @param bypassWaitlist true if we do not wish to handle the waitlist
     * @return a string array of size 3. <br><br>
     * first index - SUCCESS, ERROR or HANDLE. HANDLE is an indication to handle the waitlist in UserManager <br><br>
     * second index - contains student's matriculation number to add into index <br><br>
     * third index - contains the index number to add the student matriculation number (specified in second index) to.
     */
    public String[] dropFromIndex(String matricNo, int indexNo, boolean bypassWaitlist) {
        Index tempIndex = getIndexByIndexNo(indexNo);

        String[] retStrArr = new String[3];

        //Index wasn't found
        if (tempIndex == null) {
            retStrArr[0] = "ERROR";
            return retStrArr;
        }
        //If the student MatricNo was found and successfully removed from the index
        if (tempIndex.withdrawStudent(matricNo, bypassWaitlist) == true) {
            //CHECK IF NEED TO HANDLE WAITLIST
            if (tempIndex.checkIfHandleWaitlist() == true) {
                List<String> studentWaitList = tempIndex.getWaitList();

                //Check if studentWaitlist 1 or more. 0 means no students in waitlist to push to index
                //Enrol student at the front of the waitlist. enrolInIndex returns 1 if successful
                if (studentWaitList.size() >= 1) {

                    String studentMatricNoFromWaitlist = enrolFirstStudentInWaitlist(indexNo);


                    if (studentMatricNoFromWaitlist == null) {
                        retStrArr[0] = "ERROR";
                        return retStrArr;
                    }

                    retStrArr[0] = "HANDLE";
                    retStrArr[1] = studentMatricNoFromWaitlist;
                    retStrArr[2] = Integer.toString(indexNo);

                    return retStrArr; //Notify STARS to make UserManager add IndexNo to Student and send email!
                }
            }

            retStrArr[0] = "SUCCESS";
            return retStrArr;
        }
        retStrArr[0] = "ERROR"; //Returns success if no need to handle waitlist
        save();
        return retStrArr;
    }

    /**
     * Print the details of all the indexes
     */
    public void printAllIndexDetails() {
        Index temp = null;
        for (int i = 0; i < courseList.size(); ++i) {
            printIndexDetails(courseList.get(i).getCourseId());
        }
    }

    /**
     * Prints all index details of a specific course Id
     *
     * @param courseId courseId details to print
     * @return 1 if successful <br>
     * -1 if not successful
     */
    public int printIndexDetails(String courseId) {
        Course tempCourse = getCourseByCourseId(courseId);
        if (tempCourse == null)
            return -1;

        List<Index> currentIndexList = tempCourse.getIndexList();
        for (int j = 0; j < currentIndexList.size(); ++j) {
            Index tempIndex = currentIndexList.get(j);

            //Prints index number of current course and enrolled list
            System.out.println("Index No: " + tempIndex.getIndexNum() + " | No. of students: " + tempIndex.getNumberOfEnrolledStudent());

            //Print Lab and Tut Details of the current Index
            List<TimeSlot> timeSlotList = tempIndex.getTutLabTimeSlotList();
            for (int x = 0; timeSlotList != null && x < timeSlotList.size(); ++x) {
                //LAB: 1400-1500hrs LOCATION

                System.out.println(timeSlotList.get(x).getType() + ": " + timeSlotList.get(x).getDay().toString() + " "
                        + timeSlotList.get(x).getStartTime() + " - " + timeSlotList.get(x).getEndTime() + "hrs "
                        + timeSlotList.get(x).getLocation());
            }//print fin

            System.out.println("");
        }

        return 1;
    }


}
