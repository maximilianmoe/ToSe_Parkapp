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
        String fahrzeugtyp = "kleinwagen";
        model.addAttribute("fahrzeugtyp", fahrzeugtyp);
        return "registrieren";
    }


    @PostMapping("/registrieren")
    public String addUser(@ModelAttribute Nutzer nutzer, @ModelAttribute Konsument konsument,
                          @ModelAttribute Anbieter anbieter, @RequestParam("nutzertyp") String nutzertyp,
                          @RequestParam("fahrzeugtyp") String fahrzeugtyp, @RequestParam("email") String email,
                          @RequestParam("username") String username) {

        //boolean duplicate = nutzerRepository.findByEmailAdresse("mmm@gmx.de").isPresent();

        //if(!duplicate) {
            nutzer.setAdmin("nutzer");
            nutzer.setSperrung(false);
            nutzer.setSaldo(0);
            nutzer.setEmailAdresse(email);
            nutzer.setBenutzername(username);

            //Set all values for Anbieter
            if(nutzertyp.contains("anbieter")) {
                anbieter.setNid(nutzer.getNidNutzer());
                nutzer.setRolle("anbieter");
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
                System.out.println("save anbieter");
            }
            //Set all values for Konsument
            else if(nutzertyp.contains("konsument")) {
                konsument.setNid(nutzer.getNidNutzer());
                konsumentRepository.save(konsument);
                nutzer.setRolle("konsument");
                System.out.println("save Konsument");
            }
            //Set all values for both
            else if(nutzertyp.contains("beides")){
                anbieter.setNid(nutzer.getNidNutzer());
                konsument.setNid(nutzer.getNidNutzer());
                nutzer.setRolle("beides");
                //Set fahrzeugtyp for both
                if(fahrzeugtyp.contains("van"))
                    konsument.setFahrzeugtyp("van");
                else if(fahrzeugtyp.contains("kombi"))
                    konsument.setFahrzeugtyp("kombi");
                else if(fahrzeugtyp.contains("suv"))
                    konsument.setFahrzeugtyp("suv");
                else if(fahrzeugtyp.contains("kleinwagen"))
                    konsument.setFahrzeugtyp("kleinwagen");

                anbieterRepository.save(anbieter);
                konsumentRepository.save(konsument);
                System.out.println("save anbieter and konsument");
            }
        //}

        System.out.println(nutzer.getEmailAdresse());
        nutzerRepository.save(nutzer);
        System.out.println("save nutzer");

        return "home";
    }

    @GetMapping("/guthaben")
    public String validate(Model model) {
        String returnstring = "";
        Nutzer nutzer = nutzerRepository.findByNid(13);

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
        //nid = 13 for testing
        Nutzer nutzer = nutzerRepository.findByNid(13);
        model.addAttribute("nutzer", nutzer);
        return "guthabenverwaltung";
    }

    @PostMapping("/guthabenverwaltung")
    public String guthabenPost(Nutzer nutzer, @RequestParam("button") String button,
                               @RequestParam("betrag") Integer betrag) {
        //nid = 13 for testing
        Nutzer nutzer2 = nutzerRepository.findByNid(13);
        Integer saldo = nutzer2.getSaldo();

        //Check which button was pressed (Aufladen or Auszahlen)
        if (button.contains("1"))
            saldo = saldo + betrag;
        else if (button.contains("2"))
            saldo = saldo - betrag;

        nutzerRepository.updateSaldo(saldo, nutzer2.getSaldo());
        //redirect to guthabenweiterleitung because the new saldo is not displayed properly
        return "guthabenweiterleitung";
    }

    @GetMapping("/mein_profil")
    public String profilGet(Model model) {
        //nid=14 for testing
        Nutzer nutzer = nutzerRepository.findByNid(14);
        Konsument konsument = konsumentRepository.findByNid(nutzer);
        model.addAttribute("nutzer", nutzer);
        model.addAttribute("konsument", konsument);
        return "mein_profil";
    }



//    //returns the user
//    private String findNutzer() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            String username = ((UserDetails) principal).getUsername();
//            here you can set all the necessary information with the given user
//        } else {
//            username = principal.toString();
//        }
//        System.out.println(username);
//        return username;
//    }
}
