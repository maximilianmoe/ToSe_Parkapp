package de.g2p.ToSe_Parkapp.Controller;

import de.g2p.ToSe_Parkapp.Web.UploadFileResponse;
import de.g2p.ToSe_Parkapp.Entities.Bilder;
import de.g2p.ToSe_Parkapp.Service.DBFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import lombok.Getter;


public class BilderController {


    @RestController
    public class FileController {



        @Autowired
        private DBFileStorageService dbFileStorageService;

        @PostMapping("/parkplatz_hinzufuegen")
        public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
            Bilder dbFile = dbFileStorageService.storeFile(file);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(dbFile.getBid())
                    .toUriString();



            return new UploadFileResponse(dbFile.getFileName(), fileDownloadUri,
                    file.getContentType(), file.getSize());
        }


    }


}
