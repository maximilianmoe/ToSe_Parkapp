package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Transaktion")
public class Transaktion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kid")
    private Konsument kid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "aid")
    private Anbieter aid;

    private Integer betrag;

    private boolean gebuehr;

    private Date datum;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parkid")
    private Parken parkid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pid")
    private Parkplatz pid;

    private boolean abgeschlossen;

    public Transaktion(Konsument kid, Anbieter aid, Integer betrag, boolean gebuehr, Date datum, Parken parkid,
                       Parkplatz pid, boolean abgeschlossen) {
        this.kid = kid;
        this.aid = aid;
        this.betrag = betrag;
        this.gebuehr = gebuehr;
        this.datum = datum;
        this.parkid = parkid;
        this.pid = pid;
        this.abgeschlossen = abgeschlossen;
    }

    public Integer compareKid(List<Konsument> konsumenten) {
        for (Konsument konsumentFor : konsumenten) {
            if (konsumentFor == this.kid) {
                return konsumentFor.getKid();
            }
        }
        return null;
    }

    public Integer comparePid(List<Parkplatz> parkplaetze) {
        for (Parkplatz parkplatzFor : parkplaetze) {
            if (parkplatzFor == this.pid) {
                return parkplatzFor.getPid();
            }
        }
        return null;
    }
}
