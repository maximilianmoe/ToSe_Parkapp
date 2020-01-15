package de.g2p.ToSe_Parkapp.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Bilder")
public class Bilder {

    @Id
    @GeneratedValue(generator = "bid")
    @GenericGenerator(name = "bid", strategy = "uuid2")
    private String bid;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;


    public Bilder(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }
}
