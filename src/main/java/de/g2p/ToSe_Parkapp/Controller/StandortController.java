package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Standort;
import de.g2p.ToSe_Parkapp.Repositories.StandortRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
@RequestMapping("/standort")
@RestController
public class StandortController {

    @Autowired
    StandortRepository standortRepository;

    @GetMapping("/getall")
    public List<Standort> findAll(){
        return standortRepository.findAll();
    }

    @GetMapping("/add")
    public String insertOne(){
        standortRepository.save(new Standort("Hauptstrasse",12,94124,
                "121353.123", "346135.436134"));
        //noch herausfinden, wie das mit redirect funktioniert
        return "redirect:/anbieter/getall";
    }

    //If one gets deleted, ortid will be auto_incremented as it was before
    @GetMapping("/delete-{ortid}")
    public List<Standort> deleteOne(@PathVariable("ortid") Integer ortid){
        standortRepository.deleteById(ortid);
        return findAll();
    }
}
