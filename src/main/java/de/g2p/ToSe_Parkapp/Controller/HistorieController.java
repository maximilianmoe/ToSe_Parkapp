package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Historie;
import de.g2p.ToSe_Parkapp.Repositories.HistorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HistorieController {

    @Autowired
    HistorieRepository historieRepository;

    @GetMapping("/test")
    public List<Historie> getAll() {
        return historieRepository.findAll();
    }

    @GetMapping("/test/{hid}")
    public Historie getOne(@PathVariable("hid") Integer hid){
        return historieRepository.findByHistorienId(hid);
    }

}
