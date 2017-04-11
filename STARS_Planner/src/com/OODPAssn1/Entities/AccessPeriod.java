package com.OODPAssn1.Entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * AccessPeriod is a Serializable class that acts as a container for the
 * Start and End dates of the access period
 */
public class AccessPeriod implements Serializable {
    //SerialVersionUID of this Class used to deconflict serialisation
    static final long serialVersionUID = 1L;
    Calendar endDate;
    private Calendar startDate;

    /**
     * Initialises start and end date to default values that should be overwritten.
     */
    public AccessPeriod() {

        //Set default access period for student to current month and year
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 30);
        this.setAccessPeriod(startDate, endDate);
    }

    /**
     * Gets the start date of the access period.
     *
     * @return Start date of access period.
     */
    public Calendar getStartDate() {
        return startDate;

    }

    /**
     * Gets the end date of the access period.
     *
     * @return End date of access period.
     */
    public Calendar getEndDate() {
        return endDate;

    }

    /**
     * Sets the access period's start and end dates.
     *
     * @param startDate Start date using Calendar object
     * @param endDate   End date using Calendar object
     * @return true if successfully set
     */
    public boolean setAccessPeriod(Calendar startDate, Calendar endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");

        //TODO: needs a return false statement
        return true;
    }

}
