package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.*;
import de.g2p.ToSe_Parkapp.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class ParkplatzController {

    @Autowired
    ParkplatzRepository parkplatzRepository;
    @Autowired
    NutzerRepository nutzerRepository;
    @Autowired
    AnbieterRepository anbieterRepository;
    @Autowired
    KonsumentRepository konsumentRepository;
    @Autowired
    ParkenRepository parkenRepository;
    @Autowired
    ReservierungenRepository reservierungenRepository;

    Parkplatz parkplatz;

    @GetMapping("/mein_parkplatz_oeffentlich")
    public String meinOeffentlichGet() {
        return "mein_parkplatz_oeffentlich";
    }

    @GetMapping("/parkplatz_hinzufuegen_oeffentlich")
    public String addOeffentlichGet(Model model) {
        Anbieter anbieter = anbieterRepository.findByNid(findNutzer());
        model.addAttribute("parkplatz", new Parkplatz());
        return "parkplatz_hinzufuegen_oeffentlich";
    }

    @PostMapping("/parkplatz_hinzufuegen_oeffentlich")
    public String addOeffentlichPost(@ModelAttribute Parkplatz parkplatz, @RequestParam("fahrzeugtyp") String fahrzeugtyp,
                                     Model model) {

        parkplatz.setBewertung(0);
        parkplatz.setBewertungsanzahl(0);
        parkplatz.setGesamtbewertung(0);
        parkplatz.setAnbieterId(null);
        parkplatz.setStatus("fremdbelegt");
        parkplatz.setPrivat(false);
        parkplatz.setZeitbegrenzung(0);
        parkplatz.setParkgebuehr(0);
        parkplatz.setStrafgebuehr(0);

        if (fahrzeugtyp.contains("on"))
            parkplatz.setFahrzeugtyp(fahrzeugtyp);

        parkplatzRepository.saveAndFlush(parkplatz);

        model.addAttribute("parkplatz", parkplatz);

        return "mein_parkplatz_oeffentlich";
    }

    @GetMapping("/parkplatz_hinzufuegen")
    public String add(Model model) {
        Anbieter anbieter = anbieterRepository.findByNid(findNutzer());
        String returnstring = "";
        //Checks if anbieter already has a Parkplatz
        if (anbieter.getParkplatz()) {
            returnstring = "error_bereits_ein_parkplatz";
        } else {
            model.addAttribute("parkplatz", new Parkplatz());
            returnstring = "parkplatz_hinzufuegen";
        }
        return returnstring;
    }

    @PostMapping("/parkplatz_hinzufuegen")
    public String addParkplatz(@ModelAttribute Parkplatz parkplatz,
                               @RequestParam("parkplatzChecked") String checked,
                               @RequestParam("fahrzeugtyp") String fahrzeugtyp, Model model) {

        String returnstring = "";
        Anbieter aid = anbieterRepository.findByNid(findNutzer());
        parkplatz.setBewertung(0);
        parkplatz.setBewertungsanzahl(0);
        parkplatz.setGesamtbewertung(0);

        //Sets Fahrzeugtyp if box is checked
        if (fahrzeugtyp.contains("on"))
            parkplatz.setFahrzeugtyp(fahrzeugtyp);

        //Sets Parkplatz to private if the box for "privater Parkplatz" is checked
        if (checked.contains("1")) {
            parkplatz.setAnbieterId(aid);
            parkplatz.setPrivat(true);
            anbieterRepository.updateParkplatz(true, aid.getAid());
            anbieterRepository.updatePid(parkplatz.getPid(), aid.getAid());
            parkplatz.setStatus("frei");
            returnstring="mein_parkplatz_oeffentlich";
            model.addAttribute("parkplatz", parkplatz);
        }
        else if (checked.contains("2")) {
            parkplatz.setAnbieterId(null);
            parkplatz.setStatus("fremdbelegt");
            parkplatz.setPrivat(false);
            parkplatz.setZeitbegrenzung(0);
            parkplatz.setParkgebuehr(0);
            parkplatz.setStrafgebuehr(0);
            returnstring="mein_parkplatz";
        }

        //Saves all data in the database
        parkplatzRepository.saveAndFlush(parkplatz);
        return returnstring;
    }


    @GetMapping("/spezieller_parkplatz_privat")
    public String spezParkplatzPrivatGet(Model model) {
        System.out.println("getMapping spezieller privater Parkplatz");
        return "spezieller_parkplatz_privat";
    }

    @PostMapping("/spezieller_parkplatz_öffentlich")
    public String spezParkplatzOeffentlichPost(@RequestParam("pid") Integer pid, @RequestParam("belegung") String belegt,
                                               @ModelAttribute Parken parken) {

        System.out.println(pid +"  PostMapping speziellerÖffentlich");
        Konsument konsument = konsumentRepository.findByNid(findNutzer());
        parken.setKid(konsument);
        parken.setPid(parkplatzRepository.findByPid(pid));
        parken.setOeffentlich(true);
        String returnstring = "";
        String status = "frei";
        if (belegt.contains("fremdbelegt")) {
            status = "fremdbelegt";
            returnstring = "home";

        }
        else if (belegt.contains("belegt")) {
            konsumentRepository.updatebelegt(true, konsument.getKid());
            status ="belegt";
            returnstring = "home";

        }
        System.out.println(status);
        parkenRepository.save(parken);
        parkplatzRepository.updateStatus(status, pid);

        return returnstring;
    }

    @GetMapping("/spezieller_parkplatz_öffentlich")
    public String spezParkplatzOeffentlichGet(Model model) {
        model.addAttribute("parkplaetze", parkplatzRepository.findAll());
        System.out.println("getMapping spezieller öffentlicher Parkplatz");
        return "spezieller_parkplatz_öffentlich";
    }

    @GetMapping("/parkplaetze_medialist")
    public String parkMediaGet(Model model) {
        String returnstring = "";
        Konsument konsument = konsumentRepository.findByNid(findNutzer());
        if (!konsument.getReserviert()) {
            List<Parkplatz> parkplaetze = parkplatzRepository.findAll();
            model.addAttribute("parkplaetze", parkplaetze);
            returnstring = "parkplaetze_medialist";
        } else {
            returnstring = "error_bereits_geparkt";
        }
        return returnstring;
    }

    //handles the redirect to the special_parkingslot page
    @PostMapping("/parkplaetze_medialist")
    public String parkMediaPost(Model model, @RequestParam("button") Integer button) {
        String returnstring="";
        List<Parkplatz> parkenList = parkplatzRepository.findAll();
        Integer pid = 1;
        for (Parkplatz parkplatz : parkenList) {
            pid = parkplatz.getPid();
        }
        System.out.println(pid + "   PID");

        for (int i=1; i<=pid; i++) {
            if (button == i) {
                parkplatz = parkplatzRepository.findByPid(i);
                model.addAttribute("parkplatz", parkplatz);
                if (parkplatz.isPrivat()) {
                    returnstring = "spezieller_parkplatz_privat";
                    model.addAttribute("reservierung", new Reservierung());
                    model.addAttribute("parken", new Parken());
                    break;
                } else if (!parkplatz.isPrivat()) {
                    returnstring = "spezieller_parkplatz_öffentlich";
                    model.addAttribute("parken", new Parken());
                    break;
                }
            } else
                returnstring = "error_page";
        }

        return returnstring;
    }

    @PostMapping("/suche")
    public String suchePost(Model model, @RequestParam("suche") Integer suche) {
        List<Parkplatz> parkplatzList = parkplatzRepository.findByPlz(suche);

        if (parkplatzList.isEmpty()) {
            model.addAttribute("parkplaetze", 0);
        }
        else {
            model.addAttribute("parkplaetze", parkplatzList);
        }

        return "parkplaetze_medialist_suche";
    }

    @GetMapping("/parkplaetze_medialist_suche")
    public String parkMediaSucheGet() {
        return "parkplaetze_medialist_suche";
    }

    @PostMapping("/special_parkingslot")
    public String reserveParkplatz(@ModelAttribute Parken parken, @ModelAttribute Reservierung reservierung,
                                   @RequestParam("startDatum") String startDate, @RequestParam("endeDatum") String endDate
                                   , @RequestParam("startZeit") String startTime, @RequestParam("endeZeit") String endTime,
                                   @Validated Reservierung reservierungValid, BindingResult result) throws ParseException {


        String returnString;
        Nutzer nutzer = findNutzer();


        if (result.hasErrors()) {
            returnString = "special_parkingslot";
        } else {
            System.out.println(startDate+" anfangsstartdatum");
            System.out.println(startTime+" anfangsstartzeit");
            System.out.println(endDate+" anfangsenddatum");
            System.out.println(endTime+" anfangsendzeit");


            if (nutzer.getSaldo() < parkplatz.getParkgebuehr()) {
                returnString = "error_zu_wenig_guthaben";
            } else {

                reservierung.setBeendet(false);
                reservierung.setResZuParken(false);
                reservierung.setKid((konsumentRepository.findByNid(nutzer.getNidNutzer())).getKidKonsument());
                reservierung.setPid(parkplatzRepository.findByPid(parkplatz.getPid()));
                konsumentRepository.updateReserviert(true, nutzer.getNid());

                //Convert time and date
                startDate = startDate.substring(0, 10);
                endDate = endDate.substring(0, 10);
                startTime = startTime.substring(0, 5);
                endTime = endTime.substring(0, 5);

                Date endDateConv = convertDate(endDate);
                endDateConv = datePlusOne(endDateConv);
                System.out.println(endDateConv+" Datum +1");
                reservierung.setEndeDatum(convertSql(endDateConv));
                reservierung.setEndTime(timePlusOne(convertTime(endTime)));

                Date startDateConv = convertDate(startDate);
                startDateConv = datePlusOne(startDateConv);
                System.out.println(startDateConv+" Datum +1");
                reservierung.setStartDatum(convertSql(startDateConv));
                reservierung.setStartTime(timePlusOne(convertTime(startTime)));

                Date reminderDate = endDateConv;

                reservierung.setErinnerungDatum(convertSql(reminderDate));
                reservierung.setErinnerungZeit(convertReminderTime(startTime, nutzer.getErinnerung()));



                //Saves all data in the database
                reservierungenRepository.save(reservierung);
                System.out.println("reseriertung gesaved");

                returnString = "home";
            }
        }
        return returnString;

    }


    @GetMapping("/parkplatz_allgemein")
    public String parkplatzAllg() {
        String returnstring = "";
        Nutzer nutzer = findNutzer();

        if (nutzer.getRolle().equalsIgnoreCase("anbieter"))
            returnstring = "parkplaetze_anbieter";
        else if (nutzer.getRolle().equalsIgnoreCase("beides"))
            returnstring = "parkplaetze_beides";
        else if (nutzer.getRolle().equalsIgnoreCase("konsument"))
            returnstring = "parkplaetze_konsument";

        return returnstring;
    }

    //returns the Parkplatz after it is created

    @GetMapping("/mein_parkplatz")
    public String meinParkplatzGet(Model model) {
        Nutzer nutzer = findNutzer();
        Anbieter anbieter = anbieterRepository.findByNid(nutzer);
        String returnstring = "";
        if (!anbieter.getParkplatz())
            returnstring = "error_noch_kein_parkplatz";
        else {
            Parkplatz parkplatz = parkplatzRepository.findByAnbieterId(anbieterRepository.findByNid(nutzer.getNidNutzer()));
            model.addAttribute("parkplatz", parkplatz);
            returnstring = "mein_parkplatz";
            Reservierung reservierung = null;
            List<Parken> parkenList = parkenRepository.findAll();
            reservierung = reservierungenRepository.findByPid(parkplatz);
            List<Konsument> konsumenten = konsumentRepository.findAll();
            for (Konsument konsumentFor : konsumenten){
                if (reservierung != null) {
                    if (konsumentFor == reservierung.getKid()) {
                        model.addAttribute("reservierung", 0);
                        System.out.println("reservierung = 0 bei Reservierung");
                    }
                } else {
                    model.addAttribute("reservierung", null);
                    System.out.println("reservierung = null bei Reservierung");
                }
            }
            for (Parken parkenFor : parkenList) {
                if (parkenFor.getPid() == parkplatz) {
                    model.addAttribute("reservierung", 0);
                    System.out.println("reservierung = 0 bei Parken");
                }
                else {
                    model.addAttribute("reservierung", null);
                    System.out.println("reservierung = null bei Parken");
                }
            }
        }
        return returnstring;
    }
    @PostMapping("/mein_parkplatz")
    public String meinParkplatzPost(@RequestParam("auswahl") String auswahl) {

        Anbieter anbieter = anbieterRepository.findByNid(findNutzer());
        Parkplatz parkplatz = parkplatzRepository.findByAnbieterId(anbieter);

        String status = "";

        if (auswahl.contains("frei")) {
            status ="frei";
        }
        else if (auswahl.contains("belegt")) {
            status = "belegt";
        }
        else if (auswahl.contains("fremdbelegt")) {
            status = "fremdbelegt";
        }
        System.out.println(status);
        parkplatzRepository.updateStatus(status, parkplatz.getPid());

        return "home";
    }


    @PostMapping("/parkplatz_entfernen")
    public String deleteParkplatz(Model model) {
        Anbieter anbieter = anbieterRepository.findByNid(findNutzer());
        Parkplatz parkplatz = parkplatzRepository.findByAnbieterId(anbieter);
        Reservierung reservierung = null;
        reservierung = reservierungenRepository.findByPid(parkplatz);
        List<Konsument> konsumenten = konsumentRepository.findAll();
        Konsument konsument = null;

        Anbieter anbieterSave = new Anbieter();
        anbieterSave.setNid(findNutzer());
        anbieterSave.setParkplatz(false);
        anbieterRepository.updateParkplatz(false, anbieter.getAid());
        parkplatzRepository.delete(parkplatz);
        anbieterRepository.save(anbieterSave);

        return "home";
    }

    @GetMapping("/error_noch_kein_parkplatz")
    public String errorKeinParkplatz() {
        return "error_noch_kein_parkplatz";
    }

    public Nutzer findNutzer() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String benutzername = "";
        if (principal instanceof UserDetails)
            benutzername = ((UserDetails) principal).getUsername();
        else
            benutzername = principal.toString();

        Nutzer nutzer = nutzerRepository.findByBenutzernameNO(benutzername);
        return nutzer;
    }

    /**
     * This method converts a time, which are given as a String to the format of Time
     *
     * @param time which is given as a String
     * @return time which includes time
     */
    private Time convertTime(String time) {

        try {
            SimpleDateFormat sdfToTime = new SimpleDateFormat("HH:mm");
            long time1 =sdfToTime.parse(time).getTime();
            Time t = new Time(time1);
            return t;
        } catch (ParseException ex2) {
            ex2.printStackTrace();
            return null;
        }
    }

    private Time convertReminderTime(String time, int reminder) {

        try {
            SimpleDateFormat sdfToTime = new SimpleDateFormat("HH:mm");
            long time1 =sdfToTime.parse(time).getTime();
            Time t = new Time(time1);
            Calendar c = Calendar.getInstance();
            c.setTime(t);
            c.add(Calendar.HOUR, 1);
            c.add(Calendar.MINUTE, -reminder);
            long time2 = c.getTime().getTime();
            Time newTime = new Time(time2);
            System.out.println(newTime + " neue Zeit nach reminer abzug");
            return newTime;
        } catch (ParseException ex2) {
            ex2.printStackTrace();
            return null;
        }
    }

    /**
     * This method converts a date, which are given as a String to the format of Date
     *
     * @param date time which is given as a String
     * @return date which includes date
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

    public java.sql.Date convertSql(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
        return sqlDate;
    }


    public java.util.Date datePlusOne(java.util.Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        java.util.Date newDate = c.getTime();
        return newDate;
    }
    public Time timePlusOne(Time time){
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.add(Calendar.HOUR, 1);
        long time2 = c.getTime().getTime();
        Time newTime = new Time(time2);
        return newTime;
    }

}


