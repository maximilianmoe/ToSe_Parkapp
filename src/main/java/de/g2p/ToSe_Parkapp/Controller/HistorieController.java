package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Entities.Historie;
import de.g2p.ToSe_Parkapp.Service.HistorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HistorieController {

    @Autowired
    HistorieService historieService;

//    @GetMapping("/logtabelle")
//    public String getAllHistorien (@RequestParam(defaultValue = "0") Integer pageNo,
//                                   @RequestParam(defaultValue = "3") Integer pageSize,
//                                   @RequestParam(defaultValue = "historienId") String sortBy, Model model) {
//        List<Historie> historieList = historieService.getAllHistorien(pageNo, pageSize, sortBy);
//        model.addAttribute("historienlist", historieList);
//        return "logtabelle";
//
//    }

}
