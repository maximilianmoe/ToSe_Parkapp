package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Konsument;
import de.g2p.ToSe_Parkapp.Repositories.KonsumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KonsumentController {

    @Autowired
    KonsumentRepository konsumentRepository;

    @GetMapping("/guthaben")
    public String validate() {
        String returnstring = "";
        Konsument konsument = konsumentRepository.findByKid(4);
        if (konsument.getSaldo() < 0)
            returnstring = "Guthaben_ueberzogen";
        else
            returnstring = "guthabenverwaltung";

        return returnstring;
    }

    @GetMapping("/guthabenverwaltung")
    public String guthaben(Model model) {
        //kid = 4 for testing
        Konsument konsument = konsumentRepository.findByKid(4);
        model.addAttribute("konsument", konsument);
        return "guthabenverwaltung";
    }

    @PostMapping("/guthabenverwaltung")
    public String guthaben(Konsument konsument, Model model, @RequestParam("button") String button,
                           @RequestParam("betrag") double betrag) {
        //kid = 4 for testing
        Konsument konsument2 = konsumentRepository.findByKid(4);
        double saldo = konsument2.getSaldo();

        //Check which button was pressed (Aufladen or Auszahlen)
        if (button.contains("1"))
            saldo = saldo + betrag;
        else if (button.contains("2"))
            saldo = saldo - betrag;

        konsumentRepository.update(saldo);
        //redirect to home because the new saldo is not displayed properly
        return "guthabenweiterleitung";
    }
}
