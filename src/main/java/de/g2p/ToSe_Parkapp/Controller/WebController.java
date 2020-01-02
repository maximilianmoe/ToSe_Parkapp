package de.g2p.ToSe_Parkapp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

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


    //GetMapping for the buttons on the home page if there is no mapping in other classes
    //Profil and Kontakt has to be added later when the html pages are done

    @GetMapping("/suche")
    public String suche() {
        return "parkplaetze";
    }

    @GetMapping("/spezieller_parkplatz")
    public String speziellerParkplatz() {
        return "spezieller_parkplatz";
    }

    @GetMapping("/aktueller_parkplatz")
    public String aktuellerParkplatz() {
        return "spezieller_parkplatz";
    }
}
