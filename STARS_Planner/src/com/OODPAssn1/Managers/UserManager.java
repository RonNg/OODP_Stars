package com.OODPAssn1.Managers;

import com.OODPAssn1.AESEncrypter;
import com.OODPAssn1.Entities.Admin;
import com.OODPAssn1.Entities.Student;
import com.OODPAssn1.Entities.User;

import java.util.ArrayList;
import java.util.List;


/**
 * This class is incharge of handling everything {@link Student} related including encryption of the password.
 *
 * @author Tweakisher
 */
public class UserManager extends DataManager {
    private static UserManager instance;

    public final static String USER_PATH = "user.dat";
    private List<User> userList = null;

    private boolean isInitAlready = false;

    private UserManager() {
        //Set Encryption Key
        this.setEncryptionKey("StudentKStudentK"); //16 Bytes long
        this.setIV("NotPokemonIVFour"); //16 Bytes long
        init();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();

            return instance;
        }
        return instance;
    }

    public void decryptPassForLogin(){//Accessor for STARS to decrypt for log-out then log-in operation

        this.decryptPassword();
    }

    public int init() {
        if (isInitAlready) {
            System.out.println("UserManager is being reinitialised again!");
            return -1;
        }

        userList = (ArrayList) this.read(USER_PATH);
        if (userList == null) {
            System.out.println("userList read in from file is null");
            userList = new ArrayList();
        }

        this.decryptPassword(); //Decrypts all passwords retrieved from database


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


    public boolean saveData() {//Return true if save succeed
        this.encryptPassword(); //Encrypt All Passwords before writing
        return this.write(userList, USER_PATH);
    }

    private void encryptPassword() {

        for (int i = 0; i < userList.size(); ++i) {
            //Student's password is already encrypted
            if (userList.get(i).getIsPasswordEncrpyted())
                continue;

            String tempPass = userList.get(i).getPassword();

            try {
                String encryptedPass = AESEncrypter.encrypt(tempPass, this.getEncryptionKey(), this.getIV());

                userList.get(i).setPassword(encryptedPass);
                userList.get(i).toggleEncrypted();
            } catch (Exception e) {
                //System.out.println(e.getMessage());
                //Works but throws an error. Dunno why.
            }
        }
    }

    private void decryptPassword() {
        for (int i = 0; i < userList.size(); ++i) {
            //Student's password is already decrypted
            if (!userList.get(i).getIsPasswordEncrpyted())
                continue;

            String tempPass = userList.get(i).getPassword();

            try {
                String decryptedPass = AESEncrypter.decrypt(tempPass, this.getEncryptionKey(), this.getIV());

                userList.get(i).setPassword(decryptedPass);
                userList.get(i).toggleEncrypted();
            } catch (Exception e) {
                //System.out.println(e.getMessage());
                //Works but throws an error. Dunno why.
            }
        }

        //After everything is decrypted, save the password.
        //userList.
    }


    /**
     * Searches {@link List} for {@link Student} by name and returns the {@link List} index if matched
     *
     * @return index of the student if found in List.
     * <p>returns -1 if student could not be found</p>
     */
    public int findStudent(String name) {
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

//---------------------------------Method for authentication into STARS-------------------------------------------------
// - Return the logged-on user if authentication succeeds
// - Return null if authentication fail or User not found in DB

    public User authenticateUser(String userName, String passWord) {
        User user;
        for (int i = 0; i < userList.size(); ++i) {
            user = userList.get(i);
            if (userName.equals(user.getUsername())) {
                if (!user.getPassword().equals(passWord)) {
                    System.out.println("Password incorrect!");
                    return null;
                } else
                    return user;
            }
        }
        System.out.println("User not found in database!");
        return null;//

    }

//-------------------Method to add student into UserList-------------------------------
//Note that writing of new data into DB is not done here!


    public void addStudent(String name, String email, String matricNo,
                           int contact, Student.GENDER gender, String nationality,
                           String username, String password) {
        try {
            userList.add(new Student(name, email, matricNo,
                    contact, gender, nationality,
                    username, password));
        } catch (Exception e) {
            System.out.println("addStudent() Exception caught: " + e.getMessage());
        }

        /*
        if(this.saveData())
            System.out.println(name + " successfully added to STARS!");
        else
            System.out.println("writing failed");
        */
    }

    public void addAdmin(final Admin admin) {
        try {
            userList.add(admin);
        } catch (Exception e) {
            System.out.println("addAdmin() Exception caught: " + e.getMessage());
        }
    }


    public void addCourseIndex (User student)
    {

    }


//----------------------Methods for debugging purpose only. Remove for production--------------------------------------

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


}
