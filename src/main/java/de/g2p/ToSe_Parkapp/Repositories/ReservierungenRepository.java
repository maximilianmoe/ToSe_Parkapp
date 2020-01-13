package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Konsument;
import de.g2p.ToSe_Parkapp.Entities.Parkplatz;
import de.g2p.ToSe_Parkapp.Entities.Reservierung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservierungenRepository extends JpaRepository<Reservierung, Integer> {

    Reservierung findByKid(Konsument kid);
    Reservierung findByPid(Parkplatz pid);
}
