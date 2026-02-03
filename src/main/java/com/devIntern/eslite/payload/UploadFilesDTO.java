package com.devIntern.eslite.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFilesDTO {
    private String fileId;
    @NotEmpty
    private String fileName;
    @NotEmpty
    private long fileSize;
    @NotEmpty
    private String fileType;
    @NotEmpty
    private byte[] fileContent;

    @NotEmpty
    @Size(min = 16 , max = 16 , message = "The key must be of 16 chars")
    private String key;

}
