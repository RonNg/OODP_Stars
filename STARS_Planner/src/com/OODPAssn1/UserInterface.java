package com.OODPAssn1;

/**
 * Created by qingh on 09/04/2017.
 */
public interface UserInterface {

//---------------------Methods for displaying of menu in UI------------------------

    int loginScreen();

    void studentMenu();

    void adminMenu();

//---------------------Methods for input validation---------------------------------

    int getInt();

    String getString();

    boolean getYesNo();

    int getTime();

    String getCourseId();
}
