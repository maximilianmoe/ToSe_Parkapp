package de.g2p.ToSe_Parkapp.Service;

import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class MyUserDetails implements UserDetails {

    private Nutzer nutzer;
    private String benutzername;
    private String passwort;
    private boolean gesperrt;
    private List<GrantedAuthority> authorities;

    public MyUserDetails(Nutzer nutzer) {
        this.nutzer = nutzer;
        this.benutzername = nutzer.getBenutzername();
        this.passwort = nutzer.getPasswort();
        this.gesperrt = nutzer.getSperrung();
        this.authorities = Arrays.stream(nutzer.getAdmin().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passwort;
    }

    @Override
    public String getUsername() {
        return benutzername;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        boolean returnvalue = false;
        if(gesperrt)
            returnvalue = false;
        else
            returnvalue = true;
        return returnvalue;
    }

    public Nutzer getUserDetails() {
        return nutzer;
    }
}