package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@NoArgsConstructor
@Entity
public class Reservierung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kid")
    private Konsument kid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pid")
    private Parkplatz pid;

    private Date start;

    private Date ende;

    //wurde das Parken durch eine Reservierung ausgelöst?
    @Column(name = "REMARK")
    private boolean resZuParken;

    private boolean beendet;

    public Reservierung(Konsument kid, Parkplatz pid, Date start, Date ende, boolean resZuParken, boolean beendet) {
        this.kid = kid;
        this.pid = pid;
        this.start = start;
        this.ende = ende;
        this.resZuParken = resZuParken;
        this.beendet = beendet;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
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

    public boolean isResZuParken() {
        return resZuParken;
    }

    public void setResZuParken(boolean resZuParken) {
        this.resZuParken = resZuParken;
    }

    public boolean isBeendet() {
        return beendet;
    }

    public void setBeendet(boolean beendet) {
        this.beendet = beendet;
    }
}
