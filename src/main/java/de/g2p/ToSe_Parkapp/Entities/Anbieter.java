package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
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
}
