package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Standort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandortRepository extends JpaRepository<Standort, Integer> {

    Standort deleteByOrtid(Integer ortid);
    Standort findByOrtid(Integer ortid);
}
