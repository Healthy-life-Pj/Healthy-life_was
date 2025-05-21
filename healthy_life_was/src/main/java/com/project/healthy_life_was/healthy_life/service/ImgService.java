package com.project.healthy_life_was.healthy_life.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImgService {
    @Value("${file.upload-dir}")
    private String projectPath;

    public String convertImgFile(MultipartFile file, String subPath) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("Invalid file: file name is missing");
        }

        String newImgName = UUID.randomUUID().toString() + "_" + originalFilename;

        String rootPath = projectPath + "/image/";
        String filePath = subPath + "/" + newImgName;
        File f = new File(rootPath + subPath);

        if (!f.exists() && !f.mkdirs()) {
            throw new RuntimeException("Failed to create directory: " + f.getAbsolutePath());
        }

        Path uploadPath = Paths.get(rootPath + filePath);

        try {
            Files.write(uploadPath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }
        System.out.println("Saving file to: " + uploadPath.toString());
        return filePath;
    }
}
