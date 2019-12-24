package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Anbieter;
import de.g2p.ToSe_Parkapp.Entities.Konsument;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Repositories.AnbieterRepository;
import de.g2p.ToSe_Parkapp.Repositories.KonsumentRepository;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NutzerController {

    @Autowired
    NutzerRepository nutzerRepository;

    @Autowired
    AnbieterRepository anbieterRepository;

    @Autowired
    KonsumentRepository konsumentRepository;

    @GetMapping("/registrieren")
    public String registration(Model model) {
        model.addAttribute("nutzer", new Nutzer());
        model.addAttribute("konsument", new Konsument());
        model.addAttribute("anbieter", new Anbieter());
        System.out.println("getmapping");
        return "registrieren";
    }

    @PostMapping("/registrieren")
    public String addUser(@ModelAttribute Nutzer nutzer, @ModelAttribute Konsument konsument,
                          @ModelAttribute Anbieter anbieter, @RequestParam("nutzertyp") String nutzertyp,
                          @RequestParam("fahrzeugtyp") String fahrzeugtyp) {

        System.out.println("vor duplicate");
        boolean duplicate = nutzerRepository.findByEmailAdresse(nutzer.getEmailAdresse()).isPresent();
        System.out.println("duplicate");

        if(!duplicate) {
            nutzer.setAdmin(false);
            nutzer.setSperrung(false);
            nutzerRepository.save(nutzer);
            System.out.println("save nutzer");
            //Set all values for Anbieter
            if(nutzertyp.contains("anbieter")) {
                anbieter.setNid(nutzer.getNid());
                //Set fahrzeugtyp for anbieter
                if(fahrzeugtyp.contains("van"))
                    konsument.setFahrzeugtyp("van");
                else if(fahrzeugtyp.contains("kombi"))
                    konsument.setFahrzeugtyp("kombi");
                else if(fahrzeugtyp.contains("suv"))
                    konsument.setFahrzeugtyp("suv");
                else if(fahrzeugtyp.contains("kleinwagen"))
                    konsument.setFahrzeugtyp("kleinwagen");

                anbieterRepository.save(anbieter);
            }
            //Set all values for Konsument
            else if(nutzertyp.contains("konsument")) {
                konsument.setNid(nutzer.getNid());
                konsumentRepository.save(konsument);
            }
            //Set all values for both
            else if(nutzertyp.contains("beides")){
                anbieter.setNid(nutzer.getNid());
                konsument.setNid(nutzer.getNid());
                anbieterRepository.save(anbieter);
                konsumentRepository.save(konsument);
            }
        }

        //Set the name from "testweiterleitung" to the home.html file
        return "testweiterleitung";
    }

    @PostMapping("/login")
    public String loginUser(){

        return null;
    }
}
