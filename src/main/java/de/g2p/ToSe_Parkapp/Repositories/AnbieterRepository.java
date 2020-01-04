package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Anbieter;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnbieterRepository extends JpaRepository<Anbieter, Integer> {

    Anbieter findByAid(Integer aid);
    Anbieter findByNid(Nutzer nid);
}
