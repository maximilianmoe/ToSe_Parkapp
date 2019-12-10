package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Anbieter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nid;

    private Integer aid;

    private double umsatz;

    private boolean parkplatz;

    public Anbieter(Integer aid, double umsatz, boolean parkplatz) {
        this.aid = aid;
        this.umsatz = umsatz;
        this.parkplatz = parkplatz;
    }
}
