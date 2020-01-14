package de.g2p.ToSe_Parkapp.Controller;


import de.g2p.ToSe_Parkapp.Entities.Konsument;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Entities.Parken;
import de.g2p.ToSe_Parkapp.Entities.Parkplatz;
import de.g2p.ToSe_Parkapp.Repositories.KonsumentRepository;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import de.g2p.ToSe_Parkapp.Repositories.ParkenRepository;
import de.g2p.ToSe_Parkapp.Repositories.ParkplatzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.Session;


@Controller
public class ParkenController {

    @Autowired
    ParkplatzRepository parkplatzRepository;
    @Autowired
    KonsumentRepository konsumentRepository;
    @Autowired
    NutzerRepository nutzerRepository;
    @Autowired
    ParkenRepository parkenRepository;

    // this method sets the status of the current public parkplatz to the chosen value
    @PostMapping("/spezieller_parkplatz_öffentlich")
    public String spezParkplatzOeffentlichPost(@ModelAttribute Parken parken, @RequestParam("pid") Integer pid, @RequestParam("belegung") String belegt,
                                               Model model) {
        Konsument konsument = konsumentRepository.findByNid(findNutzer());
        Parkplatz parkplatz = parkplatzRepository.findByPid(pid);
        String status = "frei";
        if (belegt.contains("fremdbelegt")) {
            System.out.println("fremdbelegt");
            status = "fremdbelegt";
        }
        else if (belegt.contains("belegt")) {
            System.out.println("belegt");
            status ="belegt";
        }
        model.addAttribute("parkplatz", parkplatz);
        konsumentRepository.updateParkplatzBelegt(true,konsument.getKid());
        parkplatzRepository.updateStatus(status, pid);
        System.out.println("parkencontroller");
        parken.setPid(parkplatz);
        parken.setKid(konsument);
        parken.setFreigabe(false);
        parken.setErinnerung(null);
        parken.setFreigabe(false);
        //TODO Startzeit einstellen (aktuelle zeit)
        //parken.setStart();
        parkenRepository.save(parken);

        model.addAttribute("konsument",konsument );
        model.addAttribute("parken", parkenRepository.findAll());

        return "parkbestaetigung_oeffentlich";
    }

    @GetMapping("/parkbestaetigung_oeffentlich")
    public String parkbesOeffentlichGet(Model model) {
        return "parkbestaetigung_oeffentlich";
    }

    @PostMapping("/parkbestaetigung_oeffentlich")
    public String parkbesOeffentlichPost(@ModelAttribute Model model) {
        //System.out.println(parkplatz);

        System.out.println(model.getAttribute("konsument"));
        //TODO findet den parameter Parkplatz nicht
        Konsument konsument = konsumentRepository.findByNid(findNutzer());
        //System.out.println(parkplatz.getPid());

        //TODO kann ein öffentlicher Parkplatz frei sein oder nur "evtl frei oder evtl belegt"?
        //parkplatzRepository.updateStatus("frei", parkplatz.getPid());
        //konsumentRepository.updateParkplatzBelegt(false, konsument.getKid());
        return "parkplaetze_medialist";
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
