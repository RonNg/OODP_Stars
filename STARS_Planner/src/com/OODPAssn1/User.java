package com.OODPAssn1;

import java.io.Serializable;

/**
 * Created by jonah on 15/3/2017.
 */
public abstract class User implements Serializable
{
    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;

    public enum GENDER { MALE, FEMALE }
    private enum USER_TYPE { STUDENT, ADMIN }

    protected String username;
    protected String password;
    protected Boolean isPasswordEncrypted = false;

    //Basic Info
    protected String name;
    protected String matricNo;
    protected String email;
    protected int contact;
    protected GENDER gender;
    protected String nationality;

    protected USER_TYPE type; // S = Student_temp A = Admin


    /*=====================================

                    MUTATORS

    ======================================*/

    /**
     * Sets username of the User
     * @param username String variable to set username of User
     */
    protected void setUsername( String username ) { this.username = username; }

    /**
     * Sets password of User
     * @param password String variable to set password of User
     */
    protected void setPassword( String password) { this.password = password; }

    /**
     * Toggles the boolean to keep track of whether the password is encrpyted
     *
     */
    protected void toggleEncrypted () { this.isPasswordEncrypted = !this.isPasswordEncrypted; }


    /*=====================================

                    ACCESSORS

    ======================================*/
    /**
     * Returns username of the User
     * @return String username
     */
    public String getUsername() { return username; }

    /**
     * Returns password of the User
     * @return String password
     */
    public String getPassword() { return password; }

    /**
     * Returns type of the User
     * @return USER_TYPE type
     */
    public USER_TYPE getType () { return type; }

    /**
     * Returns boolean isPasswordEncrypted
     * @return Boolean isPasswordEncrypted
     */
    public boolean getIsPasswordEncrpyted () {return isPasswordEncrypted; }
}
