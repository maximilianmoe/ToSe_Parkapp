package de.g2p.ToSe_Parkapp.Service;

import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    NutzerRepository nutzerRepository;

    @Override
    public UserDetails loadUserByUsername(String benutzername) throws UsernameNotFoundException {
        Optional<Nutzer> nutzer = nutzerRepository.findByBenutzername(benutzername);

        nutzer.orElseThrow(() -> new UsernameNotFoundException("Not found: "+benutzername));
        return nutzer.map(MyUserDetails::new).get();
    }
}
