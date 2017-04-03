package com.OODPAssn1.Entities;

import com.OODPAssn1.Entities.User;

/**
 * Created by jonah on 15/3/2017.
 */
public class Admin extends User
{

    private String name;


    public Admin(String name, String email, String userName, String passWord){

        this.name = name;
        this.email = email;
        this.username = userName;
        this.password = passWord;
        type = USER_TYPE.ADMIN;
    }

    //TODO: Implement SET GET METHOD
}
