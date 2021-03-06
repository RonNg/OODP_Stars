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
       super("StudentKStudentK", "NotPokemonIVFour", COURSE_PATH);


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

    public boolean saveAll()
    {
        return this.write(courseList, COURSE_PATH);
    }

    //---------Course methods---------

    public List getCourseList()
    {
        return courseList;
    }

    public Course addCourse (String courseId, String courseName, String faculty)
    {
        //Note that writing of new data into DB is not done here!
        courseList.add(new Course(courseId, courseName, faculty));

        return courseList.get(courseList.size()-1); //Returns the course added so you can add the lecture time slot

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

    public boolean createIndex(Course course, int indexNum, int maxNumOfStudetns)
    {
        return courseList.get(courseList.indexOf(course)).addIndex(indexNum, maxNumOfStudetns);
    }

    public boolean deleteIndex(Course course, Index index)
    {
        return courseList.get(courseList.indexOf(course)).deleteIndex(index);
    }

    public int enrolInIndex(String matricNo, Course course, Index index)
    { // 1 - success 0 - something wrong -1 - index full // 2 - already in index
        Course toChange = courseList.get(courseList.indexOf(course));
        if (toChange.getIndexList().get(toChange.getIndexList().indexOf(index)).checkIfStudentEnrolled(matricNo))
        {
            return 2;
        }
        switch (toChange.getIndexList().get(toChange.getIndexList().indexOf(index)).enrolStudent(matricNo))
        {
            case -1:
                //TODO : Track the student is in the waitlist
                return -1;
            case 0:
                return 0;
            case 1:
                // add into student object registered course
                return 1;
        }
        return 0;
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
        System.out.println("courseList.size(): " + courseList.size());
        Course temp = null;
        int j = 0;
        for (int i = 0; i < courseList.size(); ++i)
        {

            temp = courseList.get(i);

            System.out.print("Name: " + temp.getCourseName() + "   ID: " + temp.getCourseId() + "   Index: ");

            while (j < temp.getIndexList().size())
            {
                System.out.print(temp.getIndexList().get(j) + ", ");
                j++;
            }
            System.out.println();

        }

        return 1;

    }

    public int printAllIndex()
    {
        Index temp = null;
        for (int i = 0; i < courseList.size(); ++i)
        {
            List<Index> currentIndexList = courseList.get(i).getIndexList();

            for (int j = 0; j < currentIndexList.size(); ++j)
            {
                //Prints index number of current course and enrolled list
                System.out.println("Index No: " + currentIndexList.get(j).getIndexNum() + " No. of students: "
                        + currentIndexList.get(j).getNumberOfEnrolledStudent());
            }
        }

        return 1;
    }


}
