package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

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

    private Date startDatum;

    private Date endeDatum;

    private Date erinnerungDatum;

    private Time startTime;

    private Time endTime;

    private Time erinnerungZeit;

    //wurde das Parken durch eine Reservierung ausgelöst?
    @Column(name = "REMARK")
    private boolean resZuParken;

    private boolean beendet;

    public Reservierung(Konsument kid, Parkplatz pid, Date startDatum,Date erinnerungDatum, Date endeDatum,Time erinnerungZeit, Time startTime, Time endTime, boolean resZuParken, boolean beendet) {
        this.kid = kid;
        this.pid = pid;
        this.startDatum = startDatum;
        this.endeDatum = endeDatum;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resZuParken = resZuParken;
        this.beendet = beendet;
        this.erinnerungDatum=erinnerungDatum;
        this.erinnerungZeit=erinnerungZeit;
    }

    public Integer getPidInteger() {
        return pid.getPid();
    }

    public Integer compareWithKonsument(List<Konsument> konsumenten) {
        for (Konsument konsument : konsumenten) {
            if(konsument == this.kid)
                return konsument.getKid();
        }
        return null;
    }


}
