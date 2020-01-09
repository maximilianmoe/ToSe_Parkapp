package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Anbieter;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface AnbieterRepository extends JpaRepository<Anbieter, Integer> {

    Anbieter findByAid(Integer aid);
    Anbieter findByNid(Nutzer nid);

    @Query(value = "select aid from anbieter where nid = :nid ", nativeQuery = true)
    Anbieter findByNidIntegerParam(@Param("nid")Integer nid);

    @Transactional
    @Modifying
    @Query(value = "update anbieter set parkplatz = :parkplatz where aid = :aid", nativeQuery = true)
    void updateParkplatz(@Param("parkplatz") boolean parkplatz, @Param("aid") Integer aid) ;
}
