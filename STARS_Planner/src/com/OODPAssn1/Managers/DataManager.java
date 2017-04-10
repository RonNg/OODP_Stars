package com.OODPAssn1.Managers;

import com.OODPAssn1.Entities.AccessPeriod;

import java.io.*;
import java.util.List;

/**
 * <i>DataManager</i> is a base class for any classes that wants to handle and manipulate data stored in a .dat file <br>
 * It stores and reads {@link Serializable} objects
 */
public class DataManager {
    private static final String cFilePath = System.getProperty("user.dir") + "\\STARS_Planner\\database\\"; // To specify location to store database files
    //private static final String cFilePath = System.getProperty("user.dir") + "\\database\\"; // To specify location to store database files


     /*=================================
                 ACCESSORS
     ==================================*/

    /**
     * Initialises the DataManager object which creates a "database" folder in the root directory of the application. <br>
     * In addition, a database file is created  in that folder based on the String passed into the constructor.
     *
     * @param filename The filename e.g. user.dat
     */
    public DataManager(final String filename) {
        File directory = new File(cFilePath);

        //Creates the directory
        if (!directory.exists()) { // Checks if directory already exist
            try {
                if (directory.mkdirs()) // Creates directory if not available
                {
                    System.out.println(cFilePath + " directory created");
                } else {
                    System.out.println(cFilePath + " directory not created");
                }
            } catch (SecurityException e) {
                e.printStackTrace();
                System.out.println("Program does not have enough privilege to create folder.");
            }

        }

        //Creates the file if none exists
        File file = new File(cFilePath + filename);
        if (!file.exists()) { // Checks if file already exist
            try {
                //Returns true if file does not exist and was created
                //false if exists already
                if (file.createNewFile()) // Creates file if not available
                {
                    System.out.println(filename + " database was created");
                } else {
                    System.out.println(filename + " database already exists");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Initialises the DataManager object which creates a "database" folder in the root directory of the application. <br>
     * In addition, a two database files isare created in that folder based on the Strings passed into the constructor.
     *
     * @param filepath1 first filename
     * @param filepath2 second filename
     */
    public DataManager(final String filepath1, final String filepath2)//Constructor for two files
    {
        File directory = new File(cFilePath);

        //Creates the directory
        if (!directory.exists()) { // Checks if directory already exist
            try {
                if (directory.mkdirs()) // Creates directory if not available
                {
                    System.out.println(cFilePath + " directory created");
                } else {
                    System.out.println(cFilePath + " directory not created");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Program does not have enough privilege to create folder.");
            }
        }

        //Creates the file if none exists
        File file1 = new File(cFilePath + filepath1);
        if (!file1.exists()) { // Checks if file already exist
            try {
                //Returns true if file does not exist and was created
                //false if exists already
                if (file1.createNewFile()) // Creates file if not available
                {
                    System.out.println(filepath1 + " database was created");
                } else {
                    System.out.println(filepath1 + " database already exists");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        File file2 = new File(cFilePath + filepath2);
        if (!file2.exists()) { // Checks if file already exist
            try {
                //Returns true if file does not exist and was created
                //false if exists already
                if (file2.createNewFile()) // Creates file if not available
                {
                    System.out.println(filepath2 + " database was created");
                } else {
                    System.out.println(filepath2 + " database already exists");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * @param filename filename to open and load the Serializable objects from
     * @return {@link Serializable} objects. Objects should be downcasted into the correct derived objects and stored into an ArrayList
     */
    protected Object read(String filename) {
        File file = new File(cFilePath + filename);
        if ((!file.exists())) { // Check if file exist
            System.out.println("File not exist in: " + cFilePath + filename);
            return null; // return null if file does not exist
        }

        Object pDetails = null;
        FileInputStream fis;
        ObjectInputStream in;
        try {
            fis = new FileInputStream(cFilePath + filename);
            in = new ObjectInputStream(fis);
            pDetails = in.readObject();
            in.close();
            return pDetails; // return list if successful read
        } catch (Exception ex) {
        }

        return null; // return null by default if error occurred
    }

    /**
     * @param list     List of objects to write into the database file
     * @param filename name of the database file
     * @return true if successful
     */
    protected boolean write(List list, String filename) {
        File directory = new File(cFilePath);
        File file = new File(cFilePath + filename);
        if (!directory.exists()) { // Checks if directory already exist
            if (!directory.mkdir()) // Creates directory if not available
                return false; // If fails return false
        }
        if (!file.exists()) { // Checks if file already exist
            try {
                file.createNewFile(); // Creates file if not available
            } catch (IOException e) {
                return false; // If fails return false
            }
        }

        if (list != null) { // Checks if list is null

            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            try {
                fos = new FileOutputStream(cFilePath + filename);
                out = new ObjectOutputStream(fos);
                out.writeObject(list); // Write entire list into file
                out.close();
                return true; // return true if write operation succeed
            } catch (IOException ex) {
                return false; // return false if error occurred
            }
        }
        return false; // return false by default
    }

    /**
     * @param aP       the AccessPeriod object to write into the database file
     * @param filename name of the database file
     * @return true if successful
     */
    protected boolean write(AccessPeriod aP, String filename) {
        File directory = new File(cFilePath);
        File file = new File(cFilePath + filename);
        if (!directory.exists()) { // Checks if directory already exist
            if (!directory.mkdir()) // Creates directory if not available
                return false; // If fails return false
        }
        if (!file.exists()) { // Checks if file already exist
            try {
                file.createNewFile(); // Creates file if not available
            } catch (IOException e) {
                return false; // If fails return false
            }
        }

        if (aP != null) { // Checks if list is null

            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            try {
                fos = new FileOutputStream(cFilePath + filename);
                out = new ObjectOutputStream(fos);
                out.writeObject(aP); // Write entire obj into file
                out.close();
                return true; // return true if write operation succeed
            } catch (IOException ex) {
                return false; // return false if error occurred
            }
        }
        return false; // return false by default
    }
}
