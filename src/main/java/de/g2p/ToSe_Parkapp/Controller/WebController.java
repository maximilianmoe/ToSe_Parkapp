package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Entities.Standort;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    @Autowired
    NutzerRepository nutzerRepository;

    @GetMapping("/logintest")
    public String index(Model model) {
        model.addAttribute("nutzer", new Nutzer());
        model.addAttribute("standort", new Standort());
        return "logintest";
    }

    @PostMapping("/logintest")
    @ResponseBody
    public String login(@ModelAttribute Nutzer nutzer, @ModelAttribute Standort standort) {
        return standort.getStrasse()+" "+nutzer.getNachname();
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @PostMapping("/home")
    public String homemapping(@RequestParam("input") String input) {

        String returnstring = "";

        System.out.println(input);

        //update the final html. site names
        switch (input) {
            case "suche":
                returnstring = "testweiterleitung";
                break;
            case "parkplatz":
                returnstring = "testweiterleitung";
                break;
            case "guthaben":
                returnstring = "guthabenverwaltung";
                break;
            case "profil":
                returnstring = "testweiterleitung";
                break;
            case "parkplatz_aktuell":
                returnstring = "testweiterleitung";
                break;
            case "kontakt":
                returnstring = "testweiterleitung";
                break;
        }
        return returnstring;
    }

    //GetMapping for the error page
    @GetMapping("/errorpage")
    public String error() {
        return "error_page";
    }




}
