package com.uit.hotelmanagement.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileService {
    String uploadFile(MultipartFile file) throws IOException;
    void deleteFile(String filename) throws IOException;
    FileInputStream getResourceFile(String filename) throws FileNotFoundException;
}
