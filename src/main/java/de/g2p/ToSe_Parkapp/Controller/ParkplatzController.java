package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Anbieter;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Entities.Parkplatz;
import de.g2p.ToSe_Parkapp.Entities.Standort;
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
        model.addAttribute("parkplatz", new Parkplatz());
        model.addAttribute("standort", new Standort());
        return "parkplatz_hinzufuegen";
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
        if(checked.contains("1"))
            parkplatz.setPrivat(true);
        else if (checked.contains("2"))
            parkplatz.setPrivat(false);

        //Sets Fahrzeugtyp if box is checked
        if(fahrzeugtyp.contains("on"))
            parkplatz.setFahrzeugtyp(fahrzeugtyp);

        //Saves all data in the database
        parkplatzRepository.save(parkplatz);
        standortRepository.save(standort);

        //Übersichtsseite erstellen und den Namen hier ändern
        return "testweiterleitung";
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
