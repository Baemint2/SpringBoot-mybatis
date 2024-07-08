package com.moz1mozi.mybatis.image.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ImageDto {
    private long imageId;
    private long prodId;
    private String piOriginalFileName;
    private String piStoredFileName;
    private String piStoredUrl;
    private Date createdAt;
    private Date modifiedAt;
}
