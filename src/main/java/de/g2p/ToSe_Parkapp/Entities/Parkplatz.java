package de.g2p.ToSe_Parkapp.Entities;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Parkplatz")
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

    private Integer bewertung;

    @Column(name = "BEWANZ")
    private Integer bewertungsanzahl;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "aid")
    private Anbieter anbieterId;

    private Integer parkgebuehr;

    @Lob
    private byte[] bild;

    private Integer strafgebuehr;

    private String beschreibung;

    private String strasse;

    private Integer plz;

    private Integer hausnummer;

    private String latitude;

    private String longitude;

    public Parkplatz(){

    }


    public Parkplatz(String fahrzeugtyp, String status, boolean privat, Integer zeitbegrenzung,
                     Integer bewertung, Integer bewertungsanzahl, Anbieter anbieterId, @NotNull Integer strafgebuehr,
                     String beschreibung, Integer parkgebuehr, byte[] bild, String strasse, Integer plz, Integer hausnummer, String latitude, String longitude) {
        this.fahrzeugtyp = fahrzeugtyp;
        this.status = status;
        this.privat = privat;
        this.zeitbegrenzung = zeitbegrenzung;
        this.bewertung = bewertung;
        this.bewertungsanzahl = bewertungsanzahl;
        this.anbieterId = anbieterId;
        this.strafgebuehr = strafgebuehr;
        this.beschreibung = beschreibung;
        this.parkgebuehr = parkgebuehr;
        this.bild = bild;
        this.strasse = strasse;
        this.plz = plz;
        this.hausnummer = hausnummer;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Integer getPid() {
        return pid;
    }

    public String getFahrzeugtyp() {
        return fahrzeugtyp;
    }

    public String getStatus() {
        return status;
    }

    public boolean isPrivat() {
        return privat;
    }

    public Integer getZeitbegrenzung() {
        return zeitbegrenzung;
    }

    public Integer getBewertung() {
        return bewertung;
    }

    public Integer getBewertungsanzahl() {
        return bewertungsanzahl;
    }

    public Anbieter getAnbieterId() {
        return anbieterId;
    }

    public Integer getParkgebuehr() {
        return parkgebuehr;
    }

    public byte[] getBild() {
        return bild;
    }

    public Integer getStrafgebuehr() {
        return strafgebuehr;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public String getStrasse() {
        return strasse;
    }

    public Integer getPlz() {
        return plz;
    }

    public Integer getHausnummer() {
        return hausnummer;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
