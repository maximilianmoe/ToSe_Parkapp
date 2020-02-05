//Alles Auskommentiert, da sonst bei jedem Zugriff auf den Server ein User authentifiziert werden müsste.
//Einfügen erst zu einem späteren Zeitpunkt


package de.g2p.ToSe_Parkapp.Configuration;

import de.g2p.ToSe_Parkapp.Service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resources;


@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

//    @Bean
//    public PasswordEncoder getPasswordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new UrlAuthenticationSuccessHandler();
    }


    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //sets the configuration on the auth object
        auth.userDetailsService(userDetailsService);




    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.authorizeRequests()
                //add all other html sites and check the "hasRole" function again!
                    .antMatchers("/resources/**").permitAll()
                    .antMatchers("/registrieren").permitAll()
                    .antMatchers("/passwort_zurueckgesetzt").permitAll()
                    .antMatchers("/passwordreset").permitAll()
                    .antMatchers("/newpassword").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin().loginPage("/login")
                    .successHandler(myAuthenticationSuccessHandler())
                    .usernameParameter("username").passwordParameter("password").permitAll()
                .and()
                    .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .permitAll().logoutSuccessUrl("/login?logout")
                .and()
                    .csrf().disable();

    }
}