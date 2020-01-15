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


    private Date start;

    private Date ende;



    @Column(name = "oeffentlich")
    private boolean oeffentlich;

    private Date erinnerung;

    private boolean freigabe;

    public Parken(Konsument kid, Parkplatz pid, Date start, Date ende, boolean oeffentlich,
                  Date erinnerung, boolean freigabe) {
        this.kid = kid;
        this.pid = pid;
        this.start = start;
        this.ende = ende;
        this.oeffentlich = oeffentlich;
        this.erinnerung = erinnerung;
        this.freigabe = freigabe;
    }

    public boolean getOeffentlich() {
        return oeffentlich;
    }
}
