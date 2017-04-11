package com.OODPAssn1;

/**
 * An interface with a list of functions that the STARS UI must implement
 * */
public interface UserInterface
{

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
