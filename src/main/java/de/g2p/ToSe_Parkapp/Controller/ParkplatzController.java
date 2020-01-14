package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.*;
import de.g2p.ToSe_Parkapp.Repositories.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class ParkplatzController {

    @Autowired
    ParkplatzRepository parkplatzRepository;
    @Autowired
    NutzerRepository nutzerRepository;
    @Autowired
    AnbieterRepository anbieterRepository;
    @Autowired
    KonsumentRepository konsumentRepository;
    @Autowired
    ParkenRepository parkenRepository;


    @GetMapping("/parkplatz_hinzufuegen")
    public String add(Model model) {
        Anbieter anbieter = anbieterRepository.findByNid(findNutzer());
        String returnstring = "";
        //Checks if anbieter already has a Parkplatz
        if (anbieter.getParkplatz() == true) {
            returnstring = "error_bereits_ein_parkplatz";
        } else {
            model.addAttribute("parkplatz", new Parkplatz());
            returnstring = "parkplatz_hinzufuegen";
        }
        return returnstring;
    }

    @PostMapping("/parkplatz_hinzufuegen")
    public String addParkplatz(@ModelAttribute Parkplatz parkplatz,
                               @RequestParam("parkplatzChecked") String checked,
                               @RequestParam("fahrzeugtyp") String fahrzeugtyp) {

        Anbieter aid = anbieterRepository.findByNid(findNutzer());
        parkplatz.setAnbieterId(aid);
        parkplatz.setStatus("frei");
        parkplatz.setBewertung(0);
        parkplatz.setBewertungsanzahl(0);
        parkplatz.setGesamtbewertung(0);

        //Sets Parkplatz to private if the box for "privater Parkplatz" is checked
        if (checked.contains("1"))
            parkplatz.setPrivat(true);
        else if (checked.contains("2")) {
            parkplatz.setPrivat(false);
            parkplatz.setZeitbegrenzung(0);
            parkplatz.setParkgebuehr(0);
            parkplatz.setStrafgebuehr(0);
        }

        //Sets Fahrzeugtyp if box is checked
        if (fahrzeugtyp.contains("on"))
            parkplatz.setFahrzeugtyp(fahrzeugtyp);

        //Saves all data in the database
        parkplatzRepository.save(parkplatz);
        anbieterRepository.updateParkplatz(true, aid.getAid());
        return "mein_parkplatz";
    }

    @GetMapping("/spezieller_parkplatz_privat")
    public String spezParkplatzPrivatGet(Model model) {
        System.out.println("getMapping spezieller privater Parkplatz");
        return "spezieller_parkplatz_privat";
    }

    @GetMapping("/spezieller_parkplatz_öffentlich")
    public String spezParkplatzOeffentlichGet(Model model) {
        model.addAttribute("parken", new Parken());
        return "spezieller_parkplatz_öffentlich";
    }

    @GetMapping("/parkplaetze_medialist")
    public String parkMediaGet(Model model) {
        List<Parkplatz> parkplaetze = parkplatzRepository.findAll();
        model.addAttribute("parkplaetze", parkplaetze);
        return "parkplaetze_medialist";
    }

    //handles the redirect to the special_parkingslot page
    @PostMapping("/parkplaetze_medialist")
    public String parkMediaPost(Model model, @RequestParam("button") Integer button) {
        Konsument konsument = konsumentRepository.findByNid(findNutzer());
        Parkplatz parkplatz = parkplatzRepository.findByPid(button);
        String returnstring="";
        for (int i=1; i<=parkplatzRepository.count(); i++) {
            if (button == i) {
                model.addAttribute("parkplatz", parkplatz);
                if (konsument.getBelegt()){
                    returnstring = "parkbestaetigung_oeffentlich";
                    break;
                } else if (parkplatz.isPrivat() == true) {
                    returnstring = "spezieller_parkplatz_privat";
                    model.addAttribute("reservierung", new Reservierung());
                    model.addAttribute("parken", new Parken());
                    break;
                } else if (parkplatz.isPrivat() == false) {
                    returnstring = "spezieller_parkplatz_öffentlich";
                    model.addAttribute("parken", new Parken());
                    break;
                }
            } else
                returnstring = "error_page";
        }

        return returnstring;
    }

    @GetMapping("/parkplatz_allgemein")
    public String parkplatzAllg() {
        String returnstring = "";
        Nutzer nutzer = findNutzer();

        if (nutzer.getRolle().equalsIgnoreCase("anbieter"))
            returnstring = "parkplaetze_anbieter";
        else if (nutzer.getRolle().equalsIgnoreCase("beides"))
            returnstring = "parkplaetze_beides";
        else if (nutzer.getRolle().equalsIgnoreCase("konsument"))
            returnstring = "parkplaetze_konsument";

        return returnstring;
    }

    //returns the Parkplatz after it is created
    @GetMapping("/mein_parkplatz")
    public String ownParkingslot(Model model) {
        Nutzer nutzer = findNutzer();
        Anbieter anbieter = anbieterRepository.findByNid(nutzer);
        String returnstring = "";
        if (anbieter.getParkplatz() == false)
            returnstring = "error_noch_kein_parkplatz";
        else if (anbieter.getParkplatz() == true) {
            Parkplatz parkplatz = parkplatzRepository.findByAnbieterId(anbieterRepository.findByNid(nutzer.getNidNutzer()));
            model.addAttribute("parkplatz", parkplatz);
            returnstring = "mein_parkplatz";
        }
        return returnstring;
    }


    @PostMapping("/delete_parkplatz")
    public String deleteParkplatz() {
        //TODO add the method to delete the Parkplatz from the Database and change the "parkplatz" attribute
        // in table Konsument to false

        //parkplatzRepository.delete(parkplatz);
        return "home";
    }

    @GetMapping("/error_noch_kein_parkplatz")
    public String errorKeinParkplatz() {
        return "error_noch_kein_parkplatz";
    }

    public Nutzer findNutzer() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String benutzername = "";
        if (principal instanceof UserDetails)
            benutzername = ((UserDetails) principal).getUsername();
        else
            benutzername = principal.toString();

        Nutzer nutzer = nutzerRepository.findByBenutzernameNO(benutzername);
        return nutzer;
    }

}
