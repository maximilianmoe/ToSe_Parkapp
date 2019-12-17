package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebController {

    @Autowired
    NutzerRepository nutzerRepository;

    @RequestMapping(value ="/test1",method = RequestMethod.GET)
    public String index() {
        return "logintest";
    }

    @PostMapping("/test1")
    public String login(
            @RequestParam(value = "username", required = true) String username, BindingResult result,
            @RequestParam(value = "passwort", required = true) String passwort, BindingResult result2,
            Model model) {

        model.addAttribute("username", username);
        model.addAttribute("passwort", passwort);
        return username+" "+passwort+" erfolgreich!";
    }

    @RequestMapping("/test")
    public String index2(){
        return "registrieren";
    }



}
