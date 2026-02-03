package com.devIntern.eslite.controller;


import com.devIntern.eslite.model.UploadFiles;
import com.devIntern.eslite.payload.UploadFilesDTO;
import com.devIntern.eslite.service.UploadOneFileService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/vault/file")
public class UploadFilesController {

//    @Autowired
//    private UploadOneFileService shareMultipleFilesService;
    @Autowired
    private UploadOneFileService shareOneFileService;

    @PostMapping("/upload/file")
    public ResponseEntity<?> uploadOneToOneFile(@RequestParam("file") MultipartFile []file , @RequestParam("key") @NotEmpty
    @Size(min = 16 , max = 16 , message = "The key must be of 16 chars") String key ) {
        try {
            if(file.length > 1) {
                return new ResponseEntity<>("Cannot upload more than one file." ,HttpStatus.BAD_REQUEST);
            }
            if(file.length == 1){
                UploadFilesDTO fileDTO = new UploadFilesDTO();
                fileDTO.setFileName(file[0].getOriginalFilename());
                fileDTO.setFileSize(file[0].getSize());
                fileDTO.setFileType(file[0].getContentType());
                fileDTO.setFileContent(file[0].getBytes());
                String code = shareOneFileService.uploadOneToOne(fileDTO , key);
                return new ResponseEntity<>(code, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Server Error, Unable to upload file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Server Error, Unable to upload file.", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/get-file")
    public ResponseEntity<?> getFiles(
            @RequestParam("key") @NotEmpty
            @Size(min = 16, max = 16) String key) {

        Optional<UploadFiles> optionalFile;
        try {
            optionalFile = shareOneFileService.downloadFile(key);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server Error, Unable to download file.");
        }

        if (optionalFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File Not Found.");
        }

        UploadFiles file = optionalFile.get();

//        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
//        if (file.getFileType() != null) {
//            mediaType = MediaType.parseMediaType(file.getFileType());
//        }
//
//        return ResponseEntity.ok()
//                .contentType(mediaType)
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=\"" + file.getFileName() + "\"")
//                .contentLength(file.getFileSize())
//                .body(file.getFileContent());

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (file.getFileType() != null && !file.getFileType().isBlank()) {
            mediaType = MediaType.parseMediaType(file.getFileType());
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .contentLength(file.getFileSize())
                .body(file.getFileContent());

    }



//    @GetMapping("/get-file")
//    public ResponseEntity<?> getFiles( @RequestParam("key") @NotEmpty
//                                           @Size(min = 16 , max = 16 , message = "The key must be of 16 chars") String key){
//        Optional<UploadFiles> optionalfile = null;
//        try {
//            optionalfile = shareOneFileService.downloadFile(key);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Server Error, Unable to download file.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        if(optionalfile.isEmpty()){
//            return new ResponseEntity<>("File Not Found.", HttpStatus.NOT_FOUND);
//        }
//        UploadFiles file = optionalfile.get();
//        try {
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION ,
//                            "attachment; filename=\"" + file.getFileName() + "\"")
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .contentLength(file.getFileSize())
//                    .body(file.getFileContent());
//        } catch (Exception e) {
//            return new ResponseEntity<>("Server Error, Unable to download file.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFiles( @RequestParam("key") @NotEmpty
                                              @Size(min = 16 , max = 16 , message = "The key must be of 16 chars") String key){
        try {
            String message = shareOneFileService.deleteFile(key);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Server Error, Unable to delete file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
