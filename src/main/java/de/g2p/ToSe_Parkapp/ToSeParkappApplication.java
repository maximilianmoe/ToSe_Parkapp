package de.g2p.ToSe_Parkapp;

import de.g2p.ToSe_Parkapp.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.SimpleMailMessage;

@SpringBootApplication
@EnableJpaRepositories
public class ToSeParkappApplication {
	public static void main(String[] args) {
		SpringApplication.run(ToSeParkappApplication.class, args);
	}

}
