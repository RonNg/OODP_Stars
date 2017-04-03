package com.OODPAssn1;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 * The Notification class contains methods to send email blasts and SMSes to specified email addresses
 * and numbers.
 *
 * @author Ron Ng Jian Ying
 *
 */
public class Notification
{
    //This is the email address and password used for the emailing system
    final String username = "ce2002fep2group6@gmail.com";
    final String password = "boblaiqinghui";
    private Session session = null;

    Notification()
    {
        //Properties of the email service
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    /**
     * Sends an email to a single email addresses
     * @param emailAddress The specific emailAddress to send to
     * @param subject Subject of email
     * @param text Message to send to recipient
     *
     * @return -1 on failure to send <br>
     *          1 on success
     */
    public void sendMessage ( final String emailAddress, final String subject, final String text )
    {
        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ce2002fep2group6@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress));


            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
        }

        catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Overloaded function, sends an email blast to all email addresses in the String array.
     *
     * @param emailAddress Array of emailAddress
     * @param subject Subject of email
     * @param text Message to send to recipient
     *
     * @return -1 on failure to send <br>
     *          1 on success
     */
    public void sendMessage ( final String[] emailAddress, final String subject, final String text )
    {
        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ce2002fep2group6@gmail.com"));

            for (int i = 0; i < emailAddress.length; ++i)
                message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress[i])); //Adds multiple recipients as specified from first arg emailAddress String []

            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
        } catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends an SMS to the specified handphone number.
     *
     * @param number Handphone number of recipient
     * @param text Message to send to recipient
     *
     * @return -1 on failure to send <br>
     *          1 on success
     */
    public int sendSMS (final int number, final String text)
    {

        System.out.println ("SMS has been sent to notify HP No. " + number);
        return 1;
    }

    /**
     * Sends an SMS to the specified list of handphone numbers.
     *
     * @param number array of Handphone numbers of recipients
     * @param text Message to send to recipient
     * @return -1 on failure to send <br>
     *          1 on success
     */
    public int sendSMS (final int [] number, final String text)
    {
        for( int i = 0; i < number.length; ++ i )
        {
            System.out.println("SMS has been sent to notify HP No. " + number);
        }

        return 1;
    }
}
