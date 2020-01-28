package de.g2p.ToSe_Parkapp.Service;

import de.g2p.ToSe_Parkapp.Entities.Historie;
import de.g2p.ToSe_Parkapp.Repositories.HistorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistorieService {

    @Autowired
    HistorieRepository historieRepository;

    public List<Historie> getAllHistorien(Integer pageNo, Integer pageSize, String sortBy) {
        PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<Historie> pagedResult = historieRepository.findAll(paging);

        if (pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }
}
