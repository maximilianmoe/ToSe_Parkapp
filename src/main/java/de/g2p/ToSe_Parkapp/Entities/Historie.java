package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@NoArgsConstructor
@Entity
public class Historie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hid")
    private Integer historienId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nid")
    private Nutzer nid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pid")
    private Parkplatz pid;

    private String aktion;

    public Historie(Nutzer nid, Parkplatz pid, String aktion) {
        this.nid = nid;
        this.pid = pid;
        this.aktion = aktion;
    }

    public Integer getHistorienId() {
        return historienId;
    }

    public void setHistorienId(Integer historienId) {
        this.historienId = historienId;
    }

    public Nutzer getNid() {
        return nid;
    }

    public void setNid(Nutzer nid) {
        this.nid = nid;
    }

    public Parkplatz getPid() {
        return pid;
    }

    public void setPid(Parkplatz pid) {
        this.pid = pid;
    }

    public String getAktion() {
        return aktion;
    }

    public void setAktion(String aktion) {
        this.aktion = aktion;
    }
}
