package com.devIntern.eslite.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class UploadFiles {
    @Id
    @Column(name = "file_id" , unique = true, nullable = false)
    private String fileId;
    @Column(name = "file_name" , nullable = false)
    private String fileName;
    @Column(name = "file_size")
    private long fileSize;
    @Column(name = "file_type" , nullable = false)
    private String fileType;
    @Lob
    @Column(name = "file_content" , nullable = false)
    private byte[] fileContent;

    private String key;
    private Boolean isUpdated = false;

    @OneToOne
    @JoinColumn(name = "user_name")
    private Customer customer;
}
