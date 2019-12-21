package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Repositories.AnbieterRepository;
import de.g2p.ToSe_Parkapp.Repositories.KonsumentRepository;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NutzerController {

    @Autowired
    NutzerRepository nutzerRepository;

    @Autowired
    AnbieterRepository anbieterRepository;

    @Autowired
    KonsumentRepository konsumentRepository;



    @GetMapping("/registrieren")
    public String registration() {
        return "registrieren";
    }

    @PostMapping("/registrieren")
    public String addUser(@ModelAttribute Nutzer nutzer, Model model){
//        String asf = request.getParameter("vorname");
//        String sdf = request.getParameter("nachname");
//        String emailadresse = request.getParameter("emailadresse");
//        String passwort = request.getParameter("passwort");
        //Nutzer nutzer = new Nutzer(vorname, nachname, emailadresse, passwort);

        return nutzer.getNachname();
    }

    @PostMapping("/login")
    public String loginUser(){

        return null;
    }
}
