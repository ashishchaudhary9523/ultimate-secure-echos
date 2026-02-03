package com.devIntern.eslite.service.implementation;


import com.devIntern.eslite.AESUtil.AESUtil;
import com.devIntern.eslite.AESUtil.AESUtilFile;
import com.devIntern.eslite.Exceptions.SecureEchoAPIException;
import com.devIntern.eslite.model.Customer;
import com.devIntern.eslite.model.UploadFiles;
import com.devIntern.eslite.payload.UploadFilesDTO;
import com.devIntern.eslite.repository.CustomerRepository;
import com.devIntern.eslite.repository.UploadFilesRepository;
import com.devIntern.eslite.service.UploadOneFileService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UploadOneFileServiceImplementation implements UploadOneFileService {
    @Value("${max.size}")
    private int MAX_SIZE;

    @Autowired
    private UploadFilesRepository shareOneFileRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private long MAX_FILE_SIZE ;

    public UploadOneFileServiceImplementation(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        MAX_FILE_SIZE = (long) MAX_SIZE * 1024 * 1024;
    }

    @Override
    public String uploadOneToOne(UploadFilesDTO fileDTO , String key) throws Exception {
        if(fileDTO.getFileSize() > MAX_FILE_SIZE){
            return "The file is too large. Upload a file within " + MAX_SIZE + "MB";
        }
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        String code = currentUserName;
        if(shareOneFileRepository.existsById(code)){
           return "unable to upload new file. Only one file per user is allowed";
        }
        Customer customer = customerRepository.findById(currentUserName)
                .orElseThrow(() -> new SecureEchoAPIException(HttpStatus.NOT_FOUND, "User not found"));
        if (!customer.isVerified()) {
            return "This user is not verified";
        }

        String keyHash = passwordEncoder.encode(key);
        UploadFiles shareOneFile = new UploadFiles();
        shareOneFile.setFileId(code);
        shareOneFile.setFileName(fileDTO.getFileName());
        shareOneFile.setFileType(fileDTO.getFileType());
        shareOneFile.setFileSize(fileDTO.getFileSize());
        byte[] encrypted = AESUtilFile.encrypt(fileDTO.getFileContent(), key);
        shareOneFile.setFileContent(encrypted);
        shareOneFile.setKey(keyHash);
        shareOneFile.setIsUpdated(true);
        shareOneFile.setCustomer(customer);
        shareOneFileRepository.save(shareOneFile);
        return code;
    }


    @Override
    public Optional<UploadFiles> downloadFile(String key) throws Exception {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        String code = currentUserName;
        System.out.println(currentUserName);
        if(!shareOneFileRepository.existsById(currentUserName)){
            return Optional.empty();
        }
        Customer customer = customerRepository.findById(currentUserName)
                .orElseThrow(() -> new SecureEchoAPIException(HttpStatus.NOT_FOUND, "User not found"));
        if (!customer.isVerified()) {
            return Optional.empty();
        }
        Optional<UploadFiles> file = shareOneFileRepository.findById(code);
        UploadFiles fileEntity = file.get();

        if (passwordEncoder.matches(key , fileEntity.getKey())) {

            byte[] plain = AESUtilFile.decrypt(fileEntity.getFileContent(), key);
            fileEntity.setFileContent(plain);

            return Optional.of(fileEntity);
        }
        return Optional.empty();
    }

    @Override
    public String deleteFile(String code) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!shareOneFileRepository.existsById(currentUserName)){
            return "No file found for this user";
        }
        Customer customer = customerRepository.findById(currentUserName)
                .orElseThrow(() -> new SecureEchoAPIException(HttpStatus.NOT_FOUND, "User not found"));
        if (!customer.isVerified()) {
            return "This user is not verified";
        }
        Optional<UploadFiles> file = shareOneFileRepository.findById(currentUserName);
        UploadFiles fileEntity = file.get();
        if (passwordEncoder.matches(code , fileEntity.getKey())) {
            shareOneFileRepository.deleteById(currentUserName);
            return "The file has been deleted";
        }
        return "Unable to delete the file";
    }

}
