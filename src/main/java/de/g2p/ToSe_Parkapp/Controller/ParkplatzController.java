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
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @Autowired
    HistorieRepository historieRepository;
    @Autowired
    BildRepository bildRepository;

    Parkplatz parkplatz;

    private String finalImageName;


    /**
     * gets public parking spaces
     *
     * @return mein_parkplatz_oeffentlich
     */
    @GetMapping("/mein_parkplatz_oeffentlich")
    public String meinOeffentlichGet() {
        return "mein_parkplatz_oeffentlich";
    }

    /**
     * Get Mapping for public parking spaces
     *
     * @param model the model
     * @return parkplatz_hinzufuegen_oeffentlich
     */
    @GetMapping("/parkplatz_hinzufuegen_oeffentlich")
    public String addOeffentlichGet(Model model) {
        Anbieter anbieter = anbieterRepository.findByNid(findNutzer());
        model.addAttribute("parkplatz", new Parkplatz());
        return "parkplatz_hinzufuegen_oeffentlich";
    }

    /**
     * Get Mapping to add public parking spaces
     *
     * @param parkplatz   the parkplatz
     * @param fahrzeugtyp the fahrzeugtyp
     * @param model       the model
     * @param imageFile   the image file
     * @return mein_parkplatz_oeffentlich
     */
    @PostMapping("/parkplatz_hinzufuegen_oeffentlich")
    public String addOeffentlichPost(@ModelAttribute Parkplatz parkplatz, @RequestParam("fahrzeugtyp") String fahrzeugtyp,
                                     Model model, @RequestParam("imageFile") MultipartFile imageFile) {

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
        historieRepository.save(new Historie(findNutzer(), parkplatz, "create", "Parkplatz öffentlich"));


        finalImageName = parkplatz.getPid().toString();

        Bild bild = new Bild();
        try {
            saveImage(bild, imageFile);
        } catch (Exception e) {
            e.printStackTrace();
             return "error";
        }
        //Path wird in der methode saveImage gesetzt!
        //sets the filename in the database = pid
        bild.setFileName(parkplatz.getPid().toString());
        //Saves Picture Metadata in the database
        bildRepository.save(bild);

        model.addAttribute("parkplatz", parkplatz);

        return "mein_parkplatz_oeffentlich";
    }

    /**
     * Get Mapping to add a parking space
     *
     * @param model the model
     * @return parkplatz_hinzufuegen
     */
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

    /**
     * Post Mapping to add a parking space
     *
     * @param parkplatz   the parkplatz
     * @param checked     the checked
     * @param fahrzeugtyp the fahrzeugtyp
     * @param model       the model
     * @param imageFile   the image file
     * @return returnstring
     */
    @PostMapping("/parkplatz_hinzufuegen")
    public String addParkplatz(@ModelAttribute Parkplatz parkplatz,
                               @RequestParam("parkplatzChecked") String checked,
                               @RequestParam("fahrzeugtyp") String fahrzeugtyp, Model model,
                               @RequestParam("imageFile") MultipartFile imageFile) {

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
            historieRepository.save(new Historie(aid.getNid(), parkplatz, "update", "Anbieter Privatparkplatz"));
            parkplatz.setStatus("frei");
            returnstring="mein_parkplatz";
            model.addAttribute("parkplatz", parkplatz);
        }
        else if (checked.contains("2")) {
            parkplatz.setAnbieterId(null);
            parkplatz.setStatus("fremdbelegt");
            parkplatz.setPrivat(false);
            parkplatz.setZeitbegrenzung(0);
            parkplatz.setParkgebuehr(0);
            parkplatz.setStrafgebuehr(0);
            returnstring="mein_parkplatz_oeffentlich";
        }



        //Saves all data in the database
        historieRepository.save(new Historie(findNutzer(), parkplatz, "create", "Parkplatz privat"));
        parkplatzRepository.saveAndFlush(parkplatz);
        finalImageName = parkplatz.getPid().toString();

        Bild bild = new Bild();
        try {
            saveImage(bild, imageFile);
        } catch (Exception e) {
            e.printStackTrace();
            returnstring = "error";
        }



        //sets the filename in the database = pid
        bild.setFileName(parkplatz.getPid().toString());
        //Saves Picture Metadata in the database
        bildRepository.save(bild);


        return returnstring;
    }


    /**
     * Gets the special private parking slots
     *
     * @param model the model
     * @return spezieller_parkplatz_privat
     */
    @GetMapping("/spezieller_parkplatz_privat")
    public String spezParkplatzPrivatGet(Model model) {
        return "spezieller_parkplatz_privat";
    }

    /**
     * Post Mapping for the specific public parking space
     *
     * @param pid    the pid
     * @param belegt the belegt
     * @param parken the parken
     * @return returnstring
     */
    @PostMapping("/spezieller_parkplatz_öffentlich")
    public String spezParkplatzOeffentlichPost(@RequestParam("pid") Integer pid, @RequestParam("belegung") String belegt,
                                               @ModelAttribute Parken parken) {

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
            historieRepository.save(new Historie(konsument.getNid(), parken.getPid(), "update", "belegt Konsument"));
            status = "belegt";
            returnstring = "home";
        }

        parkenRepository.save(parken);
        historieRepository.save(new Historie(parken.getKid().getNid(), parken.getPid(), "create", "Parken"));
        parkplatzRepository.updateStatus(status, pid);
        historieRepository.save(new Historie(null, parkplatzRepository.findByPid(pid), "update", "Status"));

        return returnstring;
    }

    /**
     * Get Mapping for special public parking slot
     *
     * @param model the model
     * @return spezieller_parkplatz_öffentlich
     */
    @GetMapping("/spezieller_parkplatz_öffentlich")
    public String spezParkplatzOeffentlichGet(Model model) {
        model.addAttribute("parkplaetze", parkplatzRepository.findAll());
        System.out.println("getMapping spezieller öffentlicher Parkplatz");
        return "spezieller_parkplatz_öffentlich";
    }

    /**
     * Get Mapping for all parking spaces
     *
     * @param model the model
     * @return returnstring
     */
    @GetMapping("/parkplaetze_medialist")
    public String parkMediaGet(Model model) {
        String returnstring = "";
        Konsument konsument = konsumentRepository.findByNid(findNutzer());
        if (konsument == null) {
            returnstring = "error_nur_anbieter";
        } else {
            if (!konsument.getReserviert()) {
                List<Parkplatz> parkplaetze = parkplatzRepository.findAll();
                model.addAttribute("parkplaetze", parkplaetze);
                returnstring = "parkplaetze_medialist";
            } else {
                returnstring = "error_bereits_geparkt";
            }
        }
        return returnstring;
    }

    /**
     * Post Mapping for all parking spaces
     *
     * @param model  the model
     * @param button the button
     * @return returnstring
     */
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

    /**
     * Post Mapping for the search of parking spaces
     *
     * @param model the model
     * @param suche the suche
     * @return parkplaetze_medialist_suche
     */
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

    /**
     * Get Mapping for all searched parking spaces
     *
     * @return parkplaetze_medialist_suche
     */
    @GetMapping("/parkplaetze_medialist_suche")
    public String parkMediaSucheGet() {
        return "parkplaetze_medialist_suche";
    }


    /**
     * Reserve a Parkingslot.
     *
     * @param parken            as typ of Parken
     * @param reservierung      as typ of Reservierung
     * @param startDate         as typ of date
     * @param endDate           as typ of date
     * @param startTime         as typ of time
     * @param endTime           as typ of time
     * @param reservierungValid as typ of Reservierung
     * @param result            as typ of BindingResult
     * @return home.html
     * @throws ParseException the parse exception
     */
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
            System.out.println(startDate+" startdatum");
            System.out.println(startTime+" startzeit");
            System.out.println(endDate+" enddatum");
            System.out.println(endTime+" endzeit");


            if (nutzer.getSaldo() < parkplatz.getParkgebuehr()) {
                returnString = "error_zu_wenig_guthaben";
            } else {

                reservierung.setBeendet(false);
                reservierung.setResZuParken(false);
                reservierung.setKid((konsumentRepository.findByNid(nutzer.getNidNutzer())).getKidKonsument());
                reservierung.setPid(parkplatzRepository.findByPid(parkplatz.getPid()));
                Konsument konsument = konsumentRepository.findByNid(nutzer);
                konsumentRepository.updateReserviert(true,konsument.getKid());
                historieRepository.save(new Historie(nutzer, parken.getPid(), "update", "Reserviert"));

                //Convert time and date
                startDate = startDate.substring(0, 10);
                endDate = endDate.substring(0, 10);
                startTime = startTime.substring(0, 5);
                endTime = endTime.substring(0, 5);

                Date endDateConv = convertDate(endDate);
                endDateConv = datePlusOne(endDateConv);
                System.out.println(endDateConv+" Datum +1");
                reservierung.setEndeDatum(convertSql(endDateConv));
                //reservierung.setEndTime(timePlusOne(convertTime(endTime)));
                reservierung.setEndTime(convertTime(endTime));
                System.out.println("unkonvertiert "+endTime);
                System.out.println("konvertiert "+convertTime(endTime));

                Date startDateConv = convertDate(startDate);
                startDateConv = datePlusOne(startDateConv);
                System.out.println(startDateConv+" Datum +1");
                reservierung.setStartDatum(convertSql(startDateConv));
                //reservierung.setStartTime(timePlusOne(convertTime(startTime)));
                reservierung.setStartTime(convertTime(startTime));

                Date reminderDate = endDateConv;

                reservierung.setErinnerungDatum(convertSql(reminderDate));
                reservierung.setErinnerungZeit(convertReminderTime(startTime, nutzer.getErinnerung()));



                //Saves all data in the database
                System.out.println(reservierung.toString());
                reservierungenRepository.save(reservierung);
                historieRepository.save(new Historie(reservierung.getKid().getNid(), reservierung.getPid(), "create", "Reservierung"));

                returnString = "home";
            }
        }
        return returnString;

    }


    /**
     * Get Mapping for transfer site depending on role
     *
     * @return returnstring
     */
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


    /**
     * Get Mapping for the users Parking space
     *
     * @param model the model
     * @return returnstring
     */
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
            List<Parken> parkenList = parkenRepository.findAll();
            Reservierung reservierung = null;
            List<Reservierung> reservierungList = reservierungenRepository.findAll();
            for (Reservierung reservierungFor : reservierungList) {
                if (reservierungFor.getPid().getPid() == parkplatz.getPid())
                    if (!reservierungFor.isBeendet())
                        reservierung = reservierungFor;
            }
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
                    if (!parkenFor.isFreigabe()) {
                        model.addAttribute("reservierung", 0);
                        System.out.println("reservierung = 0 bei Parken");
                    } else {
                        model.addAttribute("reservierung", null);
                        System.out.println("reservierung = null bei Parken");
                    }
                }
            }
        }
        return returnstring;
    }

    /**
     * Post Mapping for the action of the user specific parking slot
     *
     * @param auswahl the auswahl
     * @return home
     */
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
        historieRepository.save(new Historie(null, parkplatz, "update", "Status Parkplatz"));

        return "home";
    }


    /**
     * Post Mapping to delete a parking space as a user
     *
     * @param model the model
     * @return the home
     */
    @PostMapping("/parkplatz_entfernen")
    public String deleteParkplatz(Model model) {
        Anbieter anbieter = anbieterRepository.findByNid(findNutzer());
        Parkplatz parkplatz = parkplatzRepository.findByAnbieterId(anbieter);
        Reservierung reservierung = null;
        List<Reservierung> reservierungList = reservierungenRepository.findByPidList(parkplatz.getPid());
        for (Reservierung reservierungFor : reservierungList) {
            if (!reservierungFor.isBeendet()) {
                reservierung = reservierungFor;
            }
        }
        List<Konsument> konsumenten = konsumentRepository.findAll();
        Konsument konsument = null;

        List<Historie> historieList = historieRepository.findByPid(parkplatz);
        for (Historie historieFor: historieList) {
            historieRepository.updatePid(null, historieFor.getHistorienId());
        }

        anbieterRepository.updateParkplatz(false, anbieter.getAid());
        historieRepository.save(new Historie(anbieter.getNid(), null, "update", "Parkplatzstatus Anbieter"));
        parkplatzRepository.updateAid(null, parkplatz.getPid());
        parkplatzRepository.deleteParkplatz(parkplatz.getPid());
        historieRepository.save(new Historie(anbieter.getNid(), null, "delete", "Parkplatz"));

        return "home";
    }

    /**
     * Get Mapping for the error, if a parking space was not yet created
     *
     * @return nutzer
     */
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
     * @return time as typ of Time
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

    /**
     * This method converts the given time so that you have the time when the reminder should remind you
     *
     * @param time time which is given as String
     * @param reminder time which is given as int
     * @return newTime of the typ java.sql.Date
     */
    private Time convertReminderTime(String time, int reminder) {

        try {
            SimpleDateFormat sdfToTime = new SimpleDateFormat("HH:mm");
            long time1 =sdfToTime.parse(time).getTime();
            Time t = new Time(time1);
            Calendar c = Calendar.getInstance();
            c.setTime(t);
//            c.add(Calendar.HOUR, 1);
            c.add(Calendar.MINUTE, -reminder-1);
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
     * @return date of the typ Date
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
    /**
     * This method converts a java.util.Date to a java.sql.Date
     *
     * @param date time which is given as Date
     * @return sqlDate of the typ java.sql.Date
     */
    public java.sql.Date convertSql(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
        return sqlDate;
    }

  /**
   * This method adds to the Date one Day
   *
   * @param date time which is given as Date
   * @return newDate of the typ Date
   */
  public java.util.Date datePlusOne(java.util.Date date){
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.DATE, 0);
      java.util.Date newDate = c.getTime();
      return newDate;
  }


    /**
     * Saves an image to the file system - Differently for Deployment on Apache Tomcat
     *
     * @param bild      the bild
     * @param imageFile the image file
     * @throws Exception the exception
     */
    public void saveImage(Bild bild, MultipartFile imageFile) throws Exception {

 //     //IntelliJ Implementation
 //     //findet das aktuelle directory
 //     Path currentPath = Paths.get("");
 //     //findet den kompletten Pfad vom Root directory bis zum aktuellen directory
 //     Path absolutePath = currentPath.toAbsolutePath();
 //     //System.out.println(absolutePath.toString());
 //     bild.setPath(absolutePath + "/src/main/resources/static/photos/");
 //     byte[] bytes = imageFile.getBytes();
 //     Path path = Paths.get(bild.getPath() + finalImageName + ".jpg");
 //     Files.write(path, bytes);

       //use for deployment on tomcat
       String folder = "/usr/local/apache-tomcat9/webapps/good2park/WEB-INF/classes/static/photos/";
       byte[] bytes = imageFile.getBytes();
       Path path = Paths.get(folder + finalImageName + ".jpg");
       Files.write(path, bytes);
    }

    /**
        Autocomplete:
        @param term übergibt den Input-Wert der Suchleiste von parkplaetze_medialist.html
        Eine Liste aller Parkplätze wird erstellt und zu jedem Parkplatz wird die PLZ vorübergehend mit der
        toString Methode in der variable plz abgespeichert.
        Danach wird die PLZ des Parkplatzes mit dem Input-Wert term verglichen.
        Wenn der Input-Wert in der PLZ vorkommt wird die PLZ zur Liste suggestions hinzugefügt.
        Am Ende wird die Liste an suggestions returned.
     */
    @RequestMapping(value="/ParkplatzPlzAutocomplete")
    @ResponseBody
    public List<String> ParkplatzPlzAutocomplete(@RequestParam (value="term", required = false, defaultValue="")String term) {
        List<String> suggestions = new ArrayList<String>();
        List<Parkplatz> parkplaetze = parkplatzRepository.findAll();
        for (Parkplatz parkplatz1 : parkplaetze) {
            //suggestions.add(parkplatz1.getPlz().toString());
            String plz = parkplatz1.getPlz().toString();
            if(plz.contains(term)){
                suggestions.add(parkplatz1.getPlz().toString());
            }
        }
        return suggestions;
    }

}


