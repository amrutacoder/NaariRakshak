package com.example.women_healthcare;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail extends AsyncTask<Void, Void, Boolean> {
    private String senderEmail;
    private String senderPassword;
    private ArrayList<String> recipientEmail;
    private String locationLink;

    Context context;

    public SendMail(Context context, String senderEmail, String senderPassword, ArrayList<String> recipientEmail, String locationLink) {
        this.context=context;
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
        this.recipientEmail = recipientEmail;
        this.locationLink = locationLink;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });
            String emailContent = " <!DOCTYPE html><html><head><style> body {font-family: Arial, sans-serif;}.container { text-align: center; padding: 20px;}.btn { background-color: #e63946; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;}</style> </head> <body> <div class='container'> <h1>🚨 Emergency Help Request 🚨</h1> <p>I am currently in danger and need your help.</p> <p><a class='btn' href='" + locationLink + "' target='_blank'>View My Location on Google Maps</a></p><p>Please reach out to me as soon as possible.</p></div></body></html>";

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setContent(emailContent, "text/html");
            InternetAddress[] addresses = new InternetAddress[recipientEmail.size()];
            for (int i = 0; i < recipientEmail.size(); i++) {
                addresses[i] = new InternetAddress(recipientEmail.get(i));
            }
            message.addRecipients(Message.RecipientType.TO, addresses);

            message.setSubject("⚠\uFE0F I Am in Danger - Please Help Me Now!");

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            System.out.println("Email sent successfully!");
            Toast.makeText(context, "Emergency email sent!", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("Failed to send email.");
        }
    }
}
