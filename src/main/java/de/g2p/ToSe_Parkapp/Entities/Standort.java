package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@NoArgsConstructor
@Entity
public class Standort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ortid;

    private String strasse;

    private Integer plz;

    private Integer hausnummer;

    private String latitude;

    private String longitude;

    public Standort(String strasse, Integer plz, Integer hausnummer, String latitude, String longitude) {
        this.strasse = strasse;
        this.plz = plz;
        this.hausnummer = hausnummer;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getOrtid() {
        return ortid;
    }

    public void setOrtid(Integer ortid) {
        this.ortid = ortid;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public Integer getPlz() {
        return plz;
    }

    public void setPlz(Integer plz) {
        this.plz = plz;
    }

    public Integer getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(Integer hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
