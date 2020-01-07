package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface NutzerRepository extends JpaRepository<Nutzer, Integer> {

    Optional<Nutzer> findByEmailAdresse(String emailadresse);
    Nutzer findByNid(Integer nid);
    Optional<Nutzer> findByBenutzername (String benutzername);

    @Query(value = "select * from nutzer where benutzername = :benutzername", nativeQuery = true)
    Nutzer findByBenutzernameNO(@Param("benutzername") String benutzername);

    @Query(value = "select * from nutzer where nid = :nid ", nativeQuery = true)
    Integer findByNidInteger(@Param("nid")Integer nid);

    @Transactional
    @Modifying
    @Query(value = "update nutzer set saldo = :saldo where nid = :nid", nativeQuery = true)
    void updateSaldo(@Param("saldo") Integer saldo, @Param("nid") Integer nid);

    @Transactional
    @Modifying
    @Query(value = "update nutzer set sperrung = :sperrung where nid = :nid",nativeQuery = true)
    void updateSperrung(@Param("nid") Integer nid,@Param("sperrung") boolean sperrung);

    @Transactional
    @Modifying
    @Query(value = "update nutzer set admin = :admin where nid = :nid", nativeQuery = true)
    void updateAdmin(@Param("nid") Integer nid, @Param("admin") boolean admin);

    @Transactional
    @Modifying
    @Query(value = "update nutzer set passwort = :passwort where emailadresse = :emailadresse", nativeQuery = true)
    void updatePasswort(@Param("emailadresse") String emailadress, @Param("passwort") String password);

}
