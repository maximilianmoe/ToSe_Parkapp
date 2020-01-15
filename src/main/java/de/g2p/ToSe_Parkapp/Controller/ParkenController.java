package de.g2p.ToSe_Parkapp.Controller;


import de.g2p.ToSe_Parkapp.Repositories.ParkplatzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ParkenController {

    @Autowired
    ParkplatzRepository parkplatzRepository;

    // this method sets the status of the current public parkplatz to the chosen value
    @PostMapping("/spezieller_parkplatz_öffentlich")
    public String spezParkplatzOeffentlichPost(@RequestParam("pid") Integer pid, @RequestParam("belegung") String belegt) {
        System.out.println(pid);
        String status = "frei";
        if (belegt.contains("fremdbelegt")) {
            System.out.println("fremdbelegt");
            status = "fremdbelegt";
        }
        else if (belegt.contains("belegt")) {
            System.out.println("belegt");
            status ="belegt";
        }
        System.out.println(status);
        parkplatzRepository.updateStatus(status, pid);

        return "parkbestaetigung_oeffentlich";
    }

}
