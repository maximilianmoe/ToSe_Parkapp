package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.*;
import de.g2p.ToSe_Parkapp.Repositories.ParkenRepository;
import de.g2p.ToSe_Parkapp.Repositories.ReservierungenRepository;
import de.g2p.ToSe_Parkapp.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
public class ReservierungController {
    @Autowired
    ParkenRepository parkenRepository;
    @Autowired
    ReservierungenRepository reservierungenRepository;

//    MailService mailService = new MailService();

    @GetMapping("/meine_reservierungen")
    public String reservierungenGet() {
        return "meine_reservierungen";
    }

    @GetMapping("/reserve")
    public String reserve(Model model) {
//        mailService.sendSimpleMessage("lehnermaxi@yahoo.de", "Test", "test");
        model.addAttribute("reservierung", new Reservierung());
        model.addAttribute("parken", new Parken());
        return "spezieller_parkplatz";

    }

    @PostMapping("/reserve")
    public String reserveParkplatz(@ModelAttribute Parkplatz parkplatz,  @ModelAttribute Parken parken, @ModelAttribute Reservierung reservierung, @ModelAttribute Konsument konsument, @RequestParam("reservierungChecked") String checked, @RequestParam("startDatum") String startDatum, @RequestParam("endeDatum") String endeDatum) {

        reservierung.setBeendet(false);
        reservierung.setResZuParken(true);
        reservierung.setKid(konsument);
        reservierung.setPid(parkplatz);

        //Convert time and date
        startDatum =startDatum.substring(0, 10);
        endeDatum =endeDatum.substring(0, 10);
/*        startTime = startTime.substring(0, 5);
        endTime = endTime.substring(0, 5);
        */
        Date dts = convertDate(startDatum);
        Date dte = convertDate(endeDatum);




        //      checks if reminder is requested. Weather checked == "1", consumer chose to get a reminder in the dropdown menue.
        if (checked.contains("1")) {
            //      Erinnerung is hardcoded here to 30 minutes before the parking time ends.
//            parken.setErinnerung(reservierung.getEnde().minusMinutes(30));
            System.out.println("workedyes");
        }else if (checked.contains("2")) {
//            parken.setErinnerung(null);
            System.out.println("workedno");
        }



        //Saves all data in the database
        parkenRepository.save(parken);
        reservierungenRepository.save(reservierung);

        //Übersichtsseite erstellen und den Namen hier ändern
        return "testweiterleitung";
    }

    /**
     * This method converts a date and a time, which are given as a String to the format of date
     *
     * @param date date which is given as a String
     * @param time time which is given as a String
     *
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


    /**
     * This method converts a date, which is given as a String to the format of date
     *
     * @param date date which is given as a String
     *
     * @return date date which is formatted as Date
     */
    private Date convertDate(String date) {
        try {
            SimpleDateFormat stfDate = new SimpleDateFormat("yyyy-MM-dd");
            Date date2 = stfDate.parse(date);
            return date2;
        } catch (ParseException ex2) {
            ex2.printStackTrace();
            return null;
        }
    }
}
