package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Parkplatz;
import de.g2p.ToSe_Parkapp.Entities.Standort;
import de.g2p.ToSe_Parkapp.Repositories.ParkplatzRepository;
import de.g2p.ToSe_Parkapp.Repositories.StandortRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/parkplaetze")
    public String parkplaetze() {
        return "parkplaetze";
    }

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

        //parkplatz.setAid has to be Implemented!!
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

    @GetMapping("/parkplaetze/speziell")
    public String speziellerParkplatz( Model model) {
        System.out.println("getmapping");
        Parkplatz parkplatz = parkplatzRepository.findByPid(19);
        System.out.println(parkplatz.getBeschreibung());
        model.addAttribute("parkplatz", parkplatz);
        return "spezieller_parkplatz";
    }






    @GetMapping("/testlauf")
    @ResponseBody
    public List<Parkplatz> testlauf() {
        return parkplatzRepository.findAll();
    }





}
