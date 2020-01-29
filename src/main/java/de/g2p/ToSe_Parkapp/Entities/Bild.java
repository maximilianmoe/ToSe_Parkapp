package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="Bilder")
public class Bild {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name="photoid")
    private int photoid;

    @Column(name = "path")
    private String path;

    @Column(name= "file_name")
    private String fileName;

    @ManyToOne
    @JoinColumn(name="pid")
    private Parkplatz parkplatz;
}
