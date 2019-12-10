package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Transaktion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tid;

    private Integer kid;

    private Integer aid;

    private double betrag;

    private boolean gebuehr;

    private Date datum;

    private Integer parkid;

    private Integer pid;

    public Transaktion(Integer kid, Integer aid, double betrag, boolean gebuehr, Date datum, Integer parkid,
                       Integer pid) {
        this.kid = kid;
        this.aid = aid;
        this.betrag = betrag;
        this.gebuehr = gebuehr;
        this.datum = datum;
        this.parkid = parkid;
        this.pid = pid;
    }
}
