    package com.moz1mozi.mybatis.image.service;

    import com.moz1mozi.mybatis.image.dao.ImageMapper;
    import com.moz1mozi.mybatis.image.dto.ImageDto;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.StandardCopyOption;
    import java.time.Instant;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;
    import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    public class ImageService {

        private final ImageMapper imageMapper;

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

        @Transactional
        public void  saveOrUpdateFileMetaData(String originalFileName, String storedFileName, Long prodId) {
            List<ImageDto> imageDtos = imageMapper.findByProductId(prodId);
            String storedUrl = "/upload/" + storedFileName;
            Date now = Date.from(Instant.now());
            if(imageDtos.isEmpty()) {
                List<ImageDto> newImageDtos = new ArrayList<>();
                ImageDto imageDto = ImageDto.builder()
                        .originalFileName(originalFileName)
                        .storedFileName(storedFileName)
                        .productId(prodId)
                        .storedUrl(storedUrl)
                        .createdAt(now)
                        .build();
                newImageDtos.add(imageDto);
                imageMapper.insertProductImage(newImageDtos);
            } else {
                List<ImageDto> updateImageDtos = new ArrayList<>();
                for (ImageDto imageDto : imageDtos) {
                    if (!storedUrl.equals(imageDto.getStoredUrl())) {
                        imageDto.setOriginalFileName(originalFileName);
                        imageDto.setStoredFileName(storedFileName);
                        imageDto.setProductId(prodId);
                        imageDto.setStoredUrl(storedUrl);
                        imageDto.setModifiedAt(now);
                        updateImageDtos.add(imageDto);
                    }
                }
                if (!updateImageDtos.isEmpty()) {
                    imageMapper.updateProductImage(updateImageDtos);
                }
            }
        }

        @Transactional
        public void uploadFile(List<MultipartFile> files, Long prodId) throws IOException {
            for (MultipartFile file : files) {

                String storedFileName = storeFile(file); // 파일 저장
                saveOrUpdateFileMetaData(file.getOriginalFilename(), storedFileName, prodId); // 메타데이터 저장
            }
        }
    }
