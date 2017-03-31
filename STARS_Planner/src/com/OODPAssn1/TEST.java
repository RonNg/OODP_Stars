package com.OODPAssn1;

import com.OODPAssn1.Entities.Course;
import com.OODPAssn1.Managers.CourseManager;

/**
 * Created by jonah on 31/3/2017.
 */
public class TEST {

    public static void main(String[] args) {
        Course cs = new Course("CE0001","NOIDEA","SCSE");
        cs.addIndex(1000);
        cs.addIndex(1001);
        cs.deleteIndex(1001);
        cs.deleteIndex(10003);

        CourseManager cM = CourseManager.getInstance();
        cM.saveAll();
    }
}
