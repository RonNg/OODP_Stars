package com.OODPAssn1.Entities;

import java.io.Serializable;

/**
 * <i>User</i> class is a <i>Serializable </i>container class used to store user information.
 * This class is the base class for <i>Admin</i> and <i>User</i> classes.
 */
public abstract class User implements Serializable {
    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;
    protected String username;
    protected String password;
    //Basic Info
    protected String email;
    protected USER_TYPE type; // S = Student A = Admin

    /**
     * Returns username of the User
     *
     * @return String username
     */
    public String getUsername() {
        return username;
    }

    /*=====================================

                    MUTATORS

    ======================================*/

    /**
     * Sets username of the User
     *
     * @param username String variable to set username of User
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns password of the User
     *
     * @return String password
     */
    public String getPassword() {
        return password;
    }


    /*=====================================

                    ACCESSORS

    ======================================*/

    /**
     * Sets password of User
     *
     * @param password String variable to set password of User
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns email of the user
     *
     * @return String email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Returns type of the User
     *
     * @return USER_TYPE type
     */
    public USER_TYPE getType() {
        return type;
    }

    public enum USER_TYPE {STUDENT, ADMIN}

}
