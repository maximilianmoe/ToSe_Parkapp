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
                               @RequestParam("fahrzeugtyp") String fahrzeugtyp,
                               @RequestParam("zeitbegrenzung") Integer zeitbegrenzung, Model model) {
        // Example for checking an already existing Standort where no new database entry is created
//        for (Standort standortvariable : standortRepository.findAll()) {
//            System.out.println(standortvariable.getStrasse()+" for schleife 1");
//            System.out.println(standortvariable.getHausnummer()+" for schleife 1");
//            System.out.println(standortvariable.getPlz()+" for schleife 1");
//            System.out.println("");
//            System.out.println(standort.getStrasse()+" for schleife 2");
//            System.out.println(standort.getHausnummer()+" for schleife 2");
//            System.out.println(standort.getPlz()+" for schleife 2");
//            System.out.println("");
//            System.out.println("");
//            System.out.println("");
//            if (standortvariable.getPlz().equals(standort.getPlz())) {
//                System.out.println("sout1     plz");
//
//                if (standortvariable.getStrasse().equalsIgnoreCase(standort.getStrasse())) {
//                    System.out.println("sout2      strasse");
//
//                    if (standortvariable.getHausnummer().equals(standort.getHausnummer())) {
//                        System.out.println("sout3     hausnummer");
//                        System.out.println(standortvariable.getStrasse());
//                        parkplatz.setOrtid(standortvariable);
//                        break;
//                    }
//                }
//            }
//        }



        Anbieter aid = anbieterRepository.findByNid(findNutzer());
        parkplatz.setAnbieterId(aid);
        parkplatz.setStatus("frei");
        parkplatz.setOrtid(standort);
        parkplatz.setBewertung(0);
        parkplatz.setBewertungsanzahl(0);
        parkplatz.setZeitbegrenzung(zeitbegrenzung);

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

        //getting an Overview page after adding a new Parkplatz
        model.addAttribute("parkplatz", parkplatz);
        model.addAttribute("standort", standort);
        speziellerParkplatz(model);

        //Übersichtsseite erstellen und den Namen hier ändern
        return "spezieller_parkplatz";
    }

    @GetMapping("/spezieller_parkplatz")
    public String speziellerParkplatz(Model model) {
        return "spezieller_parkplatz";
    }

    @GetMapping("/parkplaetze_speziell")
    public String parkplatzSpeziell (Model model) {
        Parkplatz parkplatz = parkplatzRepository.findByPid(31);
        Standort standort = standortRepository.findByOrtid(parkplatz.getOrtId());
        model.addAttribute("parkplatz", parkplatz);
        model.addAttribute("standort", standort);
        return "spezieller_parkplatz";
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
