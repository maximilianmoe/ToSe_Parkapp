package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@NoArgsConstructor
@Entity
public class Anbieter  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer aid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nid")
    private Nutzer nid;


    private double umsatz;

    private boolean parkplatz;

    public Anbieter(Integer aid, double umsatz, boolean parkplatz) {
        this.aid = aid;
        this.umsatz = umsatz;
        this.parkplatz = parkplatz;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Nutzer getNid() {
        return nid;
    }

    public void setNid(Nutzer nid) {
        this.nid = nid;
    }

    public double getUmsatz() {
        return umsatz;
    }

    public void setUmsatz(double umsatz) {
        this.umsatz = umsatz;
    }

    public boolean isParkplatz() {
        return parkplatz;
    }

    public void setParkplatz(boolean parkplatz) {
        this.parkplatz = parkplatz;
    }
}
