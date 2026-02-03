package com.devIntern.eslite.service;



import com.devIntern.eslite.model.UploadFiles;
import com.devIntern.eslite.payload.UploadFilesDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UploadOneFileService {
    String uploadOneToOne(UploadFilesDTO filesDTO , String Key) throws Exception;

    Optional<UploadFiles> downloadFile(String code) throws Exception;

    String deleteFile(String code);
}
