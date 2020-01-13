package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Anbieter;
import de.g2p.ToSe_Parkapp.Entities.Parkplatz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ParkplatzRepository extends JpaRepository<Parkplatz, Integer> {

    Parkplatz findByPid(Integer pid);
    Parkplatz findByAnbieterId(Anbieter aid);

    @Transactional
    @Modifying
    @Query(value = "update parkplatz set pstat = :pstat where pid = :pid", nativeQuery = true)
    void updateStatus(@Param("pstat") String pstat, @Param("pid") Integer pid);

}
