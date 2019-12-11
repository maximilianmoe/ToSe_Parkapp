package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;


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

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Konsument getKid() {
        return kid;
    }

    public void setKid(Konsument kid) {
        this.kid = kid;
    }

    public Anbieter getAid() {
        return aid;
    }

    public void setAid(Anbieter aid) {
        this.aid = aid;
    }

    public double getBetrag() {
        return betrag;
    }

    public void setBetrag(double betrag) {
        this.betrag = betrag;
    }

    public boolean isGebuehr() {
        return gebuehr;
    }

    public void setGebuehr(boolean gebuehr) {
        this.gebuehr = gebuehr;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Parken getParkid() {
        return parkid;
    }

    public void setParkid(Parken parkid) {
        this.parkid = parkid;
    }

    public Parkplatz getPid() {
        return pid;
    }

    public void setPid(Parkplatz pid) {
        this.pid = pid;
    }
}
