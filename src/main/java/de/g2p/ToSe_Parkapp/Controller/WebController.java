package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Historie;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Repositories.AnbieterRepository;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import de.g2p.ToSe_Parkapp.Repositories.ParkplatzRepository;
import de.g2p.ToSe_Parkapp.Repositories.ReservierungenRepository;
import de.g2p.ToSe_Parkapp.Entities.Reservierung;
import de.g2p.ToSe_Parkapp.Repositories.*;
import de.g2p.ToSe_Parkapp.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class WebController {

    @Autowired
    NutzerRepository nutzerRepository;
    @Autowired
    AnbieterRepository anbieterRepository;
    @Autowired
    ParkplatzRepository parkplatzRepository;
    @Autowired
    ReservierungenRepository reservierungenRepository;
    @Autowired
    HistorieRepository historieRepository;

    MailService mailService;

    //GetMapping for the homepage for IP-Adress (or localhost) only
    @GetMapping("/")
    public String homeGet() {
        return "home";
    }

    //GetMapping for the testing page
    @GetMapping("/testweiterleitung")
    public String testweiterleitung() {
        return "testweiterleitung";
    }

    //GetMapping for the error page
    @GetMapping("/errorpage")
    public String error() {
        return "error-page";
    }

    @GetMapping("/error_bereits_ein_parkplatz")
    public String errorExistierenderParkplatz() {
        return "error_bereits_ein_parkplatz";
    }

    //GetMapping for the homepage
    @GetMapping("/home")
    public String homeAdmin(Model model) {
        Nutzer nutzer = findNutzer();
        String returnstring = "";
        if (nutzer.getSperrung()) {
            model.addAttribute("sperrung", 0);
            returnstring = "forward:/login";
        } else {
            if (nutzer.getSaldo() < 0) {
                returnstring = "error_zu_wenig_guthaben";
            } else {
                if (nutzer.getAdmin().equalsIgnoreCase("admin"))
                    returnstring = "home_admin";
                else
                    returnstring = "home";
            }
            model.addAttribute("sperrung", 0);
        }
        return returnstring;
    }

    @GetMapping("/passwort_zurueckgesetzt")
    public String passwortZurueck() {
        return "email_bestaetigt";
    }

    //GetMapping for the Login Page
    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("nutzerList", nutzerRepository.findAll());
        return "login";
    }

    //GetMapping for the logout page and redirect to the login form
    @GetMapping("/logout")
    public String logoutGet() {
        return "logout";
    }
    @PostMapping("/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "login?logout";
    }


    //GetMapping for the buttons on the home page if there is no mapping in other classes

    @GetMapping("/suche")
    public String suche() {
        return "parkplaetze";
    }

    @GetMapping("/aktueller_parkplatz")
    public String aktuellerParkplatz() {
        return "spezieller_parkplatz_privat";
    }

    @GetMapping("/kontakt")
    public String kontakt(){return "kontakt";}

    //GetMapping for getting an E-Mail to set a new password.
    @GetMapping("/passwordreset")
    public String resetPasswordGet(Model model) {
        model.addAttribute("nutzer", new Nutzer());
        return "passwort_zuruecksetzten";
    }

    //PostMapping for getting an E-Mail to set a new password.
    @PostMapping("/passwordreset")
    public String resetPasswordPost(@RequestParam("emailaddresse") String emailaddress) {

//        TODO try convert a String into a HTML hyperlink so that the User ust has to click on it in the mail instead of copy and pasting the url. Following is a example how to do this...
        /*String url = "localhost:8080/newpassword";
        String someVariable = "Passwort zurücksetzten";
        Html entryLink = new Html("<a target=\"_blank\" href=\"" + url + "\">" + someVariable + "</a>");*/

      /*  String resetLink;
        URLConnection connection;
        try {
            connection =  new URL("localhost:8080/newpassword").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            resetLink = scanner.next();
            scanner.close();
        }catch ( Exception ex ) {
            ex.printStackTrace();
        }*/


        MailService mailService = new MailService();
        String resetLink = "http://132.231.36.203:8080/newpassword";
        mailService.sendSimpleMessage(emailaddress, "Passwort zurücksetzten", "Bitte öffnen Sie folgenden Link, durch kopieren und einfügen im Browser, um Ihr Passwort zurückzusetzten: \n" + resetLink);

        return "email_bestaetigt";
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



    //GetMapping for setting the new password.
    @GetMapping("/newpassword")
    public String newPasswordGet(Model model) {

        model.addAttribute("nutzer", new Nutzer());

        return "neues_passwort";
    }

    //PostMapping for setting the new password.
    @PostMapping("/newpassword")
    public String newPasswordPost(@ModelAttribute Nutzer nutzer, @RequestParam("emailaddresse") String emailaddress, @RequestParam("newPassword") String password) {

        boolean check = nutzerRepository.findByEmailAdresse(emailaddress).isPresent();
        String returnString = "";
        if (check) {
            nutzerRepository.updatePasswort(emailaddress, password);
            historieRepository.save(new Historie(nutzer, null, "update", "Passwort"));
            returnString = "passwort_zurueckgesetzt";
        } else {
//            TODO change errror_page to specific error_page for this content
            returnString = "error_page";
        }
        return returnString;
    }

//    @Scheduled(fixedRate = 60000)
//    public void erinnerungSchedule(){
//        TimeZone timeZone;
//        timeZone = TimeZone.getTimeZone("GMT+1:00");
//        TimeZone.setDefault(timeZone);
//        Nutzer nutzer = findNutzer();
//        List<Reservierung> reservierungen = reservierungenRepository.findAll();
//        String email;
//
//        Calendar c = Calendar.getInstance();
//        c.setTimeZone(timeZone);
//        Time currentTime = new Time(c.getTime().getTime());
//
//        for (Reservierung reservierung:reservierungen) {
//            email = nutzer.getEmailAdresse();
//         if (reservierung.getErinnerungZeit() == currentTime) {
//             mailService.sendSimpleMessage(email, "Erinnerung", "Sie müssen Ihren reservierten Parkplatz in der von Ihnen eingestellten Zeit ab jetzt beparken.");
//         }
//        }
//
//    }


}

