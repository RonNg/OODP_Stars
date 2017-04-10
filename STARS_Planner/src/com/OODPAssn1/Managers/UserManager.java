package com.OODPAssn1.Managers;

import com.OODPAssn1.Entities.AccessPeriod;
import com.OODPAssn1.Entities.Admin;
import com.OODPAssn1.Entities.Student;
import com.OODPAssn1.Entities.User;
import com.OODPAssn1.MD5Hasher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * This class is incharge of handling everything {@link Student} related including encryption of the password.
 *
 * @author Tweakisher
 */
public class UserManager extends DataManager {
    public final static String USER_PATH = "user.dat";
    public final static String ACCESS_Period_PATH = "accessPeriod.dat";
    private static UserManager instance;
    private List<User> userList = null;
    private AccessPeriod acccessPeriod = null;

    private boolean isInitAlready = false;

    private UserManager() {
        //super(USER_PATH);
        super(USER_PATH, ACCESS_Period_PATH);

        //Retrieves the user lists from the database
        userList = (ArrayList) this.read(USER_PATH);
        if (userList == null)
            userList = new ArrayList();

        //Retrieves the user lists from the database
        acccessPeriod = (AccessPeriod) this.read(ACCESS_Period_PATH);
        if (acccessPeriod == null)
            acccessPeriod = new AccessPeriod();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();

            return instance;
        }
        return instance;
    }

    /**
     * Searches {@link List} for {@link Student} by name and returns the {@link List} index if matched
     *
     * @return index of the student if found in List.
     * <p>returns -1 if student could not be found</p>
     */
    public int getStudentByName(String name) {
        Student temp = null;
        for (int i = 0; i < userList.size(); ++i) {
            if (userList.get(i) instanceof Student) {
                temp = (Student) userList.get(i);
                if (name == temp.getName())
                    return i;
            }
        }
        //Could not be found
        return -1;
    }

    /**
     * @param matricNumber matriculation number of the
     * @return
     */
    public Student getStudentByMatricNo(String matricNumber) {

        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            //Downcasting will work for second half of the statement as it got through the first half
            if (userList.get(i).getType() == User.USER_TYPE.STUDENT && ((Student) user).getMatricNo().equals(matricNumber))
                return (Student) user;

        }
        return null;
    }

    public User authenticateUser(String userName, String password) {
        password = MD5Hasher.hash(password); //Need to hash the user input password as we are comparing hashes and not plaintext password
        User user;
        for (int i = 0; i < userList.size(); ++i) {
            user = userList.get(i);
            if (userName.equals(user.getUsername())) {
                if (!user.getPassword().equals(password)) {
                    System.out.println("Password incorrect!");
                    return null;
                } else
                    return user;
            }
        }
        System.out.println("User not found in database!");
        return null;//

    }

    public AccessPeriod getAccessPeriod() {
        return acccessPeriod;
    }


    public boolean save() {//Return true if save succeed
        return (this.write(userList, USER_PATH) && this.write(acccessPeriod, ACCESS_Period_PATH));
    }

    public boolean addStudent(String name, String email, String matricNo,
                              int contact, Student.GENDER gender, String nationality,
                              String username, String password) {

        if (this.getStudentByMatricNo(matricNo) != null) {
            return false;
        }

        try {
            String hashedPass = MD5Hasher.hash(password);
            userList.add(new Student(name, email, matricNo,
                    contact, gender, nationality,
                    username, hashedPass));
        } catch (Exception e) {
            System.out.println("addStudent() Exception caught: " + e.getMessage());
        }
        return this.save();
    }

    public boolean addAdmin(final String name, final String email, final String username, final String password) {
        try {
            String hashedPass = MD5Hasher.hash(password); //We need to hash this as we can't store password as plaintext
            userList.add(new Admin(name, email, username, hashedPass));
        } catch (Exception e) {
            return false;
        }


        return this.save();

    }

    public boolean changeAccessPeriod(Calendar start, Calendar end) {
        return acccessPeriod.setAccessPeriod(start, end);

    }


//----------------------Methods for debugging purpose only. Remove for production--------------------------------------

    public List<Student> getAllStudent() {
        List<Student> stList = new ArrayList<>();
        for (int n = 0; n < userList.size(); n++) {
            if (userList.get(n).getType() == User.USER_TYPE.STUDENT) {
                stList.add((Student) userList.get(n));
            }
        }
        return stList;
    }

    public boolean doesUserExist(String usrName) {
        for (int n = 0; n < userList.size(); n++) {
            if (userList.get(n).getUsername().equals(usrName))
                return true;
        }
        return false;
    }

    public int printAllUser() {
        if (userList == null) {
            System.out.println("printAll(): List is empty");
            return 0;
        }
        System.out.println("userList.size(): " + userList.size());
        User temp = null;
        for (int i = 0; i < userList.size(); ++i) {

            temp = userList.get(i);
            System.out.println("Name: " + temp.getUsername() + "   Password: " + temp.getPassword());
        }
        return 1;
    }

    /**
     * Prints information of every Student in the {@link #userList}
     *
     * @return returns 0 if List is empty a
     * <p>returns 1 if print successful</p>
     */
    public int print() {
        if (userList == null) {
            System.out.println("printAll(): List is empty");
            return 0;
        }
        System.out.println("userList.size(): " + userList.size());
        Student temp = null;
        for (int i = 0; i < userList.size(); ++i) {

            if (userList.get(i).getType() == User.USER_TYPE.STUDENT) {

                temp = (Student) userList.get(i);
                System.out.println("Name: " + temp.getName() + "   Password: " + temp.getPassword());
            }
        }

        return 1;
    }


}
