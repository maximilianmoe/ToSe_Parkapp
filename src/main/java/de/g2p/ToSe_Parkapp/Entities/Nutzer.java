package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@NoArgsConstructor
@Entity
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

    public Integer getNid() {
        return nid;
    }

    public void setNid(Integer nid) {
        this.nid = nid;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isSperrung() {
        return sperrung;
    }

    public void setSperrung(boolean sperrung) {
        this.sperrung = sperrung;
    }
}
