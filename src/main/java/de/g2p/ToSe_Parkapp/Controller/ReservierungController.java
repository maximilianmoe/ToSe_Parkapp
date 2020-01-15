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
        if (!konsument.getReserviert())
            model.addAttribute("reservierungen", 0);
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
        List<Parken> parkenList = parkenRepository.findAll();
        Parken parken = null;
        if(konsument.getBelegt()) {
            for (Parken parken1: parkenList) {
                if (konsument.getKidKonsument() == parken1.getKid() || parken1.isFreigabe()) {
                    parken = parken1;
                }
            }
            assert parken != null;
            System.out.println(parken.getParkid()+"  parkid "+parken.getOeffentlich()+"  öffentlich");

            Parkplatz parkplatz = parkplatzRepository.findByPid(parken.getPidParkplatz());
            if(parken.getOeffentlich()) {
                System.out.println(parken + "  belegt");
                model.addAttribute("parkplatzParken", parkplatz);
                model.addAttribute("parken", parken);
            } else {
                model.addAttribute("parken", 0);
            }
        } else {
            System.out.println("nicht belegt");
            model.addAttribute("parken", 0);

        }
        return "meine_reservierungen";
    }

    @PostMapping("/meine_reservierungen")
    public String reservierungenPost(@RequestParam("rateRes") Integer starsRes, @RequestParam("rateParken") Integer starsParken,
                                     @RequestParam("button") String button) throws InterruptedException {
        //TODO insert the method for deleting the Reservierung from the database
        String returnstring="meine_reservierungen";
        Konsument konsument = konsumentRepository.findByNid(findNutzer());
        List<Parken> parkenList = parkenRepository.findAll();
        Parken parken = null;
        for (Parken parken1: parkenList) {
            if (konsument.getKidKonsument() == parken1.getKid() || parken1.isFreigabe()) {
                parken = parken1;
            }
        }
        Reservierung reservierung = reservierungenRepository.findByKid(konsument.getKidKonsument());
        Parkplatz parkplatzRes = parkplatzRepository.findByPid(reservierung.getPidInteger());
        assert parken != null;
        Parkplatz parkplatzPark = parkplatzRepository.findByPid(parken.getPidParkplatz());

        if (button.contains("freigebenSpeichernRes") || button.contains("freigebenZurueckRes")) {
                if (button.contains("freigebenSpeichernRes")) {
                    Integer gesamtbewertung = parkplatzRes.getGesamtbewertung()+starsRes;
                    Integer bewertungsanzahl = parkplatzRes.getBewertungsanzahl() + 1;
                    Integer bewertung = (gesamtbewertung)/bewertungsanzahl;
                    parkplatzRepository.updateBewertung(bewertung, bewertungsanzahl, gesamtbewertung, parkplatzRes.getPid());
                }
                returnstring = "meine_reservierungen";
                //TODO löschen einbinden;
            parkenRepository.updateFreigabe(true, parken.getParkid());
            //reservierungenRepository.delete(reservierung);
        }
        if (button.contains("freigebenSpeichernParken") || button.contains("freigebenZurueckParken")) {
            if (button.contains("freigebenSpeichernParken")) {
                Integer gesamtbewertung = parkplatzRes.getGesamtbewertung()+starsParken;
                Integer bewertungsanzahl = parkplatzRes.getBewertungsanzahl() + 1;
                Integer bewertung = (gesamtbewertung)/bewertungsanzahl;
                parkplatzRepository.updateBewertung(bewertung, bewertungsanzahl, gesamtbewertung, parkplatzPark.getPid());
            }
            returnstring = "meine_reservierungen";
            //TODO löschen einbinden;
            parkenRepository.updateFreigabe(true, parken.getParkid());
            //reservierungenRepository.delete(reservierung);
        }
        else if (button.contains("stornieren")) {
            System.out.println("stornieren");
            returnstring = "home";
//            reservierungenRepository.delete(reservierung);

        }
        else if (button.contains("beparken")) {
            System.out.println("beparken");
            returnstring = "meine_reservierungen";
            //TODO finish the Parken part
//            Parken parken = new Parken();
//            parken.setStart();
//            parken.setEnde();
//            parken.setKid(konsument);
//            parken.setPid(parkplatz);
            //parken.setStart(sqlDate);
            //parkenRepository.save();
        }
        return returnstring;
    }

    @PostMapping("/special_parkingslot/{id}")
    public String reserveParkplatz( @ModelAttribute Parkplatz parkplatz, @ModelAttribute Parken parken, @ModelAttribute Reservierung reservierung, @RequestParam("startDatum") String startDate, @RequestParam("endeDatum") String endDate, @RequestParam("startZeit") String startTime, @RequestParam("endeZeit") String endTime) {
        String returnString = "";
        Nutzer nutzer = findNutzer();

        if (nutzer.getSaldo() < parkplatz.getParkgebuehr()) {
//            TODO change errror_page to specific error_page for this content
            returnString = "error_page";
        }else{
            reservierung.setBeendet(false);
            reservierung.setResZuParken(true);
            reservierung.setKid((konsumentRepository.findByNid(nutzer.getNidNutzer())).getKidKonsument());
            reservierung.setPid(parkplatz);

            //Convert time and date
            startDate = startDate.substring(0, 10);
            endDate = endDate.substring(0, 10);
            startTime = startTime.substring(0, 5);
            endTime = endTime.substring(0, 5);

            Date dts = convertTime(startDate, startTime);
            Date dte = convertTime(endDate, endTime);
            Date dtr = dte;

            reservierung.setStart(convertSql(dts));
            reservierung.setEnde(convertSql(dte));
            //Erinnerung is hardcoded here to 30 minutes before the parking time ends.
            parken.setErinnerung(convertSqlReminder(dtr));
            System.out.println("1");

            //Saves all data in the database
            //TODO wird hier wirklich nur in parken gespeichert oder auch in reservierung?
            parkenRepository.save(parken);
            System.out.println("2");
//           TODO throws exception when comming to this code... don't no why, says that 'saldo' isn't in 'field list'....
            reservierungenRepository.save(reservierung);
            System.out.println("3");

//           TODO Übersichtsseite erstellen und den Namen hier ändern
            returnString = "testweiterleitung";
        }
        return returnString;
    }

    /**
     * This method converts a date and a time, which are given as a String to the format of date
     *
     * @param date date which is given as a String
     * @param time time which is given as a String
     * @return date which includes date and time
     */
    private Date convertTime(String date, String time) {
        String timestamp = date + " " + time;
        try {
            SimpleDateFormat sdfToDate = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm");
            Date date1 = sdfToDate.parse(timestamp);
            return date1;
        } catch (ParseException ex2) {
            ex2.printStackTrace();
            return null;
        }
    }

    public java.sql.Date convertSql(Date dt){

        Calendar cal = Calendar.getInstance();
        // remove next line if you're always using the current time.
        cal.setTime(dt);
        java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
        return sqlDate;
    }
    public java.sql.Date convertSqlReminder(Date dt){

        Calendar cal = Calendar.getInstance();
        // remove next line if you're always using the current time.
        cal.setTime(dt);
        cal.add(Calendar.MINUTE, -30);
        java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
        return sqlDate;
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
