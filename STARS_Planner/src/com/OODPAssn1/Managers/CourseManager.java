package com.OODPAssn1.Managers;

import com.OODPAssn1.Entities.Course;
import com.OODPAssn1.Entities.Index;
import com.OODPAssn1.Entities.TimeSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonah on 15/3/2017.
 */
public class CourseManager extends DataManager {

    private static CourseManager cMInstance;

    public static final String COURSE_PATH = "course.dat";
    private List<Course> courseList = null;

    private CourseManager() {
        super(COURSE_PATH);

        courseList = (ArrayList<Course>) this.read(COURSE_PATH);

        if (courseList == null)
            courseList = new ArrayList<Course>();
    }

    public static CourseManager getInstance() {
        if (cMInstance == null) {
            cMInstance = new CourseManager();
            return cMInstance;
        }
        return cMInstance;
    }

    public boolean save() {
        return this.write(courseList, COURSE_PATH);
    }

    //---------Course methods---------

    public List getCourseList() {
        return courseList;
    }

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

    public boolean deleteCourse(Course dCourse) {
        if (courseList.remove(dCourse)) {
            save();
            return true;
        }
        return false;
    }

    public boolean addLecTimeSlot(Course course, TimeSlot.DAY day, int startH, int startM, int endH, int endM, String location) {
        if (courseList.get(courseList.indexOf(course)).addLecTimeSlot(day, startH, startM, endH, endM, location)) {
            save();
            return true;
        }
        return false;
    }

    public boolean deleteLecTimeSlot(Course course, TimeSlot timeSlot) {
        return course.deleteLectTimeSlot(timeSlot);
    }

    public List<TimeSlot> getLecTimeSlot(Course course){
        return course.getLecTimeSlotList();
    }





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


    //---------Index methods---------

    public List<Index> getIndexList(Course course) {
        return courseList.get(courseList.indexOf(course)).getIndexList();
    }

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


    public boolean createIndex(Course course, int indexNum, int maxNumOfStudetns) {
        Course tempCourse = courseList.get(courseList.indexOf(course));
        if (tempCourse.addIndex(indexNum, maxNumOfStudetns)) {
            save();
            return true;
        }
        return false;
    }

    public boolean deleteIndex(Course course, Index index) {
        if (courseList.get(courseList.indexOf(course)).deleteIndex(index)) {
            save();
            return true;
        }
        return false;
    }


    /**
     * @param matricNo Matriculation number of the student who is enrolling into this index
     * @param indexNo  Index Number of the index the student is attempting to enrol into
     * @return Error occured -> 0 <br>
     * Added into waitlist -> -1 <br>
     * Successfully enrolled into index -> 1 <br>
     * Already enrolled in index -> 2 <br>
     * Already in waitlist of the index -> 3
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

    public String enrolFirstStudentInWaitlist(String matricNo, int indexNo) {
        Index tempIndex = getIndexByIndexNo(indexNo);

        if (tempIndex == null)
            return null;

        //If the first student in waitlist matches the matric no passed in
        if (tempIndex.getWaitList().get(0) == matricNo) {

            String tempMatricNo = (String) tempIndex.getWaitList().get(0);

            //remove student from waitlist
            tempIndex.getWaitList().remove(0);

            tempIndex.enrolStudent(tempMatricNo);

            return tempMatricNo; //Returns the matricNo of the student added into the index to STARS
        }

        return null;
    }

    /**
     * @param matricNo
     * @param indexNo
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
        //If the student MatricNo was found and succesfully removed from the index
        if (tempIndex.withdrawStudent(matricNo, bypassWaitlist) == true) {
            //CHECK IF NEED TO HANDLE WAITLIST
            if (tempIndex.checkIfHandleWaitlist() == true) {
                List<String> studentWaitList = tempIndex.getWaitList();

                //Check if studentWaitlist 1 or more. 0 means no students in waitlist to push to index
                //Enrol student at the front of the waitlist. enrolInIndex returns 1 if successful
                if (studentWaitList.size() >= 1) {

                    String studentMatricNoFromWaitlist = enrolFirstStudentInWaitlist(studentWaitList.get(0), indexNo);


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

//----------------------------------Method for debugging purposes. Remove for production--------------------------------

    public int printAllIndexDetails() {
        Index temp = null;
        for (int i = 0; i < courseList.size(); ++i) {
            printIndexDetails(courseList.get(i).getCourseId());
        }
        return 1;
    }

    /**
     * Prints all index details of a specific course Id
     *
     * @return
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
