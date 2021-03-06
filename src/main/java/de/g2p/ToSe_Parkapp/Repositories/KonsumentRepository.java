package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Konsument;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;

@Repository
public interface KonsumentRepository extends JpaRepository<Konsument, Integer> {

    Konsument findByKid(Integer kid);
    Konsument findByNid(Nutzer nid);
    @Transactional
    @Modifying
    @Query(value = "update konsument set pbel = :pbel where kid = :kid", nativeQuery = true)
    void updatebelegt(@Param("pbel") boolean pbel, @Param("kid") Integer kid);

    @Transactional
    @Modifying
    @Query(value = "update konsument set pres = :pres where kid = :kid", nativeQuery = true)
    void updateReserviert(@Param("pres") boolean pres, @Param("kid") Integer kid);


//    Integer findByErrinerungszeit(Time reminder);


}
