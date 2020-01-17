package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.*;
import de.g2p.ToSe_Parkapp.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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


    @GetMapping("/meine_reservierungen")
    public String reservierungenGet(Model model) {
        Konsument konsument = konsumentRepository.findByNid(findNutzer());
        List<Parken> parkenList = parkenRepository.findAll();
        System.out.println("GetMapping meine Reservierungen");
        if (!konsument.getReserviert()) {
            System.out.println("konsument.getReserviert()");
            model.addAttribute("reservierungen", 0);
        }
        else {
            Reservierung reservierung = reservierungenRepository.findByKid(konsument);
            Parkplatz parkplatz = parkplatzRepository.findByPid(reservierung.getPidInteger());
            if (reservierungenRepository.findByKid(konsument) == null) {
                model.addAttribute("reservierungen", 0);
            } else
                model.addAttribute("reservierungen", reservierung);

            model.addAttribute("parkplatz", parkplatz);
        }

        //alle öffentlichen beparkten Parkplätze anzeigen
        List<Parken> parkenList1 = parkenRepository.findAll();
        Parken parken = null;
        Parkplatz parkplatz = null;
        if(konsument.getBelegt()) {
            for (Parken parken1: parkenList1) {
                if ((konsument.getKidKonsument() == parken1.getKid()) && !parken1.isFreigabe()) {
                    parken = parken1;
                }
                if (parken != null) {
                    parkplatz = parkplatzRepository.findByPid(parken.getPidParkplatz());


                    if (parken.getOeffentlich()) {
                        System.out.println(parken + "  belegt");
                        model.addAttribute("parkplatzParken", parkplatz);
                        model.addAttribute("parken", parken);
                    }
                } else {
                    model.addAttribute("parken", 0);
                }
            }
        } else {
            System.out.println("nicht belegt");
            model.addAttribute("parken", 0);

        }
        return "meine_reservierungen";
    }

    @PostMapping("/meine_reservierungen")
    public String reservierungenPost(@RequestParam("rateRes") Integer starsRes,
                                     @RequestParam("button") String button) throws InterruptedException {
        //TODO insert the method for deleting the Reservierung from the database

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
        Reservierung reservierung = reservierungenRepository.findByKid(konsument.getKidKonsument());

        //Freigabe Res
        if (button.contains("freigebenSpeichernRes") || button.contains("freigebenZurueckRes")) {
            Parkplatz parkplatzRes = parkplatzRepository.findByPid(reservierung.getPidInteger());
            for (Parken parken1 : parkens)
                if (!parken1.isFreigabe()) {
                    if (!parken1.getOeffentlich()) {
                        System.out.println("parken privat");
                        parkenPrivat = parken1;
                    }
                }
            System.out.println("freigebenSpeichern FreigebenZurueck IfSchleife Reservieren");
            if (button.contains("freigebenSpeichernRes")) {
                System.out.println("freigebenSpeichernRes If Schleife");
                Integer gesamtbewertung = parkplatzRes.getGesamtbewertung()+starsRes;
                Integer bewertungsanzahl = parkplatzRes.getBewertungsanzahl() + 1;
                Integer bewertung = (gesamtbewertung)/bewertungsanzahl;
                parkplatzRepository.updateBewertung(bewertung, bewertungsanzahl, gesamtbewertung, parkplatzRes.getPid());
            }
            returnstring = "home";
            assert parkenPrivat != null;
            parkenRepository.updateFreigabe(true, parkenPrivat.getParkid());
            reservierungenRepository.updateBeendet(true, reservierung.getRid());
            parkplatzRepository.updateStatus("frei", parkplatzRes.getPid());
        }
        //Freigabe Parken
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
                System.out.println("freigebenSpeichernParken If Schleife");
                Integer gesamtbewertung = parkplatzPark.getGesamtbewertung()+starsRes;
                Integer bewertungsanzahl = parkplatzPark.getBewertungsanzahl() + 1;
                Integer bewertung = (gesamtbewertung)/bewertungsanzahl;
                parkplatzRepository.updateBewertung(bewertung, bewertungsanzahl, gesamtbewertung, parkplatzPark.getPid());
            }
            returnstring = "home";
            assert parkenPrivat != null;
            parkenRepository.updateFreigabe(true, parkenOeffentlich.getParkid());
            parkplatzRepository.updateStatus("frei", parkplatzPark.getPid());
        }


        //TODO res und Parken werden bei den Sternen nicht unterschieden,
        // es wird nur bei Parken gefragt ob öffentlich geparkt oder nicht

        //Stornieren
        else if (button.contains("stornieren")) {
            System.out.println("stornieren");
            returnstring = "home";
            konsumentRepository.updateReserviert(false, konsument.getKid());
            reservierungenRepository.updateBeendet(true, reservierung.getRid());
            //reservierungenRepository.delete(reservierung);

        }

        //beparken
        else if (button.contains("beparken")) {
            System.out.println("beparken");
            returnstring = "meine_reservierungen";
            //TODO finish the Time part
            Parken parkenSave = new Parken();
            parkenPrivat.setStart(null);
            parkenPrivat.setEnde(null);
            parkenPrivat.setKid(konsument);
            //parkenPrivat.setPid(parkplatz);
            //parken.setStart(sqlDate);
            parkenRepository.save(parkenSave);
        }
        return returnstring;
    }



    /**
     * This method converts a date and a time, which are given as a String to the format of date
     *
     * param date date which is given as a String
     * param time time which is given as a String
     * @return date which includes date and time
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
