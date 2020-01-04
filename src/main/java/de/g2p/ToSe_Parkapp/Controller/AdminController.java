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

    @PostMapping("/sperren")
    public String sperrenPost(@RequestParam("nutzerid") Integer nid, @RequestParam("buttonNutzer") Integer button, Model model) {
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

    @PostMapping("/loeschen")
    public String loeschenPost(@RequestParam("nid") Integer nid) {
        Nutzer nutzer = nutzerRepository.findByNid(nid);

        //erstes if nur beim testen, da werte mit null in db existieren
        if (nutzer.getRolle()== null)
            System.out.println("nutzerrolle = null");
        else {
            if (nutzer.getRolle().contains("beides")) {
                Anbieter anbieter = anbieterRepository.findByNid(nutzer);
                System.out.println(anbieterRepository.findByNid(nutzer));
                Konsument konsument = konsumentRepository.findByNid(nutzer);
                System.out.println(konsument);
                konsumentRepository.delete(konsument);
                anbieterRepository.delete(anbieter);
            } else if (nutzer.getRolle().contains("anbieter")) {
                Anbieter anbieter = anbieterRepository.findByNid(nutzer);
                System.out.println(anbieter);
                anbieterRepository.delete(anbieter);
            } else if (nutzer.getRolle().contains("konsument")) {
                Konsument konsument = konsumentRepository.findByNid(nutzer);
                System.out.println(konsument);
                konsumentRepository.delete(konsument);
            }
        }

        nutzerRepository.delete(nutzer);
        return "nutzer_geloescht";
    }

    @PostMapping("/rechteAendern")
    public String rechteAendern(@RequestParam("nutzerid2") Integer nid, @RequestParam("buttonAdmin") Integer button) {
        if (button.equals(1)) {
            nutzerRepository.updateAdmin(nid, true);
            System.out.println("Nutzer mit nid " + nid + " wurden Adminrechte erteilt ");
        }
        else if (button.equals(2)) {
            nutzerRepository.updateAdmin(nid, false);
            System.out.println("Nutzer mit nid " + nid + " wurden Adminrechte entzogen ");

        }

        //evtl. noch eine bestätigungsseite?
        return "adminseite";
    }
}
