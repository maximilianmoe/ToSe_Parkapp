package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.awt.print.Pageable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Historie")
public class Historie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hid")
    private Integer historienId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nid")
    private Nutzer nid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pid")
    private Parkplatz pid;

    private String aktion;

    private String info;

    public Historie(Nutzer nid, Parkplatz pid, String aktion, String info) {
        this.nid = nid;
        this.pid = pid;
        this.aktion = aktion;
        this.info = info;
    }

    public String compareBenutzername(List<Nutzer> nutzers) {
        for (Nutzer nutzerFor : nutzers) {
            if (nutzerFor == this.nid) {
                if (nutzerFor.getBenutzername().equals(this.nid.getBenutzername()))
                    return nutzerFor.getBenutzername();
            }
        }
        return null;
    }

    public Integer compareNid(List<Nutzer> nutzers) {
        for (Nutzer nutzerFor : nutzers) {
            if (nutzerFor == this.nid) {
                return nutzerFor.getNid();
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
