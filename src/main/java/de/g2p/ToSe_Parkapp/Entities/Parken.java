package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Parken")
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


    private Date startDate;
    private Time startTime;

    private Date endeDate;
    private Time endeTime;

    @Column(name = "oeffentlich")
    private boolean oeffentlich;



    private boolean freigabe;

    public Parken(Konsument kid, Parkplatz pid, Date startDatum, Date endeDatum, Time startZeit, Time endeZeit,
                   boolean freigabe) {
        this.kid = kid;
        this.pid = pid;
        this.startDate = startDatum;
        this.startTime = startZeit;
        this.endeDate = endeDatum;
        this.endeTime= endeZeit;
        this.freigabe = freigabe;
    }

    public Parken(boolean oeffentlich) {
        this.oeffentlich = oeffentlich;
    }

    public boolean getOeffentlich() {
        return oeffentlich;
    }

    public Integer getPidParkplatz() {
        return pid.getPid();
    }
}
