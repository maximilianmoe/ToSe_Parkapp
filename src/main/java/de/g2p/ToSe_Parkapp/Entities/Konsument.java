package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;


@NoArgsConstructor
@Entity
public class Konsument  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nid")
    private Nutzer nid;

    private String kennzeichen;

    @Column(name = "FTYP")
    private String fahrzeugtyp;

    @Column(name = "PBEL")
    private boolean belegt;

    private double saldo;

    @Column(name = "PRES")
    private boolean reserviert;

    public Konsument(Integer kid, String kennzeichen, String fahrzeugtyp, boolean belegt, double saldo, boolean reserviert) {
        this.kid = kid;
        this.kennzeichen = kennzeichen;
        this.fahrzeugtyp = fahrzeugtyp;
        this.belegt = belegt;
        this.saldo = saldo;
        this.reserviert = reserviert;
    }

    public Integer getKid() {
        return kid;
    }

    public void setKid(Integer kid) {
        this.kid = kid;
    }

    public Nutzer getNid() {
        return nid;
    }

    public void setNid(Nutzer nid) {
        this.nid = nid;
    }

    public String getKennzeichen() {
        return kennzeichen;
    }

    public void setKennzeichen(String kennzeichen) {
        this.kennzeichen = kennzeichen;
    }

    public String getFahrzeugtyp() {
        return fahrzeugtyp;
    }

    public void setFahrzeugtyp(String fahrzeugtyp) {
        this.fahrzeugtyp = fahrzeugtyp;
    }

    public boolean isBelegt() {
        return belegt;
    }

    public void setBelegt(boolean belegt) {
        this.belegt = belegt;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public boolean isReserviert() {
        return reserviert;
    }

    public void setReserviert(boolean reserviert) {
        this.reserviert = reserviert;
    }
}
