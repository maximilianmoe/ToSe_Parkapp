package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Transaktion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kid")
    private Konsument kid;

    @OneToOne(cascade = CascadeType.ALL)
    private Anbieter aid;

    private double betrag;

    private boolean gebuehr;

    private Date datum;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parkid")
    private Parken parkid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pid")
    private Parkplatz pid;

    public Transaktion(Konsument kid, Anbieter aid, double betrag, boolean gebuehr, Date datum, Parken parkid,
                       Parkplatz pid) {
        this.kid = kid;
        this.aid = aid;
        this.betrag = betrag;
        this.gebuehr = gebuehr;
        this.datum = datum;
        this.parkid = parkid;
        this.pid = pid;
    }
}
