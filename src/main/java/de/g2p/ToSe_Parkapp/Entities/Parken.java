package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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


    private Date startDatum;

    private Date endeDatum;

    @Column(name = "oeffentlich")
    private boolean oeffentlich;

    private Date erinnerungDatum;
    private Time erinnerungZeit;


    private boolean freigabe;

    public Parken(Konsument kid, Parkplatz pid, Date startDatum, Date endeDatum,
                  Date erinnerungDatum,Time erinnerungZeit, boolean freigabe) {
        this.kid = kid;
        this.pid = pid;
        this.startDatum = startDatum;
        this.endeDatum = endeDatum;
        this.erinnerungDatum = erinnerungDatum;
        this.erinnerungZeit = erinnerungZeit;
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
