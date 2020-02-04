package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Anbieter;
import de.g2p.ToSe_Parkapp.Entities.Historie;
import de.g2p.ToSe_Parkapp.Entities.Konsument;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Repositories.AnbieterRepository;
import de.g2p.ToSe_Parkapp.Repositories.HistorieRepository;
import de.g2p.ToSe_Parkapp.Repositories.KonsumentRepository;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import de.g2p.ToSe_Parkapp.Service.MailService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class NutzerController {

    @Autowired
    NutzerRepository nutzerRepository;

    @Autowired
    AnbieterRepository anbieterRepository;

    @Autowired
    KonsumentRepository konsumentRepository;
    @Autowired
    HistorieRepository historieRepository;

    MailService mailService = new MailService();



    @GetMapping("/registrieren")
    public String registration(Model model) {
        List<Nutzer> nutzer = nutzerRepository.findAll();
        model.addAttribute("nutzerlist", nutzer);
        model.addAttribute("nutzer", new Nutzer());
        model.addAttribute("konsument", new Konsument());
        model.addAttribute("anbieter", new Anbieter());
        return "registrieren";
    }

    @PostMapping("/registrieren")
    public String addUser(@ModelAttribute Nutzer nutzer, @ModelAttribute Konsument konsument,
                          @ModelAttribute Anbieter anbieter, @RequestParam("nutzertyp") String nutzertyp,
                          @RequestParam(value = "fahrzeugtyp", required = false, defaultValue = "") String fahrzeugtyp, @RequestParam("email") String email,
                          @RequestParam("username") String benutzername, @RequestParam("passwort") String passwort,
                          @RequestParam(value = "erinnerungszeit", required = false, defaultValue = "") String reminder, Model model) {

        boolean duplicate = nutzerRepository.findByBenutzername(benutzername).isPresent();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (duplicate) {
            model.addAttribute("duplicate", 0);
            System.out.println("Duplicate Nutzer");
            return "registrieren";
        }
        else {
            nutzer.setAdmin("nutzer");
            nutzer.setSperrung(false);
            nutzer.setSaldo(0);
            nutzer.setEmailAdresse(email);
            nutzer.setBenutzername(benutzername);
            nutzer.setPasswort(bCryptPasswordEncoder.encode(passwort));
            System.out.println(nutzer.getPasswort());

            if (!reminder.isEmpty()) {
                if (reminder.contains("15")) {
                    nutzer.setErinnerung(15);
                } else if (reminder.contains("30")) {
                    nutzer.setErinnerung(30);
                } else if (reminder.contains("45")) {
                    nutzer.setErinnerung(45);
                }
            }


            //Set all values for Anbieter
            if (nutzertyp.contains("anbieter")) {
                anbieter.setNid(nutzer.getNidNutzer());
                nutzer.setRolle("anbieter");
                //Set fahrzeugtyp for anbieter
                if (!fahrzeugtyp.isEmpty()) {
                    if (fahrzeugtyp.contains("van"))
                        konsument.setFahrzeugtyp("Van");
                    else if (fahrzeugtyp.contains("kombi"))
                        konsument.setFahrzeugtyp("Kombi");
                    else if (fahrzeugtyp.contains("suv"))
                        konsument.setFahrzeugtyp("SUV");
                    else if (fahrzeugtyp.contains("kleinwagen"))
                        konsument.setFahrzeugtyp("Kleinwagen");
                }

                anbieterRepository.save(anbieter);
                historieRepository.save(new Historie(anbieter.getNid(), null, "create", "Anbieter"));

            }
            //Set all values for Konsument
            else if (nutzertyp.contains("konsument")) {
                konsument.setNid(nutzer.getNidNutzer());
                konsumentRepository.save(konsument);
                nutzer.setRolle("konsument");
                historieRepository.save(new Historie(konsument.getNid(), null, "create", "Konsument"));
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
                historieRepository.save(new Historie(anbieter.getNid(), null, "create", "Beides"));
            }
            nutzerRepository.save(nutzer);
            historieRepository.save(new Historie(nutzer, null, "create", "Nutzer"));
            mailService.sendSimpleMessage(email, "Willkommen bei Good2Park!", "Sie haben sich erfoglreich bei Good2Park registriert.\nWir freuen uns Sie bei uns willkommen heißen zu dürfen.");
        }

        return "login";
    }

    @GetMapping("/mein_profil")
    public String profilGet(Model model) {
        Nutzer nutzer = findNutzer();
        String returnstring = "";
        model.addAttribute("nutzer", nutzer);
        if (nutzer.getRolle().equalsIgnoreCase("anbieter")) {
            returnstring = "mein_profil_anbieter";
            model.addAttribute("anbieter", anbieterRepository.findByNid(nutzer.getNidNutzer()));
        }
        else if (nutzer.getRolle().equalsIgnoreCase("beides")) {
            returnstring = "mein_profil_beides";
            model.addAttribute("anbieter", anbieterRepository.findByNid(nutzer.getNidNutzer()));
            model.addAttribute("konsument", konsumentRepository.findByNid(nutzer.getNidNutzer()));
        }
        else if (nutzer.getRolle().equalsIgnoreCase("konsument")) {
            returnstring = "mein_profil_konsument";
            model.addAttribute("konsument", konsumentRepository.findByNid(nutzer.getNidNutzer()));
        }
        return returnstring;
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
        historieRepository.save(new Historie(nutzer2, null, "update", "Saldo"));

        return "guthabenweiterleitung";
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
