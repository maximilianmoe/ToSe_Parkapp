package de.g2p.ToSe_Parkapp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


/**
 * @author Maximilian Lehner
 * This class creates a simple email.
 */
@Component
public class MailService{

    public JavaMailSender mailSender;


    /**
     * @param to The email address of the user the email is send to.
     * @param subject The subject of the email.
     * @param text The text or body of the email which is send to the user.
     */
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

