package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Anbieter;
import de.g2p.ToSe_Parkapp.Entities.Konsument;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Repositories.AnbieterRepository;
import de.g2p.ToSe_Parkapp.Repositories.KonsumentRepository;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        return "registrieren";
    }


    @PostMapping("/registrieren")
    public String addUser(@ModelAttribute Nutzer nutzer, @ModelAttribute Konsument konsument,
                          @ModelAttribute Anbieter anbieter, @RequestParam("nutzertyp") String nutzertyp,
                          @RequestParam("fahrzeugtyp") String fahrzeugtyp, @RequestParam("email") String email,
                          @RequestParam("username") String benutzername) {

        boolean duplicate = nutzerRepository.findByBenutzername("mmm@gmx.de").isPresent();

        if (duplicate) {
            //add this to the hmtl page
            System.out.println("Duplicate Nutzer");
            return "registrieren";
        }
        else {
            nutzer.setAdmin("nutzer");
            nutzer.setSperrung(false);
            nutzer.setSaldo(0);
            nutzer.setEmailAdresse(email);
            nutzer.setBenutzername(benutzername);

            //Set all values for Anbieter
            if (nutzertyp.contains("anbieter")) {
                anbieter.setNid(nutzer.getNidNutzer());
                nutzer.setRolle("anbieter");
                //Set fahrzeugtyp for anbieter
                if (fahrzeugtyp.contains("van"))
                    konsument.setFahrzeugtyp("Van");
                else if (fahrzeugtyp.contains("kombi"))
                    konsument.setFahrzeugtyp("Kombi");
                else if (fahrzeugtyp.contains("suv"))
                    konsument.setFahrzeugtyp("SUV");
                else if (fahrzeugtyp.contains("kleinwagen"))
                    konsument.setFahrzeugtyp("Kleinwagen");

                anbieterRepository.save(anbieter);
            }
            //Set all values for Konsument
            else if (nutzertyp.contains("konsument")) {
                konsument.setNid(nutzer.getNidNutzer());
                konsumentRepository.save(konsument);
                nutzer.setRolle("konsument");
            }
            //Set all values for both
            else if (nutzertyp.contains("beides")) {
                anbieter.setNid(nutzer.getNidNutzer());
                konsument.setNid(nutzer.getNidNutzer());
                nutzer.setRolle("beides");
                //Set fahrzeugtyp for both
                if (fahrzeugtyp.contains("van"))
                    konsument.setFahrzeugtyp("Van");
                else if (fahrzeugtyp.contains("kombi"))
                    konsument.setFahrzeugtyp("Kombi");
                else if (fahrzeugtyp.contains("suv"))
                    konsument.setFahrzeugtyp("SUV");
                else if (fahrzeugtyp.contains("kleinwagen"))
                    konsument.setFahrzeugtyp("Kleinwagen");

                anbieterRepository.save(anbieter);
                konsumentRepository.save(konsument);
            }
            nutzerRepository.save(nutzer);
        }

            return "home";
    }


    @GetMapping("/guthaben")
    public String validate(Model model) {
        String returnstring = "";
        Nutzer nutzer = findNutzer();

        if (nutzer.getSaldo() < 0)
            returnstring = "Guthaben_ueberzogen";
        else {
            returnstring = "guthabenverwaltung";
            model.addAttribute("nutzer", nutzer);

        }
        return returnstring;
    }

    @GetMapping("/guthabenverwaltung")
    public String guthabenGet(Model model) {
        Nutzer nutzer = findNutzer();
        model.addAttribute("nutzer", nutzer);
        return "guthabenverwaltung";
    }

    @PostMapping("/guthabenverwaltung")
    public String guthabenPost(Nutzer nutzer, @RequestParam("button") String button,
                               @RequestParam("betrag") Integer betrag) {

        Nutzer nutzer2 = findNutzer();
        Integer saldo = nutzer2.getSaldo();

        //Check which button was pressed (Aufladen or Auszahlen)
        if (button.contains("1"))
            saldo = saldo + betrag;
        else if (button.contains("2"))
            saldo = saldo - betrag;

        nutzerRepository.updateSaldo(saldo, nutzer2.getNid());
        //redirect to guthabenweiterleitung because the new saldo is not displayed properly
        return "guthabenweiterleitung";
    }

    @GetMapping("/mein_profil")
    public String profilGet(Model model) {
        //nid=14 for testing
        Nutzer nutzer = findNutzer();
        Konsument konsument = konsumentRepository.findByNid(nutzer);
        model.addAttribute("nutzer", nutzer);
        model.addAttribute("konsument", konsument);
        return "mein_profil";
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
