package de.g2p.ToSe_Parkapp.Service;

import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


/**
 * @author Maximilian Lehner
 * This class creates a simple email.
 */
@Component
public class MailService implements EmailService{

    @Autowired
    public JavaMailSender mailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
/*

    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(text);
    FileSystemResource file
            = new FileSystemResource(new File(pathToAttachment));
    helper.addAttachment("Invoice", file);
    emailSender.send(message);
*/



/*    *//**
     * @param to The email address of the user the email is send to.
     * @param subject The subject of the email.
     * @param text The text or body of the email which is send to the user.
     *//*
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    @Bean
    public SimpleMailMessage templateRegistration(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("Um die Registrierung abzuschließen und dich zu verifizieren klicke auf diesen Link: ");
        return message;
    }
    @Bean
    public SimpleMailMessage templatePassword(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("Um dein Passwort zurückzusetzten klicke auf diesen Link: ");
        return message;
    }
    @Bean
    public SimpleMailMessage templateReminder(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("In einer halben Stunde läuft dein Parkplatz ab");
        return message;
    }*/
}

   /* Die Templates können nach folgendem Beispiel verwendet werden: https://www.baeldung.com/spring-email

   @Autowired
    public SimpleMailMessage template;
...
        String text = String.format(template.getText(), templateArgs);
        sendSimpleMessage(to, subject, text);
*/
