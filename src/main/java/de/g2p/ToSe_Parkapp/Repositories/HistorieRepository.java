package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Historie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorieRepository extends JpaRepository<Historie, Integer> {

    List<Historie> findAll();
    Historie findByHistorienId(Integer hid);
}
