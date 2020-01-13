package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Konsument")
public class Konsument {

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

    @Column(name = "PRES")
    private boolean reserviert;

    public Konsument(Integer kid, String kennzeichen, String fahrzeugtyp, boolean belegt, boolean reserviert) {
        this.kid = kid;
        this.kennzeichen = kennzeichen;
        this.fahrzeugtyp = fahrzeugtyp;
        this.belegt = belegt;
        this.reserviert = reserviert;
    }

    public Konsument getKidKonsument() {
        return this;
    }

    public boolean getBelegt() {
        return belegt;
    }


}
