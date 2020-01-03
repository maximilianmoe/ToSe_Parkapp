package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutzerRepository extends JpaRepository<Nutzer, Integer> {

    Nutzer findByNid(Integer nid);
    Optional<Nutzer> findByEmailAdresse (String email);
}
