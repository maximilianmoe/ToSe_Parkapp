package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Anbieter;
import de.g2p.ToSe_Parkapp.Entities.Parkplatz;
import de.g2p.ToSe_Parkapp.Entities.Standort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkplatzRepository extends JpaRepository<Parkplatz, Integer> {

    Parkplatz findByPid(Integer pid);
    Standort findByOrtid(Integer ortid);
    Parkplatz findByAnbieterId(Anbieter aid);
}
