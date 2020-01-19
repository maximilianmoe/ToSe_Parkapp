package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Anbieter;
import de.g2p.ToSe_Parkapp.Entities.Konsument;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Repositories.AnbieterRepository;
import de.g2p.ToSe_Parkapp.Repositories.KonsumentRepository;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import de.g2p.ToSe_Parkapp.Repositories.ReservierungenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    ReservierungenRepository reservierungenRepository;

    @GetMapping("/adminseite")
    public String adminseite() {
        return "adminseite";
    }

    @PostMapping("/sperren")
    public String sperrenPost(@RequestParam("nutzerid") Integer nid, @RequestParam("buttonNutzer") Integer button, Model model) {
        Nutzer nutzer = nutzerRepository.findByNid(nid);
        String returnstring = "";
        if (nutzer == null) {
            model.addAttribute("nid", nid);
            returnstring = "error_nutzer_existiert_nicht";
        } else {
            if (button.equals(1)) {
                nutzer.setSperrung(false);
                nutzerRepository.updateSperrung(nutzer.getNid(), false);
                returnstring = "/nutzer_entsperrt";
            } else if (button.equals(2)) {
                nutzer.setSperrung(true);
                nutzerRepository.updateSperrung(nutzer.getNid(), true);
                returnstring = "/nutzer_gesperrt";
            }
        }
        model.addAttribute("nutzer", nutzer);
        return returnstring;
    }

    @PostMapping("/loeschen")
    public String loeschenPost(@RequestParam("nid") Integer nid) {
        Nutzer nutzer = nutzerRepository.findByNid(nid);
        System.out.println(nutzer.getBenutzername()+"     "+nutzer.getBenutzername());

        if (nutzer.getRolle().equalsIgnoreCase("beides")) {
            System.out.println("Rolle beides");
            Anbieter anbieter = anbieterRepository.findByNid(nutzer);
            Konsument konsument = konsumentRepository.findByNid(nutzer);
            konsumentRepository.delete(konsument);
            anbieterRepository.delete(anbieter);
        } else if (nutzer.getRolle().equalsIgnoreCase("anbieter")) {
            System.out.println("rolle anbieter");
            Anbieter anbieter = anbieterRepository.findByNid(nutzer);
            anbieterRepository.delete(anbieter);
        } else if (nutzer.getRolle().equalsIgnoreCase("konsument")) {
            System.out.println("rolle konsument");
            Konsument konsument = konsumentRepository.findByNid(nutzer);
            konsumentRepository.delete(konsument);
        }
        nutzerRepository.delete(nutzer);

        return "nutzer_geloescht";
    }

    @PostMapping("/rechteAendern")
    public String rechteAendern(@RequestParam("nutzerid2") Integer nid, @RequestParam("buttonAdmin") Integer button,
                                Model model) {
        Nutzer nutzer = nutzerRepository.findByNid(nid);
        String returnstring = "";
        if (button.equals(1)) {
            nutzerRepository.updateAdmin(nid, "admin");
            returnstring = "nutzer_adminrechte_erteilt";
        }
        else if (button.equals(2)) {
            nutzerRepository.updateAdmin(nid, "nutzer");
            returnstring = "nutzer_adminrechte_entzogen";

        }
        model.addAttribute("nutzer", nutzer);
        return returnstring;
    }

    @GetMapping("/admin_alle_ausstehenden_reservierungen")
    public String adminResGet(Model model) {
        if (reservierungenRepository.count() == 0) {
            model.addAttribute("reservierungen", 0);
        } else {
            model.addAttribute("reservierungen", reservierungenRepository.findAll());
            model.addAttribute("konsumenten", konsumentRepository.findAll());
        }

        return "admin_alle_ausstehenden_reservierungen";
    }

    @GetMapping("/admin_vergangene_transaktionen")
    public String adminTrans() {
        return "admin_vergangene_transaktionen";
    }
}
