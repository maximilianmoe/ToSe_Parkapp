package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.*;
import de.g2p.ToSe_Parkapp.Repositories.*;
import de.g2p.ToSe_Parkapp.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * The type Admin controller.
 */
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
    @Autowired
    ParkenRepository parkenRepository;

    MailService mailService = new MailService();


    /**
     * Gets the Adminseite for role Admin
     *
     * @param model the model
     * @return adminseite
     */
    @GetMapping("/adminseite")
    public String adminseite(Model model) {
        Integer gesamtvolumen = transaktionRepository.getGesamtvolumen();
        model.addAttribute("gesamtvolumen", gesamtvolumen);
        return "adminseite";
    }

    /**
     * Blocks a user
     *
     * @param nid    the nid
     * @param button the button
     * @param model  the model
     * @return returnstring
     */
    @PostMapping("/sperren")
    public String sperrenPost(@RequestParam("nutzerid") Integer nid, @RequestParam("buttonNutzer") Integer button, Model model) {
        Nutzer nutzer = nutzerRepository.findByNid(nid);
        String returnstring = "";
        if (nutzer == null) {
            model.addAttribute("nid", nid);
            returnstring = "error_nutzer_existiert_nicht";
        } else {

            if (button.equals(1)) {
                mailService.sendSimpleMessage(nutzer.getEmailAdresse(), "Account entsperrt.", "Ihr Account wurde von einem Adminsitrator erfolgreich entsperrt.");
                nutzer.setSperrung(false);
                nutzerRepository.updateSperrung(nutzer.getNid(), false);
                historieRepository.save(new Historie(nutzer, null, "update", "Sperrung"));
                returnstring = "/nutzer_entsperrt";
            } else if (button.equals(2)) {
                mailService.sendSimpleMessage(nutzer.getEmailAdresse(), "Account gesperrt.", "Ihr Account wurde von einem Adminsitrator gesperrt. Ciao.");
                nutzer.setSperrung(true);
                nutzerRepository.updateSperrung(nutzer.getNid(), true);
                historieRepository.save(new Historie(nutzer, null, "update ", "Sperrung"));
                returnstring = "/nutzer_gesperrt";
            }
        }
        model.addAttribute("nutzer", nutzer);
        return returnstring;
    }


    /**
     * Delete a Nutzer from the Admin page.
     *
     * @param nid nid
     * @return nutzer_geloescht
     */
    @PostMapping("/loeschen")
    public String loeschenPost(@RequestParam("nid") Integer nid) {
        Nutzer nutzer = nutzerRepository.findByNid(nid);

        if (nutzer == null) {
            return "error_kein_nutzer";
        } else {
            if (anbieterRepository.findByNid(nutzer).getNid() != null) {
                if (anbieterRepository.findByNid(nutzer).getParkplatz())
                    return "error_anbieter_noch_parkplatz_vorhanden";
            }

            mailService.sendSimpleMessage(nutzer.getEmailAdresse(), "Account gelöscht.", "Ihr Account wurde von einem Adminsitrator gelöscht.");
            if (nutzer.getRolle().equalsIgnoreCase("beides")) {
                System.out.println("Rolle beides");
                Anbieter anbieter = anbieterRepository.findByNid(nutzer);
                Konsument konsument = konsumentRepository.findByNid(nutzer);
                Parkplatz parkplatz = parkplatzRepository.findByAnbieterId(anbieter);
                List<Historie> historieList = historieRepository.findByNid(anbieter.getNid());
                for (Historie historieFor : historieList) {
                    historieRepository.updateNid(null, historieFor.getHistorienId());
                    if (anbieter.getParkplatz())
                        parkplatzRepository.updateAid(null, (parkplatzRepository.findByAnbieterId(anbieter)).getPid());
                }
                if (anbieter.getParkplatz()) {
                    parkplatzRepository.delete(parkplatz);
                }
                konsumentRepository.delete(konsument);
                anbieterRepository.delete(anbieter);
                historieRepository.save(new Historie(findNutzer(), null, "delete", "Konsument, Anbieter und Parkplatz"));

            } else if (nutzer.getRolle().equalsIgnoreCase("anbieter")) {
                System.out.println("rolle anbieter");
                Anbieter anbieter = anbieterRepository.findByNid(nutzer);
                Parkplatz parkplatz = parkplatzRepository.findByAnbieterId(anbieter);
                List<Historie> historieList = historieRepository.findByNid(anbieter.getNid());
                System.out.println(historieList.size());
                for (Historie historieFor : historieList) {
                    historieRepository.updateNid(null, historieFor.getHistorienId());
                    System.out.println("historieFor");
                    if (anbieter.getParkplatz())
                        System.out.println("anbieter.getParkplatz");
                    historieRepository.updatePid(null, historieFor.getHistorienId());
                }
                anbieterRepository.updateNid(null, anbieter.getAid());
                anbieterRepository.delete(anbieter);
                historieRepository.save(new Historie(findNutzer(), null, "delete", "Anbieter und Parkplatz"));
            } else if (nutzer.getRolle().equalsIgnoreCase("konsument")) {
                System.out.println("rolle konsument");
                Konsument konsument = konsumentRepository.findByNid(nutzer);
                List<Historie> historieList = historieRepository.findByNid(konsument.getNid());
                for (Historie historieFor : historieList) {
                    historieRepository.updateNid(null, historieFor.getHistorienId());
                }
                List<Parken> parkenList = parkenRepository.findByKid(konsument);
                for (Parken parkenFor : parkenList) {
                    parkenRepository.updateKid(null, parkenFor.getParkid());
                }
                konsumentRepository.delete(konsument);
                historieRepository.save(new Historie(findNutzer(), null, "delete", "Konsument"));
            }
            nutzerRepository.deleteNutzer(nutzer.getNid());
            historieRepository.save(new Historie(findNutzer(), null, "delete", "Nutzer"));
        }
        return "nutzer_geloescht";
    }

    /**
     * Change User rights on Admin Page
     *
     * @param nid    the nid
     * @param button the button
     * @param model  the model
     * @return returnstring
     */
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

    /**
     * Shows the Admin all reservations
     *
     * @param model the model
     * @return admin_alle_ausstehenden_reservierungen
     */
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

    /**
     * Gets the Admin all the transactions
     *
     * @param model the model
     * @return admin_vergangene_transaktionen
     */
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

    /**
     * Gets the Admin the Log table
     *
     * @param model the model
     * @return logtabelle
     */
    @GetMapping("/logtabelle")
    public String logtabelleGet(Model model) {
        List<Historie> histories = historieRepository.findAll();
        model.addAttribute("historien", histories);
        model.addAttribute("nutzerlist", nutzerRepository.findAll());
        model.addAttribute("parkplaetze", parkplatzRepository.findAll());
        return "logtabelle";
    }

    /**
     * Finds a user
     *
     * @return the nutzer
     */
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
