package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Historie;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Repositories.AnbieterRepository;
import de.g2p.ToSe_Parkapp.Repositories.NutzerRepository;
import de.g2p.ToSe_Parkapp.Repositories.ParkplatzRepository;
import de.g2p.ToSe_Parkapp.Repositories.ReservierungenRepository;
import de.g2p.ToSe_Parkapp.Repositories.*;
import de.g2p.ToSe_Parkapp.Service.DBService;
import de.g2p.ToSe_Parkapp.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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
    @Autowired
    KonsumentRepository konsumentRepository;

    MailService mailService = new MailService();
    DBService dbService = new DBService();

    public WebController() throws SQLException {
    }

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
        return "passwort_zuruecksetzen";
    }

    //PostMapping for getting an E-Mail to set a new password.
    @PostMapping("/passwordreset")
    public String resetPasswordPost(@RequestParam("emailaddresse") String emailaddress) {




        MailService mailService = new MailService();
        String resetLink = "http://132.231.36.203:8080/passwort_zuruecksetzen";
        mailService.sendSimpleMessage(emailaddress, "Passwort zurücksetzen", "Bitte öffnen Sie folgenden Link, durch kopieren und einfügen im Browser, um Ihr Passwort zurückzusetzen: \n" + resetLink);

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
            returnString = "error_page";
        }
        return returnString;
    }

    @Scheduled(fixedRate = 1000)
    public void erinnerungSchedule() throws SQLException {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+1:00");
        Calendar c = Calendar.getInstance();
        c.setTimeZone(timeZone);
        Time currentTime = new Time(c.getTime().getTime());

        Statement stmt = null;
        String query = "select nachname, erinnerung, emailadresse from Konsument K join Reservierung R on K.kid = R.kid join Nutzer N on K.nid = N.nid where erinnerung_zeit = '"+currentTime.toString()+"'";

        try {
            stmt = dbService.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int reminderTime = rs.getInt("erinnerung");
                String lastName = rs.getString("nachname");
                String email = rs.getString("emailadresse");
                mailService.sendSimpleMessage(email, "Erinnerung", "Hallo Herr "+lastName+"\nSie müssen Ihren reservierten Parkplatz in "+ reminderTime+" Minuten beparken.");
                System.out.println("EMail gesendet!"+ rs.getString("emailadresse"));
            }
        } catch (SQLException e ) {
            System.err.println(e);
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }


}

