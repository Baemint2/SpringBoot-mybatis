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
    private long productId;
    private String originalFileName;
    private String storedFileName;
    private String storedUrl;
    private Date created_at;
    private Date modified_at;
}
