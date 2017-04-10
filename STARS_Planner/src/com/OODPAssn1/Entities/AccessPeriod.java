package com.OODPAssn1.Entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * <class>AccessPeriod</class> is a <class>Serializable</class> class that acts as a
 * container for the access period start and end date variables. The start and end dates are
 * declared constructed using the <class>Calendar </class> class
 */
public class AccessPeriod implements Serializable
{

    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;
    private Calendar startDate;
    Calendar endDate;


    /**
     * Initialises start and end date to default values that should be overwritten.
     */
    public AccessPeriod ()
    {

        //Set default access period for student to current month and year
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 30);
        this.setAccessPeriod(startDate, endDate);
    }

    /**
     * Gets the start date of the access period.
     * @return Start date of access period.
     */
    public Calendar getStartDate ()
    {
        return startDate;

    }

    /**
     * Gets the end date of the access period.
     * @return End date of access period.
     */
    public Calendar getEndDate ()
    {
        return endDate;

    }

    /**
     * Sets the access period's start and end dates.
     * @param startDate Start date using Calendar object
     * @param endDate End date using Calendar object
     * @return true - success </p>
     *         false - fail
     */
    public boolean setAccessPeriod (Calendar startDate, Calendar endDate)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");

        //TODO: needs a return false statement
        return true;
    }

}
