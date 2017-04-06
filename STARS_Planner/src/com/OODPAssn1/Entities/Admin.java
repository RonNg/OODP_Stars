package com.OODPAssn1.Entities;

import java.io.Serializable;

/**
 * Created by jonah on 15/3/2017.
 */
public class Admin extends User implements Serializable
{

    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;

    private String name;


    public Admin(String name, String email, String userName, String passWord){

        this.name = name;
        this.email = email;
        this.username = userName;
        this.password = passWord;
        type = USER_TYPE.ADMIN;
    }
}
