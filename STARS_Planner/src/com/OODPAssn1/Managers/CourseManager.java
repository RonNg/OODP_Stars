package com.OODPAssn1.Managers;

import com.OODPAssn1.Entities.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonah on 15/3/2017.
 */
public class CourseManager extends DataManager {

    private static CourseManager cMInstance;
    private DataManager dM;

    public static final String COURSE_PATH = "course.dat";
    public static final String INDEX_PATH = "index.dat";
    private List<Course> courseList = null;
    private List<Index> indexList = null;

    private boolean isInitAlready = false;

    public static CourseManager getInstance(){
        if(cMInstance == null){
            cMInstance = new CourseManager();
            return cMInstance;
        }
        return cMInstance;
    }

    private CourseManager(){
        init();
    }

    private void init(){
        if(!isInitAlready){
            //dM = new DataManager();
            courseList = (ArrayList<Course>)this.read(COURSE_PATH);
            indexList = (ArrayList<Index>)this.read(INDEX_PATH);
            if(courseList == null){
                courseList = new ArrayList<Course>();
            }
            if(indexList == null){
                indexList = new ArrayList<Index>();
            }
        }
        isInitAlready = true;
    }

    public boolean saveAll(){
        boolean sCheck = false;

        if(this.write(courseList,COURSE_PATH) && this.write(indexList,INDEX_PATH)){
            return true;
        }
        return false;
    }

    //---------Course methods---------

    public List<Course> getCourseList(){
        return courseList;
    }

    public boolean createCourse(String courseId, String courseName, String faculty){
        //Note that writing of new data into DB is not done here!
        return courseList.add(new Course(courseId,courseName,faculty));
    }

    public boolean deleteCourse(Course dCourse){
        return courseList.remove(dCourse);
    }

    public boolean addLecTimeSlot(Course course,char day, int startH, int startM, int endH, int endM, String location){
        return courseList.get(courseList.indexOf(course)).addlecTimeSlot(day,startH,startM,endH,endM,location);
    }

    public boolean deleteLecTimeSlot(Course course, TimeSlot timeSlot){
        return courseList.get(courseList.indexOf(course)).deleteLectTimeSlot(timeSlot);
    }

    public Course findCourseById(String courseId){

        if (courseList == null) {
            System.out.println("printAll(): List is empty");
            return null;
        }
        //System.out.println("indexList.size(): " + indexList.size());
        Course temp = null;
        for (int i = 0; i < courseList.size(); ++i) {

            temp = courseList.get(i);
            if(temp.getCourseId().equalsIgnoreCase(courseId))
                return temp;

        }
        return null;

    }



    //---------Index methods---------

<<<<<<< HEAD

    public boolean printIndexOfCourse(Course c){

        if(c==null)
            return false;

        List temp = c.getIndexNumberList();
        int j = 0;
        while(j != temp.size()){

            System.out.print(temp.get(j) + "  ");
            j++;

        }
        System.out.println();
        return true;
    }

    public List<Index> getIndexList(){
        return indexList;
    }

    public boolean addIndex(Course course,int indexNum, int maxNumOfStudents){//Create new index and assign it to course
        if(indexList.add(new Index(indexNum,maxNumOfStudents))){
=======
    public List<Index> getAllIndexList(){
        return indexList;
    }

    public List<Index> getIndexList(Course course){
        List<Index> courseFiltered = new ArrayList<Index>();
        List<Integer> indexListC = courseList.get(courseList.indexOf(course)).getIndexNumberList();
        for(int n = 0; n < indexListC.size(); n++){
            for(int m = 0; m < indexList.size(); m++){
                if(indexList.get(m).getIndexNum()==indexListC.get(n)){
                    courseFiltered.add(indexList.get(m));
                    break;
                }
            }
        }
        return courseFiltered;
    }

    public boolean addIndex(Course course,int indexNum, int maxNumOfStudetns){
        if(indexList.add(new Index(indexNum,maxNumOfStudetns))){
>>>>>>> CourseManager
            return courseList.get(courseList.indexOf(course)).addIndex(indexNum);
        }
        return false;
    }

    public boolean deleteIndex(Course course, Index index){
        if(indexList.remove(index)){
            return courseList.get(courseList.indexOf(course)).deleteIndex(index.getIndexNum());
        }
        return false;
    }

    public int enrolInIndex(String matricNo,Index index){ // 1 - Success 0 - something wrong -1 - index full
        switch (indexList.get(indexList.indexOf(index)).enrolStudent(matricNo)){
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

    public boolean withdrawFromIndex(String matricNo, Index index){ // 1 - Success 0 - something wrong -1 - another student in waitlist enrolled
        String response = indexList.get(indexList.indexOf(index)).withdrawStudent(matricNo);
        switch (response){
            case "ERROR":
                return false;
            case "SUCCESS":
                return true;
            default:
                // Send email to student just enrolled
                return true;
        }
    }


//----------------------------------Method for debugging purposes. Remove for production--------------------------------

    public int printAllCourse(){

        if (courseList == null) {
            System.out.println("printAll(): List is empty");
            return 0;
        }
        System.out.println("courseList.size(): " + courseList.size());
        Course temp = null;
        int j = 0;
        for (int i = 0; i < courseList.size(); ++i) {

            temp = courseList.get(i);
            System.out.print("Name: " + temp.getCourseName() + "   ID: " + temp.getCourseId() + "   Index: " );

            while(j < temp.getIndexNumberList().size()){
                System.out.print(temp.getIndexNumberList().get(j) + ", ");
                j++;
            }
            System.out.println();

        }

        return 1;

    }

    public int printAllIndex(){

        if (indexList == null) {
            System.out.println("printAll(): List is empty");
            return 0;
        }
        System.out.println("indexList.size(): " + indexList.size());
        Index temp = null;
        for (int i = 0; i < indexList.size(); ++i) {

            temp = indexList.get(i);
            System.out.println("Index no.: " + temp.getIndexNum() + "   ID: " + temp.getNumberOfEnrolledStudent());

        }

        return 1;

    }


    public boolean createIndex(int indexNum, int maxNumStudent){

        return indexList.add(new Index(indexNum, maxNumStudent));

    }




}
