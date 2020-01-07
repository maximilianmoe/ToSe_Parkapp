package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

    //GetMapping for the homepage
    @GetMapping("/home")
    public String homeAdmin() {
        Nutzer nutzer = findNutzer();
        String returnstring = "";
        if(nutzer.getAdmin().equalsIgnoreCase("admin"))
            returnstring = "home_admin";
        else
            returnstring = "home";
        return returnstring;
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

    //GetMapping for the Login Page
    @GetMapping("/login")
    public String loginGet() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(Model model, HttpSession session) {
        return "home";
    }

    //GetMapping for the logout page and redirect to the login form
    @GetMapping("/logout")
    public String logoutGet() {
        return "logout";
    }
    @PostMapping("/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "login?logout";
    }

    @GetMapping("/parkplatz_allgemein")
    public String parkplatzAllg() {
        String returnstring = "";
        Nutzer nutzer = findNutzer();

        if(nutzer.getRolle().equalsIgnoreCase("beides"))
            returnstring = "parkplaetze_anbieter";
        else if(nutzer.getRolle().equalsIgnoreCase("anbieter"))
            returnstring="parkplaetze_beides";
        else if (nutzer.getRolle().equalsIgnoreCase("konsument"))
            returnstring = "parkplaetze_konsument";

        return returnstring;
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

    public Nutzer findNutzer() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String benutzername = "";
        if(principal instanceof UserDetails)
            benutzername = ((UserDetails) principal).getUsername();
        else
            benutzername = principal.toString();

        Nutzer nutzer = nutzerRepository.findByBenutzernameNO(benutzername);
        return nutzer;
    }
}
