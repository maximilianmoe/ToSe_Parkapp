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

    //GetMapping for the error page
    @GetMapping("/error")
    public String error() {
        return "error-page";
    }





}
