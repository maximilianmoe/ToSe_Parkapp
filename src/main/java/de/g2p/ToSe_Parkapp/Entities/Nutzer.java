package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Nutzer")
public class Nutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Cascade(CascadeType.DELETE)
    private Integer nid;

    private String vorname;

    private String nachname;

    @Column(name = "emailadresse")
    private String emailAdresse;

    private String passwort;

    private Integer saldo;

    private boolean admin;

    private boolean sperrung;

    private String rolle;

    public Nutzer(String vorname, String nachname, String emailAdresse, String passwort,
                  boolean admin, boolean sperrung, Integer saldo, String rolle) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.emailAdresse = emailAdresse;
        this.passwort = passwort;
        this.admin = admin;
        this.sperrung = sperrung;
        this.saldo = saldo;
        this.rolle = rolle;
    }

    public Nutzer(String vorname, String nachname, String emailAdresse, String passwort, Integer saldo, String rolle) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.emailAdresse = emailAdresse;
        this.passwort = passwort;
        this.saldo = saldo;
        this.rolle = rolle;
    }

    public Nutzer getNidNutzer() {
        return this;
    }

    public Integer getNidInteger() {return nid;}

    public boolean getSperrung() {return sperrung;}

}
