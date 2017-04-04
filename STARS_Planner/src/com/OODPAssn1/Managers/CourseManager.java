package com.OODPAssn1.Managers;

import com.OODPAssn1.Entities.Course;
import com.OODPAssn1.Entities.Index;
import com.OODPAssn1.Entities.TimeSlot;

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
            dM = new DataManager();
            courseList = (ArrayList<Course>)dM.read(COURSE_PATH);
            indexList = (ArrayList<Index>)dM.read(INDEX_PATH);
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
        if(dM.write(courseList,COURSE_PATH) && dM.write(indexList,INDEX_PATH)){
            return true;
        }
        return false;
    }

    //---------Course methods---------

    public List<Course> getCourseList(){
        return courseList;
    }

    public boolean createCourse(String courseId, String courseName, String faculty){
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

    //---------Index methods---------

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





}
