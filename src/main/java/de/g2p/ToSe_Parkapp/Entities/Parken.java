package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Parken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer parkid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kid")
    private Konsument kid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pid")
    private Parkplatz pid;

    private Date start;

    private Date ende;

    @Column(name = "EIGENBEL")
    private boolean eigenbelegt;

    private Date erinnerung;

    private boolean freigabe;

    public Parken(Konsument kid, Parkplatz pid, Date start, Date ende, boolean eigenbelegt, Date erinnerung,
                  boolean freigabe) {
        this.kid = kid;
        this.pid = pid;
        this.start = start;
        this.ende = ende;
        this.eigenbelegt = eigenbelegt;
        this.erinnerung = erinnerung;
        this.freigabe = freigabe;
    }
}
