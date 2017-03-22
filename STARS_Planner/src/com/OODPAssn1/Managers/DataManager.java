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

    public DataManager ()
    {
        //IMPORTANT
        //AES only supports keys that are 16, 24 or 32 bytes.
        //However, Java default only supports 128-bit which is 16 bytes. This is the max without installing JCE
        //1 char is 1 byte

        //IV must be 16 bytes
        this.IV = "NotPokemonIVFour";
        //encryptionKey must be 16 bytes
        this.encryptionKey = "GameOverGameOver";
    }

    public List read(String filepath)
    {
        //Checks to see if the file is empty.
        //if it is, return a null studentList.
        File file = new File(filepath);
        if (file.length() == 0)
            return null;

        List pDetails = null;
        FileInputStream fis;
        ObjectInputStream in ;
        try
        {
            fis = new FileInputStream(filepath);
            in = new ObjectInputStream(fis);
            pDetails = (ArrayList) in.readObject();
            in.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }

        return pDetails;
    }

    public void write(List list, String filepath)
    {
        if (list != null)
        {
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            try
            {
                fos = new FileOutputStream(filepath);
                out = new ObjectOutputStream(fos);

                out.writeObject(list); //Write entire studentList into file

                out.close();

            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }



    public void setIV (String IV) {this.IV = IV;}
    public void setEncryptionKey(String encryptionKey) {this.encryptionKey = encryptionKey;}

    protected String getIV () {return IV;}
    protected String getEncryptionKey() {return encryptionKey;}

}
