package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.*;
import de.g2p.ToSe_Parkapp.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReservierungController {
    @Autowired
    ParkenRepository parkenRepository;
    @Autowired
    ReservierungenRepository reservierungenRepository;
    @Autowired
    NutzerRepository nutzerRepository;
    @Autowired
    AnbieterRepository anbieterRepository;
    @Autowired
    KonsumentRepository konsumentRepository;
    @Autowired
    ParkplatzRepository parkplatzRepository;
    @Autowired
    TransaktionRepository transaktionRepository;



    @GetMapping("/meine_reservierungen")
    public String reservierungenGet(Model model) {
        Konsument konsument = konsumentRepository.findByNid(findNutzer());
        List<Parken> parkenList = parkenRepository.findAll();
        if (!konsument.getReserviert()) {
            System.out.println("konsument.getReserviert()");
            model.addAttribute("reservierungen", 0);
        }
        else {
           List<Reservierung> reservierungen = reservierungenRepository.findAll();
           Reservierung reservierungKonsument = null;
           for (Reservierung reservierungFor : reservierungen) {
               if (!reservierungFor.isBeendet())
                   if (reservierungFor.getKid() == konsument)
                       reservierungKonsument = reservierungFor;
           }

            if (reservierungKonsument == null) {
                model.addAttribute("reservierungen", 0);
            } else {
                Parkplatz parkplatz = parkplatzRepository.findByPid(reservierungKonsument.getPidInteger());
                model.addAttribute("reservierungen", reservierungKonsument);
                model.addAttribute("parkplatz", parkplatz);
            }
        }

        //alle öffentlichen beparkten Parkplätze anzeigen
        List<Parken> parkenList1 = parkenRepository.findAll();
        Parken parken = null;
        Parkplatz parkplatz = null;
        if(konsument.getBelegt()) {
            for (Parken parken1: parkenList1) {
                if ((konsument.getKidKonsument() == parken1.getKid()) && !parken1.isFreigabe()) {
                    System.out.println("parken = parken1");
                    parken = parken1;
                }
                if (parken != null) {
                    System.out.println("parken != null");
                    model.addAttribute("parken", 0);
                    if (parken.getOeffentlich()) {
                        System.out.println("parken.getOeffentlich()");
                        parkplatz = parkplatzRepository.findByPid(parken.getPidParkplatz());
                        System.out.println(parken + "  belegt");
                        model.addAttribute("parkplatzParken", parkplatz);
                        model.addAttribute("parken", parken);
                    }
                } else {
                    System.out.println("model parken = 0");
                    model.addAttribute("parken", 0);
                }
            }
        } else {
            System.out.println("nicht belegt");
            model.addAttribute("parken", 0);

        }
        model.addAttribute("rateRes", 0);
        return "meine_reservierungen";
    }

    @PostMapping("/sterneParken")
    public String sterneParkenPost(@RequestParam("rateRes") Integer starsRes, @RequestParam("button") String button) {
        String returnstring = "";
        Konsument konsument = konsumentRepository.findByNid(findNutzer());
        List<Parken> parkenList = parkenRepository.findAll();
        List<Parken> parkens = new ArrayList<>();
        Parken parkenOeffentlich = null;
        Parken parkenPrivat = null;

        for (Parken parken1 : parkenList) {
            if (!parken1.isFreigabe()) {
                if (konsument.getKidKonsument() == parken1.getKid()) {
                    System.out.println(parken1.getKid() + "    kid parken1");
                    assert false;
                    parkens.add(parken1);
                }
                System.out.println(parkens.toString());
            }
        }
        if (button.contains("freigebenSpeichernParken") || button.contains("freigebenZurueckParken")) {
            for (Parken parken1 : parkens) {
                if (parken1.getOeffentlich()) {
                    System.out.println("parken Öffentlich");
                    parkenOeffentlich = parken1;
                }
            }
            assert parkenOeffentlich != null;
            Parkplatz parkplatzPark = parkplatzRepository.findByPid(parkenOeffentlich.getPidParkplatz());
            System.out.println("freigebenSpeichern FreigebenZurueck IfSchleife Parken");
            if (button.contains("freigebenSpeichernParken")) {
                System.out.println(starsRes + "     starsRes Parken");
                System.out.println("freigebenSpeichernParken If Schleife");
                Integer gesamtbewertung = parkplatzPark.getGesamtbewertung()+starsRes;
                Integer bewertungsanzahl = parkplatzPark.getBewertungsanzahl() + 1;
                Integer bewertung = (gesamtbewertung)/bewertungsanzahl;
                parkplatzRepository.updateBewertung(bewertung, bewertungsanzahl, gesamtbewertung, parkplatzPark.getPid());
            }
            returnstring = "home";
            assert false;
            parkenRepository.updateFreigabe(true, parkenOeffentlich.getParkid());
            parkplatzRepository.updateStatus("frei", parkplatzPark.getPid());
            konsumentRepository.updatebelegt(false, konsument.getKid());
        }
        return returnstring;
    }

    @PostMapping("/meine_reservierungen")
    public String reservierungenPost(@RequestParam("rateRes") Integer starsRes,
                                     @RequestParam("button") String button) throws InterruptedException {
        String returnstring="meine_reservierungen";
        Konsument konsument = konsumentRepository.findByNid(findNutzer());
        List<Parken> parkenList = parkenRepository.findAll();
        List<Parken> parkens = new ArrayList<>();
        Parken parkenOeffentlich = null;
        Parken parkenPrivat = null;

        for (Parken parken1 : parkenList) {
            if (!parken1.isFreigabe()) {
                if (konsument.getKidKonsument() == parken1.getKid()) {
                    System.out.println(parken1.getKid() + "    kid parken1");
                    assert false;
                    parkens.add(parken1);
                }
                System.out.println(parkens.toString());
            }
        }
        List<Reservierung> reservierungen = reservierungenRepository.findAll();
        Reservierung reservierung = null;
        for (Reservierung reservierungFor : reservierungen) {
            if (!reservierungFor.isBeendet())
                if (reservierungFor.getKid() == konsument)
                    reservierung = reservierungFor;
        }

        //Stornieren
        if (button.contains("stornieren")) {
            System.out.println("stornieren");
            returnstring = "home";
            konsumentRepository.updateReserviert(false, konsument.getKid());
            reservierungenRepository.updateBeendet(true, reservierung.getRid());

        }

        //beparken
        else if (button.contains("beparken")) {
            System.out.println("beparken");

            if (konsument.getBelegt()){
                returnstring = "error_bereits_belegt";
            } else {
                Transaktion transaktion = new Transaktion();
                Parkplatz parkplatz = reservierung.getPid();
                returnstring = "home";
                //TODO die aktuelle zeit muss bei setStart eingefügt werden
                LocalDateTime ldt = LocalDateTime.now();
                Parken parkenSave = new Parken();
                //parkenSave.setStart(ldt);
                parkenSave.setPid(reservierung.getPid());
                parkenSave.setEnde(null);
                parkenSave.setKid(konsument);
                //transaktion.setAid(parkplatz.getAnbieterId());
                transaktion.setAid(null);
                transaktion.setBetrag(parkplatz.getParkgebuehr());
                transaktion.setGebuehr(false);
                transaktion.setKid(konsument);
                transaktion.setPid(parkplatz);
                //TODO muss noch gesetzt werden
                transaktion.setParkid(null);
                reservierungenRepository.updateResZuParken(true, reservierung.getRid());
                konsumentRepository.updatebelegt(true, konsument.getKid());
                parkenRepository.saveAndFlush(parkenSave);
                parkplatzRepository.updateStatus("belegt", reservierung.getPidInteger());
                transaktionRepository.save(transaktion);
            }
        }

        //Freigabe Res
        else if (button.contains("freigebenSpeichernRes") || button.contains("freigebenZurueckRes")) {
            List<Transaktion> transaktionList = transaktionRepository.findAll();
            Transaktion transaktion = null;
            for (Transaktion transaktionFor : transaktionList) {
                if (transaktionFor.getKid() == konsument)
                    if (!transaktionFor.isAbgeschlossen())
                        transaktion = transaktionFor;
            }
            Parkplatz parkplatzRes = parkplatzRepository.findByPid(reservierung.getPidInteger());
            Integer betrag = 0;
            for (Parken parken1 : parkens)
                if (!parken1.isFreigabe()) {
                    if (!parken1.getOeffentlich()) {
                        parkenPrivat = parken1;
                    }
                }
            if (button.contains("freigebenSpeichernRes")) {
                System.out.println("freigebenSpeichernRes If Schleife");
                Integer gesamtbewertung = parkplatzRes.getGesamtbewertung()+starsRes;
                System.out.println(gesamtbewertung + "     gesamtbewertung");
                System.out.println(starsRes + "     starsRes Reservierung");
                Integer bewertungsanzahl = parkplatzRes.getBewertungsanzahl() + 1;
                Integer bewertung = (gesamtbewertung)/bewertungsanzahl;
                System.out.println(bewertung+"     bewertung");
                parkplatzRepository.updateBewertung(bewertung, bewertungsanzahl, gesamtbewertung, parkplatzRes.getPid());
            }

            if (!transaktion.isAbgeschlossen())
                //TODO abprüfen ob die aktuelle Zeit größer ist als das enddatum der Reservierung
                if (true) {
                    Integer betragT = transaktion.getBetrag();
                    Integer betragP = parkplatzRes.getStrafgebuehr();
                    System.out.println(betragP+" Betrag Parkplatz strafgebühr");
                    System.out.println(betragT+" Betrag Transaktion parkgebühr");
                    betrag = betragP + betragT;
                    System.out.println(betrag+" Gesamtbetrag");
                    transaktionRepository.updateGebuehr(true, betrag, transaktion.getTid());
                    System.out.println(transaktion.getTid()+"   TID transaktion");

                }

            returnstring = "home";
            assert parkenPrivat != null;
            transaktionRepository.updateAbgeschlossen(true, transaktion.getTid());
            Nutzer nutzer = konsument.getNid();
            Integer saldoAktuell = nutzer.getSaldo();
            nutzerRepository.updateSaldo( (saldoAktuell-transaktion.getBetrag()),konsument.getNid().getNid() );
            Nutzer nutzerAnbieter = nutzerRepository.findByNid(parkplatzRes.getAnbieterId().getNid().getNid());
            Integer saldoAnbieter = nutzerAnbieter.getSaldo();
            nutzerRepository.updateSaldo((saldoAnbieter+transaktion.getBetrag()),nutzerAnbieter.getNid());
            konsumentRepository.updatebelegt(false, konsument.getKid());
            konsumentRepository.updateReserviert(false, konsument.getKid());
            parkenRepository.updateFreigabe(true, parkenPrivat.getParkid());
            reservierungenRepository.updateBeendet(true, reservierung.getRid());
            parkplatzRepository.updateStatus("frei", parkplatzRes.getPid());
        }
        return returnstring;
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
