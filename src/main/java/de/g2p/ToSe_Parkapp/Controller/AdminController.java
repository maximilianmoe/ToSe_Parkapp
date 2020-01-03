package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Anbieter;
import de.g2p.ToSe_Parkapp.Entities.Konsument;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Repositories.AnbieterRepository;
import de.g2p.ToSe_Parkapp.Repositories.KonsumentRepository;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @Autowired
    NutzerRepository nutzerRepository;

    @Autowired
    AnbieterRepository anbieterRepository;

    @Autowired
    KonsumentRepository konsumentRepository;

    @GetMapping("/adminseite")
    public String adminseite() {
        return "adminseite";
    }

    @GetMapping("/sperren")
    public String sperrenGet() {
        return "adminseite";
    }

    @PostMapping("/sperren")
    public String sperrenPost(@RequestParam("nutzerid") Integer nid, @RequestParam("button") Integer button, Model model) {
        Nutzer nutzer = nutzerRepository.findByNid(nid);
        String returnstring = "";
        if (button.equals(1)) {
            nutzer.setSperrung(false);
            nutzerRepository.updateSperrung(nutzer.getNid(), false);
            returnstring = "nutzer_entsperrt";
        }
        else if(button.equals(2)) {
            nutzer.setSperrung(true);
            nutzerRepository.updateSperrung(nutzer.getNid(), true);
            returnstring = "nutzer_gesperrt";
        }
        model.addAttribute("nutzer", nutzer);
        return returnstring;
    }

    @GetMapping("/loeschen")
    public String loeschenGet() {
        return "adminseite";
    }

    @PostMapping("/loeschen")
    public String loeschenPost(@RequestParam("nid") Integer nid) {
        Nutzer nutzer = nutzerRepository.findByNid(nid);
        System.out.println(nutzer);

        if (nutzer.getRolle()== null)
            System.out.println("nutzerrolle = null");
        else {
            System.out.println("else fall");
            if (nutzer.getRolle().contains("beides")) {
                System.out.println("erstes if");
                Anbieter anbieter = anbieterRepository.findByNid(nid);
                System.out.println(anbieter);
                Konsument konsument = konsumentRepository.findByNid(nid);
                System.out.println(konsument);
                konsumentRepository.delete(konsument);
                anbieterRepository.delete(anbieter);
            } else if (nutzer.getRolle().contains("anbieter")) {
                Anbieter anbieter = anbieterRepository.findByNid(nid);
                System.out.println(anbieter);
                anbieterRepository.delete(anbieter);
            } else if (nutzer.getRolle().contains("konsument")) {
                Konsument konsument = konsumentRepository.findByNid(nid);
                System.out.println(konsument);
                konsumentRepository.delete(konsument);
            }
        }

        nutzerRepository.delete(nutzer);
        return "nutzer_gesperrt";
    }
}