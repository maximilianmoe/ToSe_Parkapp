package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;


@NoArgsConstructor
@Entity
public class Parken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer parkid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kid")
    private Konsument kid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pid")
    private Parkplatz pid;

    private Date start;

    private Date ende;

    @Column(name = "EIGENBEL")
    private boolean eigenbelegt;

    private Date erinnerung;

    private boolean freigabe;

    public Parken(Konsument kid, Parkplatz pid, Date start, Date ende, boolean eigenbelegt, Date erinnerung,
                  boolean freigabe) {
        this.kid = kid;
        this.pid = pid;
        this.start = start;
        this.ende = ende;
        this.eigenbelegt = eigenbelegt;
        this.erinnerung = erinnerung;
        this.freigabe = freigabe;
    }

    public Integer getParkid() {
        return parkid;
    }

    public void setParkid(Integer parkid) {
        this.parkid = parkid;
    }

    public Konsument getKid() {
        return kid;
    }

    public void setKid(Konsument kid) {
        this.kid = kid;
    }

    public Parkplatz getPid() {
        return pid;
    }

    public void setPid(Parkplatz pid) {
        this.pid = pid;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnde() {
        return ende;
    }

    public void setEnde(Date ende) {
        this.ende = ende;
    }

    public boolean isEigenbelegt() {
        return eigenbelegt;
    }

    public void setEigenbelegt(boolean eigenbelegt) {
        this.eigenbelegt = eigenbelegt;
    }

    public Date getErinnerung() {
        return erinnerung;
    }

    public void setErinnerung(Date erinnerung) {
        this.erinnerung = erinnerung;
    }

    public boolean isFreigabe() {
        return freigabe;
    }

    public void setFreigabe(boolean freigabe) {
        this.freigabe = freigabe;
    }
}
