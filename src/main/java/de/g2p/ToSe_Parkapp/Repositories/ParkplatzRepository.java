package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Anbieter;
import de.g2p.ToSe_Parkapp.Entities.Parkplatz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ParkplatzRepository extends JpaRepository<Parkplatz, Integer> {

    Parkplatz findByPid(Integer pid);
    Parkplatz findByAnbieterId(Anbieter aid);
    List<Parkplatz> findByPlz(Integer plz);

    @Transactional
    @Modifying
    @Query(value = "update parkplatz set pstat = :pstat where pid = :pid", nativeQuery = true)
    void updateStatus(@Param("pstat") String pstat, @Param("pid") Integer pid);

    @Transactional
    @Modifying
    @Query(value = "update parkplatz set bewertung = :bewertung, bewanz = :bewanz, gesamtbewertung = :gesamtbewertung where pid = :pid", nativeQuery = true)
    void updateBewertung(@Param("bewertung") Integer bewertung,@Param("bewanz") Integer bewanz,
                         @Param("gesamtbewertung") Integer gesamtbewertung, @Param("pid") Integer pid);

    @Transactional
    @Modifying
    @Query(value = "update parkplatz set aid = :aid where pid = :pid", nativeQuery = true)
    void updateAid(@Param("aid") Integer aid, @Param("pid") Integer pid);

    @Transactional
    @Modifying
    @Query(value = "delete from parkplatz where pid = :pid", nativeQuery = true)
    void deleteParkplatz(@Param("pid") Integer pid);
}
