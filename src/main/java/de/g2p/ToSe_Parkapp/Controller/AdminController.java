package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.*;
import de.g2p.ToSe_Parkapp.Repositories.*;
import de.g2p.ToSe_Parkapp.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    @Autowired
    TransaktionRepository transaktionRepository;
    @Autowired
    ParkplatzRepository parkplatzRepository;
    @Autowired
    HistorieRepository historieRepository;

    MailService mailService;

    @GetMapping("/adminseite")
    public String adminseite(Model model) {
        Integer gesamtvolumen = transaktionRepository.getGesamtvolumen();
        model.addAttribute("gesamtvolumen", gesamtvolumen);
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
                historieRepository.save(new Historie(nutzer, null, "update", "Sperrung"));
                returnstring = "/nutzer_entsperrt";
            } else if (button.equals(2)) {
                nutzer.setSperrung(true);
                nutzerRepository.updateSperrung(nutzer.getNid(), true);
                historieRepository.save(new Historie(nutzer, null, "update ", "Sperrung"));
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
            Parkplatz parkplatz = parkplatzRepository.findByAnbieterId(anbieter);
            konsumentRepository.delete(konsument);
            anbieterRepository.delete(anbieter);
            parkplatzRepository.delete(parkplatz);
            historieRepository.save(new Historie(findNutzer(), null, "delete", "Konsument, Anbieter und Parkplatz"));

        } else if (nutzer.getRolle().equalsIgnoreCase("anbieter")) {
            System.out.println("rolle anbieter");
            Anbieter anbieter = anbieterRepository.findByNid(nutzer);
            Parkplatz parkplatz = parkplatzRepository.findByAnbieterId(anbieter);
            anbieterRepository.delete(anbieter);
            parkplatzRepository.delete(parkplatz);
            historieRepository.save(new Historie(findNutzer(), null, "delete","Anbieter und Parkplatz"));
        } else if (nutzer.getRolle().equalsIgnoreCase("konsument")) {
            System.out.println("rolle konsument");
            Konsument konsument = konsumentRepository.findByNid(nutzer);
            konsumentRepository.delete(konsument);
            historieRepository.save(new Historie(findNutzer(), null, "delete", "Konsument"));
        }
        mailService.sendSimpleMessage(nutzer.getEmailAdresse(),"Account gelöscht.", "Ihr Account wurde von einem Adminsitrator gelöscht.");
        nutzerRepository.delete(nutzer);
        historieRepository.save(new Historie(findNutzer(), null, "delete", "Nutzer"));


        return "nutzer_geloescht";
    }

    @PostMapping("/rechteAendern")
    public String rechteAendern(@RequestParam("nutzerid2") Integer nid, @RequestParam("buttonAdmin") Integer button,
                                Model model) {
        Nutzer nutzer = nutzerRepository.findByNid(nid);
        String returnstring = "";
        if (button.equals(1)) {
            nutzerRepository.updateAdmin(nid, "admin");
            historieRepository.save(new Historie(nutzer, null, "update", "Adminstatus"));

            returnstring = "nutzer_adminrechte_erteilt";
        }
        else if (button.equals(2)) {
            nutzerRepository.updateAdmin(nid, "nutzer");
            historieRepository.save(new Historie(nutzer, null, "update", "Adminstatus"));
            returnstring = "nutzer_adminrechte_entzogen";

        }
        model.addAttribute("nutzer", nutzer);
        return returnstring;
    }

    @GetMapping("/admin_alle_ausstehenden_reservierungen")
    public String adminResGet(Model model) {
        List<Reservierung> reservierungList = reservierungenRepository.findByBeendet(false);
        if (reservierungList.isEmpty()) {
            model.addAttribute("reservierungen", 0);
        } else {
            model.addAttribute("reservierungen", reservierungList);
            model.addAttribute("konsumenten", konsumentRepository.findAll());
        }

        return "admin_alle_ausstehenden_reservierungen";
    }

    @GetMapping("/admin_vergangene_transaktionen")
    public String adminTrans(Model model) {
        List<Konsument> konsumenten = konsumentRepository.findAll();
        List<Parkplatz> parkplaetze = parkplatzRepository.findAll();
        List<Transaktion> transaktionList = transaktionRepository.findAll();
        model.addAttribute("parkplaetze", parkplaetze);
        model.addAttribute("konsumenten", konsumenten);
        model.addAttribute("transaktionen", transaktionList);
        return "admin_vergangene_transaktionen";
    }

    @GetMapping("/logtabelle")
    public String logtabelleGet(Model model) {
        List<Historie> histories = historieRepository.findAll();
//        List<Historie> historieList = null;
//        for (Historie historieFor : histories) {
//            System.out.println(historieFor.getHistorienId()+" historienid historienfor");
//            System.out.println(historieFor.getHistorienId() > (histories.size() - 200));
//            if (historieFor.getHistorienId() > (histories.size()-200)) {
//                System.out.println("historienid zwischen hid max und hidmax - 200");
//                historieList.add(historieFor);
//            }
//        }
        model.addAttribute("historien", histories);
        model.addAttribute("nutzerlist", nutzerRepository.findAll());
        model.addAttribute("parkplaetze", parkplatzRepository.findAll());
        return "logtabelle";
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
