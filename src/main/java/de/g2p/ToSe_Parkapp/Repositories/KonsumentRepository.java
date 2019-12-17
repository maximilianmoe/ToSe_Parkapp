package de.g2p.ToSe_Parkapp.Repositories;

import de.g2p.ToSe_Parkapp.Entities.Konsument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KonsumentRepository extends JpaRepository<Konsument, Integer> {
}
