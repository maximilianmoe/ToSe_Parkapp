package de.g2p.ToSe_Parkapp.Service;

import de.g2p.ToSe_Parkapp.Entities.Bild;
import de.g2p.ToSe_Parkapp.Repositories.BildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageService {

    @Autowired
    private BildRepository bildRepository;


    public static void saveImage(MultipartFile imageFile) throws Exception {

        String folder = "/photos/";
        byte[] bytes = imageFile.getBytes();
        Path path = Paths.get(folder + imageFile.getOriginalFilename());
        Files.write(path, bytes);

    }

    /*public void save(Bild bild){
        bildRepository.save(bild);
    }*/
}
