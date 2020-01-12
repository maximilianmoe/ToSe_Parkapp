package de.g2p.ToSe_Parkapp.Service;


import de.g2p.ToSe_Parkapp.Entities.Anbieter;
import de.g2p.ToSe_Parkapp.Entities.Parkplatz;
import de.g2p.ToSe_Parkapp.Exception.FileStorageException;
import de.g2p.ToSe_Parkapp.Exception.MyFileNotFoundException;
import de.g2p.ToSe_Parkapp.Repositories.DBFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;

https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/

@Service
public class DBFileStorageService {

    @Autowired
    private DBFileRepository dbFileRepository;

    public Parkplatz storeFile(MultipartFile file) {
        // Normalize file name
        String strasse = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(strasse.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " );
            }

            Parkplatz parkplatz = new Parkplatz( file.getFahrzeugtyp() );

            return dbFileRepository.save(parkplatz);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file. Please try again!", ex);
        }
    }

    public Parkplatz getFile(String fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }
}
