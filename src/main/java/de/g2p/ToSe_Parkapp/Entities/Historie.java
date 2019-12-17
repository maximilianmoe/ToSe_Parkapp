package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    public Historie(Nutzer nid, Parkplatz pid, String aktion) {
        this.nid = nid;
        this.pid = pid;
        this.aktion = aktion;
    }
}
