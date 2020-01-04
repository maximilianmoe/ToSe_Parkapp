//Alles Auskommentiert, da sonst bei jedem Zugriff auf den Server ein User authentifiziert werden müsste.
//Einfügen erst zu einem späteren Zeitpunkt


package de.g2p.ToSe_Parkapp.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        //set other password encoder!!!
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //sets the configuration on the auth object
        auth.userDetailsService(userDetailsService);


//                .dataSource(dataSource)
//                .usersByUsernameQuery("select benutzername, passwort, gesperrt from nutzer " +
//                        "where benutzername = ?")
//                .authoritiesByUsernameQuery("select benutzername, admin from nutzer where benutzername = ?");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //add all other html sites and check the "hasRole" function again!
                .antMatchers("/adminseite").authenticated()
                .antMatchers("/testweiterleitung").authenticated()
                .antMatchers("/home").authenticated()
                .and().formLogin();
    }
}