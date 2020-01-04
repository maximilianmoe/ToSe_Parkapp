package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    @Autowired
    NutzerRepository nutzerRepository;

    //GetMapping for the homepage for IP-Adress (or localhost) only
    @GetMapping("/")
    public String homeGet() {
        return "home";
    }

    //GetMapping for the testing page
    @GetMapping("/testweiterleitung")
    public String testweiterleitung() {
        return "testweiterleitung";
    }

    //GetMapping for the error page
    @GetMapping("/errorpage")
    public String error() {
        return "error_page";
    }


    //GetMapping for the home page
    @GetMapping("/home")
    public String home() {
        return "home";
    }
    //remove {id} when spring security is in place
    @GetMapping("/home-{id}")
    public String homeAdmin(@PathVariable("id") Integer id) {
        String returnstring = "";
        Nutzer nutzer = nutzerRepository.findByNid(id);
        if(nutzer.getAdmin().equalsIgnoreCase("admin"))
            returnstring = "home_admin";
        else
            returnstring = "home";
        return returnstring;
    }


    //GetMapping for the Login Page
    @GetMapping("/login")
    public String loginGet() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost() {
        //test if the given username and password are correct
        return "home";
    }

    @GetMapping("/passwort_zurueckgesetzt")
    public String passwortZurueck() {
        return "passwort_zurueckgesetzt";
    }

    @GetMapping("/admin_alle_ausstehenden_reservierungen")
    public String adminRes() {
        return "admin_alle_ausstehenden_reservierungen";
    }

    @GetMapping("/admin_vergangene_transaktionen")
    public String adminTrans() {
        return "admin_vergangene_transaktionen";
    }



    //GetMapping for the buttons on the home page if there is no mapping in other classes
    //Profil and Kontakt has to be added later when the html pages are done

    @GetMapping("/suche")
    public String suche() {
        return "parkplaetze";
    }

    @GetMapping("/aktueller_parkplatz")
    public String aktuellerParkplatz() {
        return "spezieller_parkplatz";
    }
}
