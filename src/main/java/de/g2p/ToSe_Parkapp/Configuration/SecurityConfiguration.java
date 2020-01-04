////Alles Auskommentiert, da sonst bei jedem Zugriff auf den Server ein User authentifiziert werden müsste.
////Einfügen erst zu einem späteren Zeitpunkt
//
//
//package de.g2p.ToSe_Parkapp.Configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    @Bean
//    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {return new UrlAuthenticationSuccessHandler();}
//
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//
//        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/login").permitAll()
//                .antMatchers("/registration").permitAll().antMatchers("**/secured/**").authenticated().anyRequest()
//                .permitAll().and().formLogin().loginPage("/login").permitAll()
//                .successHandler(myAuthenticationSuccessHandler()).passwordParameter("password")
//                .usernameParameter("email").and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/login").and().exceptionHandling().accessDeniedPage("/access-denied");
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth.inMemoryAuthentication()
//
//                .withUser("user")
//
//                .password("{noop}pass") // Spring Security 5 requires specifying the password storage format
//
//                .roles("USER");
//
//    }
//
//}