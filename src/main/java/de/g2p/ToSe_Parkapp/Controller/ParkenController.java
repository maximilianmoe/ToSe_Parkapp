package de.g2p.ToSe_Parkapp.Controller;


import de.g2p.ToSe_Parkapp.Entities.Parkplatz;
import de.g2p.ToSe_Parkapp.Repositories.ParkplatzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class ParkenController {

    @Autowired
    ParkplatzRepository parkplatzRepository;

    private Map<Integer, Parkplatz> parkplatzMap = new HashMap<>();
    // this method sets the status of the current public parkplatz to the chosen value


}
