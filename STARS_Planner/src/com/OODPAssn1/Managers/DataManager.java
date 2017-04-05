package com.OODPAssn1.Managers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonah on 15/3/2017.
 */
public class DataManager
{
    private String IV;
    private String encryptionKey;

    private static final String cFilePath = System.getProperty("user.dir") + "\\STARS_Planner\\database\\"; // To specify location to store database files
    //private static final String cFilePath = System.getProperty("user.dir") + "\\database\\"; // To specify location to store database files


    public DataManager(final String IV, final String encryptionKey, final String filepath)
    {
        //IMPORTANT
        //AES only supports keys that are 16, 24 or 32 bytes.
        //However, Java default only supports 128-bit which is 16 bytes. This is the max without installing JCE
        //1 char is 1 byte

        //IV must be 16 bytes
        this.IV = IV;
        //encryptionKey must be 16 bytes
        this.encryptionKey = encryptionKey;

        File directory = new File(cFilePath);

        //Creates the directory
        if (!directory.exists())
        { // Checks if directory already exist
            if (!directory.mkdir()) // Creates directory if not available
            {
                System.out.println(cFilePath + filepath + " directory created");
            }
        }

        //Creates the file if none exists
        File file = new File(cFilePath + filepath);
        if (!file.exists())
        { // Checks if file already exist
            try
            {
                //Returns true if file does not exist and was created
                //false if exists already
                if (file.createNewFile()) // Creates file if not available
                {
                    System.out.println(filepath + " database was created");
                } else
                {
                    System.out.println(filepath + " database already exists");
                }
            } catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    protected List read(String filepath)
    {
        File file = new File(cFilePath + filepath);
        if ((!file.exists()))
        { // Check if file exist
            System.out.println("File not exist in: " + cFilePath + filepath);
            return null; // return null if file does not exist
        }

        List pDetails = null;
        FileInputStream fis;
        ObjectInputStream in;
        try
        {
            fis = new FileInputStream(cFilePath + filepath);
            in = new ObjectInputStream(fis);
            pDetails = (ArrayList) in.readObject();
            in.close();
            return pDetails; // return list if successful read
        }
        catch (IOException ex)
        {
            ex.printStackTrace();

        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }

        return null; // return null by default if error occurred
    }

    protected boolean write(List list, String filepath)
    {
        File directory = new File(cFilePath);
        File file = new File(cFilePath + filepath);
        if (!directory.exists())
        { // Checks if directory already exist
            if (!directory.mkdir()) // Creates directory if not available
                return false; // If fails return false
        }
        if (!file.exists())
        { // Checks if file already exist
            try
            {
                file.createNewFile(); // Creates file if not available
            } catch (IOException e)
            {
                return false; // If fails return false
            }
        }

        if (list != null)
        { // Checks if list is null

            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            try
            {
                fos = new FileOutputStream(cFilePath + filepath);
                out = new ObjectOutputStream(fos);
                out.writeObject(list); // Write entire list into file
                out.close();
                return true; // return true if write operation succeed
            } catch (IOException ex)
            {
                return false; // return false if error occurred
            }
        }
        return false; // return false by default
    }


    public void setIV(String IV)
    {
        this.IV = IV;
    }

    public void setEncryptionKey(String encryptionKey)
    {
        this.encryptionKey = encryptionKey;
    }

    protected String getIV()
    {
        return IV;
    }

    protected String getEncryptionKey()
    {
        return encryptionKey;
    }

}
