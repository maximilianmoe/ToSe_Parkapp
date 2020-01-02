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

        //parkplatz.setAnbieterId(findNutzer());
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
        Standort standort = standortRepository.findByOrtid(38);
        System.out.println(parkplatz.getBeschreibung());
        model.addAttribute("parkplatz", parkplatz);
        return "spezieller_parkplatz";
    }


//    //returns the user
//    private String findNutzer() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username;
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails) principal).getUsername();
//            //here you can set all the necessary information with the given user
//        } else {
//            username = principal.toString();
//        }
//        System.out.println(username);
//        return username;
//    }
}
