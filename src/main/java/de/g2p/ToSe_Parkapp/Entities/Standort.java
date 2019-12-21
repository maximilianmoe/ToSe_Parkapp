package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Standort")
public class Standort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ortid;

    private String strasse;

    private Integer plz;

    private Integer hausnummer;

    private String latitude;

    private String longitude;

    public Standort(Integer ortid, String strasse, Integer plz, Integer hausnummer, String latitude, String longitude) {
        this.ortid = ortid;
        this.strasse = strasse;
        this.plz = plz;
        this.hausnummer = hausnummer;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
