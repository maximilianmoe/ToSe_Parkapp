package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.*;
import de.g2p.ToSe_Parkapp.Repositories.ParkenRepository;
import de.g2p.ToSe_Parkapp.Repositories.ReservierungenRepository;
import de.g2p.ToSe_Parkapp.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReservierungController {
    @Autowired
    ParkenRepository parkenRepository;
    @Autowired
    ReservierungenRepository reservierungenRepository;

    MailService mailService = new MailService();

    @GetMapping("/reserve")
    public String reserve(Model model) {
        mailService.sendSimpleMessage("lehnermaxi@yahoo.de", "Test", "test");
        model.addAttribute("parken", new Parken());
        model.addAttribute("reservierung", new Reservierung());
        return "spezieller_parkplatz";

    }

    @PostMapping("/reserve")
    public String reserveParkplatz(@ModelAttribute Parkplatz parkplatz, @ModelAttribute Parken parken, @ModelAttribute Reservierung reservierung, @ModelAttribute Konsument konsument, @RequestParam("reservierungChecked") String checked) {

        reservierung.setBeendet(false);
        reservierung.setResZuParken(true);
        reservierung.setKid(konsument);
        reservierung.setPid(parkplatz);

        //      checks if reminder is requested. Weather checked == "1", consumer chose to get a reminder in the dropdown menue.
        if (checked.contains("1")) {
            //      Erinnerung is hardcoded here to 30 minutes before the parking time ends.
//            parken.setErinnerung(parken.getEnde().minusMinutes(30));
            System.out.println("worked");
        }else if (checked.contains("2")) {
            parken.setErinnerung(null);
            System.out.println("worked");
        }



        //Saves all data in the database
        parkenRepository.save(parken);
        reservierungenRepository.save(reservierung);

        //Übersichtsseite erstellen und den Namen hier ändern
        return "testweiterleitung";
    }
}
