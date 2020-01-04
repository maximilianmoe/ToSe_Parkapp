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

//    @GetMapping("/guthaben")
//    public String validate(Model model) {
//        String returnstring = "";
//        Konsument konsument = konsumentRepository.findByKid(4);
//        System.out.println(konsument.getSaldo());
//
//        if (konsument.getSaldo() < 0)
//            returnstring = "Guthaben_ueberzogen";
//        else {
//            returnstring = "guthabenverwaltung";
//            System.out.println("ghv weiterleitung");
//            model.addAttribute("konsument", konsument);
//
//        }
//        return returnstring;
//    }
//
//    @GetMapping("/guthabenverwaltung")
//    public String guthabenGet(Model model) {
//        System.out.println("getmapping");
//        //kid = 4 for testing
//        Konsument konsument = konsumentRepository.findByKid(4);
//        model.addAttribute("konsument", konsument);
//        return "guthabenverwaltung";
//    }
//
//    @PostMapping("/guthabenverwaltung")
//    public String guthabenPost(Konsument konsument, @RequestParam("button") String button,
//                               @RequestParam("betrag") double betrag) {
//        //kid = 4 for testing
//        Konsument konsument2 = konsumentRepository.findByKid(4);
//        double saldo = konsument2.getSaldo();
//
//        //Check which button was pressed (Aufladen or Auszahlen)
//        if (button.contains("1"))
//            saldo = saldo + betrag;
//        else if (button.contains("2"))
//            saldo = saldo - betrag;
//
//        konsumentRepository.update(saldo);
//        //redirect to guthabenweiterleitung because the new saldo is not displayed properly
//        return "guthabenweiterleitung";
//    }

//    //returns the user
//    private String findNutzer() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username;
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails) principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
//        System.out.println(username);
//        return username;
//    }
}
