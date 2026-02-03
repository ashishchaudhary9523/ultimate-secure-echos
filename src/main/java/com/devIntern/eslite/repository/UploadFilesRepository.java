package com.devIntern.eslite.repository;


import com.devIntern.eslite.model.UploadFiles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface UploadFilesRepository extends JpaRepository<UploadFiles, String> {

}
