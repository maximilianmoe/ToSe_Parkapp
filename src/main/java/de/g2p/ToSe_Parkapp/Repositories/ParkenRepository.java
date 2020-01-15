package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Konsument;
import de.g2p.ToSe_Parkapp.Entities.Parken;
import de.g2p.ToSe_Parkapp.Entities.Parkplatz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ParkenRepository extends JpaRepository<Parken, Integer> {

    Parken findByKid(Konsument kid);

    @Transactional
    @Modifying
    @Query(value = "update parken set freigabe = :freigabe where parkid = :parkid", nativeQuery = true)
    void updateFreigabe(@Param("freigabe") boolean freigabe, @Param("parkid") Integer parkid);

}
