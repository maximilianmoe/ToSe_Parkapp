package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Reservierung")
public class Reservierung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kid")
    private Konsument kid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pid")
    private Parkplatz pid;

    private Date start;

    private Date ende;

    //wurde das Parken durch eine Reservierung ausgelöst?
    @Column(name = "REMARK")
    private boolean resZuParken;

    private boolean beendet;

    public Reservierung(Konsument kid, Parkplatz pid, Date start, Date ende, boolean resZuParken, boolean beendet) {
        this.kid = kid;
        this.pid = pid;
        this.start = start;
        this.ende = ende;
        this.resZuParken = resZuParken;
        this.beendet = beendet;
    }


}
