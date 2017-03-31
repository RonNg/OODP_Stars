package com.OODPAssn1.Managers;

import com.OODPAssn1.Entities.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonah on 15/3/2017.
 */
public class CourseManager extends DataManager {

    private static CourseManager cMInstance;
    private DataManager dM;

    public final static String COURSE_PATH = "course.dat";
    private List<Course> courseList = null;

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
            if(courseList == null){
                courseList = new ArrayList<Course>();
            }
        }
        isInitAlready = true;
    }

    public boolean saveAll(){
        return dM.write(courseList,COURSE_PATH);
    }

    public boolean createCourse(String courseId, String courseName, String faculty){
        return courseList.add(new Course(courseId,courseName,faculty));
    }

    /*public boolean deleteCourse(String courseId){

    }*/


}
