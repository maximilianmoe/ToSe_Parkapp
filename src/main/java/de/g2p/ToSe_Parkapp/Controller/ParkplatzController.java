package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Parkplatz;
import de.g2p.ToSe_Parkapp.Entities.Standort;
import de.g2p.ToSe_Parkapp.Repositories.ParkplatzRepository;
import de.g2p.ToSe_Parkapp.Repositories.StandortRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ParkplatzController {

    @Autowired
    ParkplatzRepository parkplatzRepository;

    @Autowired
    StandortRepository standortRepository;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("parkplatz", new Parkplatz());
        model.addAttribute("standort", new Standort());
        return "parkplatz_hinzufuegen";
    }

    @PostMapping("/add")
    public String addParkplatz(@ModelAttribute Parkplatz parkplatz, @ModelAttribute Standort standort) {
        //parkplatzRepository.save(parkplatz);
        //standortRepository.save(standort);
        //String returnstring = parkplatzRepository.toString()+" // "+standortRepository.toString();

        String returnstring = standort.toString()+"   //    "+ parkplatz.toString();
        //Übersichtsseite erstellen und den Namen hier ändern
        return returnstring;
    }

}
