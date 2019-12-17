package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Nutzer")
public class Nutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer nid;

    private String vorname;

    private String nachname;

    @Column(name = "emailadresse")
    private String emailAdresse;

    private String passwort;

    private boolean admin;

    private boolean sperrung;

    public Nutzer(String vorname, String nachname, String emailAdresse, String passwort,
                  boolean admin, boolean sperrung, Historie ... historie) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.emailAdresse = emailAdresse;
        this.passwort = passwort;
        this.admin = admin;
        this.sperrung = sperrung;
    }
}
