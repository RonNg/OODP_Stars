package com.OODPAssn1.Managers;

import com.OODPAssn1.Entities.Course;
import com.OODPAssn1.Entities.Index;
import com.OODPAssn1.Entities.TimeSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonah on 15/3/2017.
 */
public class CourseManager extends DataManager
{

    private static CourseManager cMInstance;

    public static final String COURSE_PATH = "course.dat";
    //public static final String INDEX_PATH = "index.dat";
    private List<Course> courseList = null;


    private boolean isInitAlready = false;


    private CourseManager()
    {
       super(COURSE_PATH);

        courseList = (ArrayList<Course>) this.read(COURSE_PATH);

        if (courseList == null)
            courseList = new ArrayList<Course>();
    }

    public static CourseManager getInstance()
    {
        if (cMInstance == null)
        {
            cMInstance = new CourseManager();
            return cMInstance;
        }
        return cMInstance;
    }

    public boolean save()
    {
        return this.write(courseList, COURSE_PATH);
    }

    //---------Course methods---------

    public List getCourseList()
    {
        return courseList;
    }

    public boolean addCourse (String courseId, String courseName, String faculty)
    {
        //Note that writing of new data into DB is not done here!
        return courseList.add(new Course(courseId, courseName, faculty));
    }

    public boolean deleteCourse(Course dCourse)
    {
        return courseList.remove(dCourse);
    }

    public boolean addLecTimeSlot(Course course, TimeSlot.DAY day, int startH, int startM, int endH, int endM, String location)
    {
        return courseList.get(courseList.indexOf(course)).addlecTimeSlot(day, startH, startM, endH, endM, location);
    }

    public boolean deleteLecTimeSlot(Course course, TimeSlot timeSlot)
    {
        return courseList.get(courseList.indexOf(course)).deleteLectTimeSlot(timeSlot);
    }

    public Course findCourseById(String courseId)
    {
        if (courseList == null)
        {
            System.out.println("printAll(): List is empty");
            return null;
        }
        //System.out.println("indexList.size(): " + indexList.size());
        Course temp = null;
        for (int i = 0; i < courseList.size(); ++i)
        {

            temp = courseList.get(i);
            if (temp.getCourseId().equalsIgnoreCase(courseId))
                return temp;

        }
        return null;

    }



    //---------Index methods---------

    public List<Index> getIndexList(Course course)
    {
        return courseList.get(courseList.indexOf(course)).getIndexList();
    }

    public Index findByIndex (int indexNo)
    {
        for(int i = 0; i < courseList.size(); ++ i)
        {
            List<Index> tempIndexList = courseList.get(i).getIndexList();
            for(int j = 0; j < tempIndexList.size(); ++ j)
            {
                if(indexNo == tempIndexList.get(i).getIndexNum())
                    return tempIndexList.get(i); //found
            }
        }
        return null; //can't find
    }


    public boolean createIndex(Course course, int indexNum, int maxNumOfStudetns)
    {
        return courseList.get(courseList.indexOf(course)).addIndex(indexNum, maxNumOfStudetns);
    }

    public boolean deleteIndex(Course course, Index index)
    {
        return courseList.get(courseList.indexOf(course)).deleteIndex(index);
    }


    /**
     *
     * @param matricNo Matriculation number of the student who is enrolling into this index
     * @param indexNo Index Number of the index the student is attempting to enrol into
     * @return Added into waitlist - -1 <br>
     *         Successfully enrolled into index - 1 <br>
     *         Already enrolled in index - 2 <br>
     *         Error occured - 0
     *
     */
    public int enrolInIndex(String matricNo, int indexNo)
    {
        // 1 - success
        // 0 - something wrong
        // -1 - index full
        // 2 - already in index
        Index tempIndex = findByIndex(indexNo);

        if(tempIndex == null)
            return 0;

        //Student already enrolled in this index
        if (tempIndex.checkIfStudentEnrolled(matricNo))
        {

            return 2;
        }
        // else if () //Check if student is in waitlist
        // {
        //      //TODO: Check if student is already in waitlist
        // }

        return tempIndex.enrolStudent(matricNo); //enrols the student into the index by Matric No

    }

    public String withdrawFromIndex(String matricNo, Course course, Index index)
    { // "SUCCESS" - Success "ERROR" - something wrong OTHERS - matric number of student to send email to
        Course toChange = courseList.get(courseList.indexOf(course));
        return toChange.getIndexList().get(toChange.getIndexList().indexOf(index)).withdrawStudent(matricNo);
    }

//----------------------------------Method for debugging purposes. Remove for production--------------------------------

    public int printAllCourse()
    {
        if (courseList == null)
        {
            System.out.println("printAll(): List is empty");
            return 0;
        }

        Course temp = null;
        for (int i = 0; i < courseList.size(); ++i)
        {
            temp = courseList.get(i);

            String indexString = "No Index(s)";
            List<Index> indexList = temp.getIndexList();
            for(int j = 0; indexList != null && j < indexList.size(); ++ j)
            {
                if(j < 1)
                    indexString = Integer.toString(indexList.get(j).getIndexNum());
                else
                    indexString  = indexString + ", " + Integer.toString(indexList.get(j).getIndexNum());
            }

            System.out.print("ID: " + temp.getCourseId() + " | Name: " + temp.getCourseName() + " | Index(s): " + indexString);
        }
        return 1;
    }

    public int printAllIndexDetails()
    {
        Index temp = null;
        for (int i = 0; i < courseList.size(); ++i)
        {
            printIndexDetails(courseList.get(i).getCourseId());
        }
        return 1;
    }

    /**
     * Prints all index details of a specific course Id
     * @return
     */
    public int printIndexDetails ( String courseId )
    {
        Course tempCourse = findCourseById(courseId);
        if(tempCourse == null)
            return -1;

        List<Index> currentIndexList = tempCourse.getIndexList();
        for (int j = 0; j < currentIndexList.size(); ++j)
        {
            Index tempIndex = currentIndexList.get(j);

            //Prints index number of current course and enrolled list
            System.out.println("Index No: " + tempIndex.getIndexNum() + " | No. of students: " + tempIndex.getNumberOfEnrolledStudent()) ;

            //Print Lab and Tut Details of the current Index
            List<TimeSlot> timeSlotList = tempIndex.getTutLabTimeSlotList();
            for(int x = 0; timeSlotList != null && x < timeSlotList.size(); ++x )
            {
                //LAB: 1400-1500hrs LOCATION
                System.out.println(timeSlotList.get(x).getType() + ": "
                        + timeSlotList.get(x).getStartTime() + " - " +  timeSlotList.get(x).getEndTime() + "hrs "
                        + timeSlotList.get(x).getLocation());
            }//print fin

            System.out.println("");
        }

        return 1;
    }


}
