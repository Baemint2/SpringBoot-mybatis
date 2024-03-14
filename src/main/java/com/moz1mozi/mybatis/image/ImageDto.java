package com.moz1mozi.mybatis.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
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
