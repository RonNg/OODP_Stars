package com.OODPAssn1;

import com.OODPAssn1.Entities.Course;
import com.OODPAssn1.Entities.Index;
import com.OODPAssn1.Managers.CourseManager;

/**
 * Created by jonah on 31/3/2017.
 */
public class TEST {

<<<<<<< HEAD
    public static void Test(String[] args) {
        Course cs = new Course("CE0001","NOIDEA","SCSE");
        cs.addIndex(1000);
        cs.addIndex(1001);
        cs.deleteIndex(1001);
        cs.deleteIndex(10003);
=======
    public static void main(String[] args) {

>>>>>>> 5c5039cd6045e2cf3e06758c4a53c61188095186

        CourseManager cM = CourseManager.getInstance();
        cM.createCourse("CE0001","EnS", "SCSE");
        cM.getCourseList();
        //cM.saveAll();
        Index in = new Index(20001,50);
        //in.addLabTimeSlot('T',13,30,14,30,"LT1A");
        //in.addLabTimeSlot('W',3,30,4,30,"LT11");
        in.deleteTutLabTimeSlot(in.getTutLabTimeSlotList().get(0));
    }
}
