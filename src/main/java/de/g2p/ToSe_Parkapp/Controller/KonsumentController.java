package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Konsument;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class KonsumentController {

    @GetMapping("/guthaben")
    public String guthaben(Model model) {
        model.addAttribute("konsument", Konsument.class);
        return "guthabenverwaltung";
    }

    @PostMapping
    public String guthaben() {
        return null;
    }
}
