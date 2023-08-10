package com.bear.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.lang.reflect.Array;
import java.util.*;

public class MailUtils {
    public static void main(String[] args) throws MessagingException {
    }

    public static void sendMail(String email, String code) throws MessagingException{
        // Create Properties object to record the email properties
        Properties props = new Properties();
        // Enable SMTP authentication (mandatory for Hotmail)
        props.put("mail.smtp.auth", "true");
        // Set the SMTP server for Hotmail
        props.put("mail.smtp.host", "smtp.office365.com");
        // Set the port number for Hotmail SMTP (587 is the default for TLS)
        props.put("mail.smtp.port", "587");
        // Set your Hotmail email address
        props.put("mail.user", "shibuyaGK@hotmail.com");
        // Set your Hotmail account password or an "App Password" if enabled
        props.put("mail.password", "b");
        // Enable STARTTLS (secure communication) for Hotmail
        props.put("mail.smtp.starttls.enable", "true");

        // Build authentication information for SMTP authentication
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };

        // Create a mail session using the properties and authentication
        Session mailSession = Session.getInstance(props, authenticator);
        // Create a new MimeMessage for the email
        MimeMessage message = new MimeMessage(mailSession);

        // Set the sender (From) address
        InternetAddress from = new InternetAddress(props.getProperty("mail.user"));
        message.setFrom(from);
        // Set the recipient (To) address
        InternetAddress to = new InternetAddress(email);
        message.setRecipients(Message.RecipientType.TO, String.valueOf(to));
        // Set message subject
        message.setContent("Dear user,\nYour registration verification code is: " + code
                + " (valid for one minute, please do not share it with others)", "text/html;charset=UTF-8");

        // Send email
        Transport.send(message);
    }

    public static String generateCode(){
        List<String> list = new ArrayList<>(List.of("2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z"));

        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(list.get(i));
        }
        return sb.toString();
    }

}
