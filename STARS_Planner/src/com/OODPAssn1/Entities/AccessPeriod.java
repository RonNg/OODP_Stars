package com.OODPAssn1.Entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by qingh on 07/04/2017.
 */
public class AccessPeriod implements Serializable {

    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;
    private Calendar startDate;
    Calendar endDate;


    public AccessPeriod(){

        //Set default access period for student to current month and year
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 30);
        this.setAccessPeriod(startDate, endDate);

    }



    //------------------------Method to get access period-------------------------------------------------------

    public Calendar getStartDate()
    {
        return startDate;

    }

    public Calendar getEndDate()
    {
        return endDate;

    }

//------------------------Method to set start date and end date of STARS availability-----------------------

    public boolean setAccessPeriod(Calendar startDate, Calendar endDate)
    {


        this.startDate = startDate;
        this.endDate = endDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        return true;
    }

}
