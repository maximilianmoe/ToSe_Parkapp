package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="bilder")
public class Bild {

    @Id
    @GeneratedValue
    private int photoId;
    private String path;
    private String fileName;

    @ManyToOne
    @JoinColumn(name="pid")
    private Parkplatz parkplatz;
}
