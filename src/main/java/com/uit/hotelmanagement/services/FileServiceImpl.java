package com.uit.hotelmanagement.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    @Value("${project.photos}")
    private String path;
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        if (file != null) {
            UUID uuid = UUID.randomUUID();

            String originalFilename = file.getOriginalFilename();

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String newFilename = uuid + fileExtension;

            String pathFile = path + File.separator + newFilename;

            File f = new File(path);

            if (!f.exists()) {
                f.mkdir();
            }

            Files.copy(file.getInputStream(), Paths.get(pathFile));

            return newFilename;
        }
        return null;
    }

    @Override
    public void deleteFile(String filename) throws IOException {
        Files.deleteIfExists(Paths.get(path + File.separator + filename));
    }

    @Override
    public FileInputStream getResourceFile(String filename) throws FileNotFoundException {
        String pathFile = path + File.separator + filename;
        return new FileInputStream(pathFile);
    }
}
