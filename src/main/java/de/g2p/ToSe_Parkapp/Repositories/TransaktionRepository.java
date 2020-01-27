package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Konsument;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Entities.Transaktion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransaktionRepository extends JpaRepository<Transaktion, Integer> {

    Transaktion findByKid(Konsument kid);

    @Transactional
    @Modifying
    @Query(value = "update transaktion set gebuehr = :gebuehr, betrag = :betrag  where tid = :tid", nativeQuery = true)
    void updateGebuehr(@Param("gebuehr") boolean gebuehr,@Param("betrag") double betrag, @Param("tid") Integer tid);

    @Transactional
    @Modifying
    @Query(value = "update transaktion set abgeschlossen = :abgeschlossen  where tid = :tid", nativeQuery = true)
    void updateAbgeschlossen(@Param("abgeschlossen") boolean abgeschlossen, @Param("tid") Integer tid);

    @Query(value = "select sum(betrag) as gesamtbetrag from transaktion", nativeQuery = true)
    Integer getGesamtvolumen();

}
