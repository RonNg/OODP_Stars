
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tweakisher
 */
public class Main 
{
    public static void main (String[] args) 
    {    
        StudentManager studentMag = new StudentManager();
       
   

         studentMag.printAll();
        
         int index = studentMag.retrieve("Ron");
         
         
         //TODO
         //Able to edit course index
         Student temp = (Student)studentMag.getList().get(index);
         temp.addCourse(1337);
         temp.addCourse(7331);

         
    }
}
