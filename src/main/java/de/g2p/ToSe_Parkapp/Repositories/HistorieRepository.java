package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Historie;
import de.g2p.ToSe_Parkapp.Entities.Nutzer;
import de.g2p.ToSe_Parkapp.Entities.Parkplatz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface HistorieRepository extends PagingAndSortingRepository<Historie, Integer> {

    List<Historie> findAll();
    Historie findByHistorienId(Integer hid);
    List<Historie> findByPid(Parkplatz pid);
    List<Historie> findByNid(Nutzer nid);

    @Transactional
    @Modifying
    @Query(value = "update historie set pid = :pid where hid = :hid", nativeQuery = true)
    void updatePid(@Param("pid") Integer pid, @Param("hid") Integer hid);

    @Transactional
    @Modifying
    @Query(value = "update historie set nid = :nid where hid = :hid", nativeQuery = true)
    void updateNid(@Param("nid") Integer nid, @Param("hid") Integer hid);


}
