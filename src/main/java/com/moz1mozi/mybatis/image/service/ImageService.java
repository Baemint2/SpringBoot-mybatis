package com.moz1mozi.mybatis.image.service;

import com.moz1mozi.mybatis.image.dao.ImageDao;
import com.moz1mozi.mybatis.image.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageDao imageDao;

    @Value("${file.upload-dir.images}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        if(file.isEmpty()) {
            throw new IllegalStateException("업로드된 파일이 없습니다");
        }

        String originalFilename = file.getOriginalFilename();
        String storedFileName = UUID.randomUUID() + "_" + originalFilename;
        Path destinationPath = Paths.get(uploadDir).resolve(storedFileName);

        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        return storedFileName;
    }

    public void saveFileMetaData(String originalFileName, String storedFileName, Long prodId) {
        ImageDto imageDto = ImageDto.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .productId(prodId)
                .storedUrl("/upload/" + storedFileName)
                .created_at(Date.from(Instant.now()))
                .build();
        imageDao.insertProductImage(imageDto);
    }

    public void uploadFile(MultipartFile file, Long prodId) throws IOException {
        String storedFileName = storeFile(file); // 파일 저장
        saveFileMetaData(file.getOriginalFilename(), storedFileName, prodId); // 메타데이터 저장
    }
}
