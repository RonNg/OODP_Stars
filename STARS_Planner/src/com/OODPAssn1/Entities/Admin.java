package com.OODPAssn1.Entities;

import java.io.Serializable;

/**
 * <i>Admin</i> class inherits User. It differs from the <i>User</i> in that it doesn't store
 * matriculation number, phone number and gender. the STARS application checks the instance of User
 * and grants access depending on whether its an Admin or User.
 */
public class Admin extends User implements Serializable {

    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;

    private String name;

    /**
     * Constructor to create an Admin object
     *
     * @param name     name of the admin
     * @param email    email of the admin
     * @param userName username of the admin
     * @param password password of the admin
     */
    public Admin(String name, String email, String userName, String password) {

        this.name = name;
        this.email = email;
        this.username = userName;
        this.password = password;
        type = USER_TYPE.ADMIN;
    }
}
