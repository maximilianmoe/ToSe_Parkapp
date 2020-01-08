package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.*;
import de.g2p.ToSe_Parkapp.Repositories.AnbieterRepository;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import de.g2p.ToSe_Parkapp.Repositories.ParkplatzRepository;
import de.g2p.ToSe_Parkapp.Repositories.StandortRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ParkplatzController {

    @Autowired
    ParkplatzRepository parkplatzRepository;
    @Autowired
    StandortRepository standortRepository;
    @Autowired
    NutzerRepository nutzerRepository;
    @Autowired
    AnbieterRepository anbieterRepository;

    @GetMapping("/parkplatz_hinzufuegen")
    public String add(Model model) {
        Anbieter anbieter = anbieterRepository.findByNid(findNutzer());
        String returnstring = "";
        //Checks if anbieter already has a Parkplatz
        if (anbieter.getParkplatz() == true) {
            returnstring = "error_bereits_ein_parkplatz";
        }
        else {
            model.addAttribute("parkplatz", new Parkplatz());
            model.addAttribute("standort", new Standort());
            returnstring = "parkplatz_hinzufuegen";
        }
        return returnstring;
    }

    @PostMapping("/parkplatz_hinzufuegen")
    public String addParkplatz(@ModelAttribute Parkplatz parkplatz, @ModelAttribute Standort standort,
                               @RequestParam("parkplatzChecked") String checked,
                               @RequestParam("fahrzeugtyp") String fahrzeugtyp) {
        // Example for checking an already existing Standort where no new database entry is created
//        for (Standort standortvariable : standortRepository.findAll()) {
//            if (standortvariable.getStrasse() == standort.getStrasse())
//                if(standortvariable.getHausnummer() == standort.getHausnummer())
//                    if(standortvariable.getPlz() == standort.getPlz()) {
//                        standort.setOrtid(standortvariable.getOrtid());
//                        Integer ortId = standortvariable.getOrtid()
//                        parkplatz.setOrtid(ortId);
//                    }
//        }

            Anbieter aid = anbieterRepository.findByNid(findNutzer());
            parkplatz.setAnbieterId(aid);
            parkplatz.setStatus("frei");
            parkplatz.setOrtid(standort);
            parkplatz.setBewertung(0);
            parkplatz.setBewertungsanzahl(0);

            //Sets Parkplatz to private if the box for "privater Parkplatz" is checked
            if (checked.contains("1")) {
                parkplatz.setPrivat(true);
                parkplatz.setZeitbegrenzung(0);
                parkplatz.setParkgebuehr(0);
                parkplatz.setStrafgebuehr(0);
            } else if (checked.contains("2"))
                parkplatz.setPrivat(false);

            //Sets Fahrzeugtyp if box is checked
            if (fahrzeugtyp.contains("on"))
                parkplatz.setFahrzeugtyp(fahrzeugtyp);

            //Saves all data in the database
            parkplatzRepository.save(parkplatz);
            anbieterRepository.updateParkplatz(true, aid.getAid());
            standortRepository.save(standort);
            return "special_parkingslot_own";
    }

    @GetMapping("/parkplaetze_medialist")
    public String parkMedia() {
        return "parkplaetze_medialist";
    }

    @GetMapping("/parkplatz_allgemein")
    public String parkplatzAllg() {
        String returnstring = "";
        Nutzer nutzer = findNutzer();

        if(nutzer.getRolle().equalsIgnoreCase("anbieter"))
            returnstring = "parkplaetze_anbieter";
        else if(nutzer.getRolle().equalsIgnoreCase("beides"))
            returnstring="parkplaetze_beides";
        else if (nutzer.getRolle().equalsIgnoreCase("konsument"))
            returnstring = "parkplaetze_konsument";

        return returnstring;
    }

    //returns the Parkplatz after it is created
    @GetMapping("/special_parkingslot_own")
    public String ownParkingslot(Model model) {
        Nutzer nutzer = findNutzer();
        Anbieter anbieter = anbieterRepository.findByNid(nutzer);
        String returnstring = "";
        if(anbieter.getParkplatz() == false)
            returnstring = "error_noch_kein_parkplatz";
        else if (anbieter.getParkplatz() == true) {
            Parkplatz parkplatz = parkplatzRepository.findByAnbieterId(anbieterRepository.findByNid(nutzer.getNidNutzer()));
            Standort standort = standortRepository.findByOrtid(parkplatz.getOrtId());
            model.addAttribute("parkplatz", parkplatz);
            model.addAttribute("standort", standort);
            returnstring = "mein_parkplatz";
        }
        return returnstring;
    }

    @PostMapping("/delete_parkplatz")
    public String deleteParkplatz() {
        //TODO add the method to delete the Parkplatz from the Database and change the "parkplatz" attribute
        // in table Konsument to false
        return "home";
    }

    @GetMapping("/error_noch_kein_parkplatz")
    public String errorKeinParkplatz() {
        return "error_noch_kein_parkplatz";
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
