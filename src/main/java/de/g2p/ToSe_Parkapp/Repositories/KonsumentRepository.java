package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Konsument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface KonsumentRepository extends JpaRepository<Konsument, Integer> {

    Konsument findByKid(Integer kid);

    @Transactional
    @Modifying
    @Query(value = "update konsument set saldo = :saldo where kid = 4", nativeQuery = true)
    void update(@Param("saldo") double saldo);
}
