package de.g2p.ToSe_Parkapp.Service;

import de.g2p.ToSe_Parkapp.Controller.ParkplatzController;
import de.g2p.ToSe_Parkapp.Entities.Bild;
import de.g2p.ToSe_Parkapp.Entities.Parkplatz;
import de.g2p.ToSe_Parkapp.Repositories.BildRepository;
import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ImageService {


 /*   public static void saveImage(MultipartFile imageFile) throws Exception {



        String folder = "/photos/";
        byte[] bytes = imageFile.getBytes();
        Path path = Paths.get(folder + parkplatz.getPid().toString()+ ".jpg");
        Files.write(path, bytes);
    }*/
}
