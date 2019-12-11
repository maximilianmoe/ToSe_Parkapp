package de.g2p.ToSe_Parkapp.Entities;

import de.g2p.ToSe_Parkapp.Fahrzeugtyp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Entity
public class Parkplatz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pid;

    @Column(name = "FTYP")
    private String fahrzeugtyp;

    @Column(name = "PSTAT")
    private String status;

    private boolean privat;

    @Column(name = "ZEITBEGR")
    private Integer zeitbegrenzung;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ortid", referencedColumnName = "ortId")
    private Standort ortid;

    private Integer bewertung;

    @Column(name = "BEWANZ")
    private Integer bewertungsanzahl;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "aid")
    private Anbieter anbieterId;

    @NotNull
    private double strafgebuehr;

    private String beschreibung;

    public Parkplatz(String fahrzeugtyp, String status, boolean privat, Integer zeitbegrenzung, Standort ortid,
                     Integer bewertung, Integer bewertungsanzahl, Anbieter anbieterId, @NotNull double strafgebuehr,
                     String beschreibung) {
        this.fahrzeugtyp = fahrzeugtyp;
        this.status = status;
        this.privat = privat;
        this.zeitbegrenzung = zeitbegrenzung;
        this.ortid = ortid;
        this.bewertung = bewertung;
        this.bewertungsanzahl = bewertungsanzahl;
        this.anbieterId = anbieterId;
        this.strafgebuehr = strafgebuehr;
        this.beschreibung = beschreibung;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getFahrzeugtyp() {
        return fahrzeugtyp;
    }

    public void setFahrzeugtyp(String fahrzeugtyp) {
        this.fahrzeugtyp = fahrzeugtyp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPrivat() {
        return privat;
    }

    public void setPrivat(boolean privat) {
        this.privat = privat;
    }

    public Integer getZeitbegrenzung() {
        return zeitbegrenzung;
    }

    public void setZeitbegrenzung(Integer zeitbegrenzung) {
        this.zeitbegrenzung = zeitbegrenzung;
    }

    public Standort getOrtid() {
        return ortid;
    }

    public void setOrtid(Standort ortid) {
        this.ortid = ortid;
    }

    public Integer getBewertung() {
        return bewertung;
    }

    public void setBewertung(Integer bewertung) {
        this.bewertung = bewertung;
    }

    public Integer getBewertungsanzahl() {
        return bewertungsanzahl;
    }

    public void setBewertungsanzahl(Integer bewertungsanzahl) {
        this.bewertungsanzahl = bewertungsanzahl;
    }

    public Anbieter getAnbieterId() {
        return anbieterId;
    }

    public void setAnbieterId(Anbieter anbieterId) {
        this.anbieterId = anbieterId;
    }

    public double getStrafgebuehr() {
        return strafgebuehr;
    }

    public void setStrafgebuehr(double strafgebuehr) {
        this.strafgebuehr = strafgebuehr;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }
}
