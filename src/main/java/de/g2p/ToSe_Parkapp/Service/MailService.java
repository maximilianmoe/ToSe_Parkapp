package de.g2p.ToSe_Parkapp.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;


/**
 * @author Maximilian Lehner
 * This class creates a simple email.
 */
@Component
public class MailService implements EmailService {


    private JavaMailSenderImpl mailSender;

    @Bean
    public JavaMailSender getJavaMailSender() {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("good2park.service@gmail.com");
        mailSender.setPassword("mllljnhjikjejikm");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    /**
     * @param to      The email address of the user the email is send to.
     * @param subject The subject of the email.
     * @param text    The text or body of the email which is send to the user.
     */
    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        getJavaMailSender();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("good2park.service@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message); //This line produces the exception
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't send message");
        }


    }
}