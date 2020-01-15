package de.g2p.ToSe_Parkapp.Service;

import de.g2p.ToSe_Parkapp.Entities.Bilder;
import de.g2p.ToSe_Parkapp.Exception.FileStorageException;
import de.g2p.ToSe_Parkapp.Exception.MyFileNotFoundException;
import de.g2p.ToSe_Parkapp.Repositories.BilderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class DBFileStorageService {

    @Autowired
    private BilderRepository dbFileRepository;

    public Bilder storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Bilder dbFile = new Bilder(fileName, file.getContentType(), file.getBytes());

            return dbFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Bilder getFile(String fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }
}
