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
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Controller
public class ReservierungController {
    @Autowired
    ParkenRepository parkenRepository;
    @Autowired
    ReservierungenRepository reservierungenRepository;
    @Autowired
    NutzerRepository nutzerRepository;
    @Autowired
    KonsumentRepository konsumentRepository;
    @Autowired
    ParkplatzRepository parkplatzRepository;



    @GetMapping("/meine_reservierungen")
    public String reservierungenGet() {
        return "meine_reservierungen";
    }

    @GetMapping("/special_parkingslot-{id}")
    public String reserve(Model model, @PathVariable("id") Integer id) {
        Parkplatz parkplatz = parkplatzRepository.findByPid(id);
        model.addAttribute("reservierung", new Reservierung());
        model.addAttribute("parken", new Parken());
        model.addAttribute("parkplatz", parkplatz);
        return "spezieller_parkplatz";

    }

    @PostMapping("/special_parkingslot")
    public String reserveParkplatz( @ModelAttribute Parkplatz parkplatz, @ModelAttribute Parken parken, @ModelAttribute Reservierung reservierung, @ModelAttribute Konsument konsument, @RequestParam("startDatum") String startDate, @RequestParam("endeDatum") String endDate, @RequestParam("startZeit") String startTime, @RequestParam("endeZeit") String endTime) {
        String returnString = "";
        Nutzer nutzer = findNutzer();

        if (/*nutzer.getSaldo() < parkplatz.getParkgebuehr()*/false) {
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
            parkenRepository.save(parken);
            System.out.println("2");
//           TODO throws exception when comming to this code... don't no why, says that 'saldo' isn't in 'field list'....
//            reservierungenRepository.save(reservierung);
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
