package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Nutzer")
public class Nutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Cascade(CascadeType.DELETE)
    private Integer nid;

    private String vorname;

    private String nachname;

    @Column(name = "emailadresse")
    private String emailAdresse;

    private String benutzername;

    private String passwort;

    private Integer saldo;
    private Integer erinnerung;

    private String admin;

    private boolean sperrung;

    private String rolle;


    public Nutzer(String vorname, String nachname, String emailAdresse, String passwort,
                  String admin, boolean sperrung, Integer saldo, String rolle, String benutzername, int erinnerung) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.emailAdresse = emailAdresse;
        this.passwort = passwort;
        this.admin = admin;
        this.sperrung = sperrung;
        this.saldo = saldo;
        this.rolle = rolle;
        this.benutzername = benutzername;
        this.erinnerung=erinnerung;
    }

    public Nutzer(String vorname, String nachname, String emailAdresse, String passwort, Integer saldo,
                  String rolle, String benutzername) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.emailAdresse = emailAdresse;
        this.passwort = passwort;
        this.saldo = saldo;
        this.rolle = rolle;
        this.benutzername = benutzername;
    }

    public Nutzer getNidNutzer() {
        return this;
    }

    public Integer getNidInteger() {return nid;}

    public boolean getSperrung() {return sperrung;}

    public String getAdmin() {return admin;}

    public String compareBenutzername(List<Nutzer> nutzers) {
        for (Nutzer nutzer : nutzers) {
            if(nutzer.getBenutzername().equals(this.benutzername))
                return nutzer.getBenutzername();
        }
        return null;
    }

    public String compareBenutzername(Nutzer nid, List<Nutzer> nutzers) {
        for (Nutzer nutzerFor : nutzers) {
            if (nutzerFor.getBenutzername().equals(nid.getBenutzername())) {
                return nutzerFor.getBenutzername();
            }
        }
        return null;
    }

    public Integer compareNid(Nutzer nid, List<Nutzer> nutzers) {
        for (Nutzer nutzerFor : nutzers) {
            if (nutzerFor.getNid() == nid.getNid()) {
                   return nutzerFor.getNid();
            }
        }
        return null;
    }


}
