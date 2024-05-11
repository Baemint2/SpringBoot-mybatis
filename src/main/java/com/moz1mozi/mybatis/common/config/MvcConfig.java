package com.moz1mozi.mybatis.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///Users/baeyeong-ug/Desktop/image/images/");
        registry.addResourceHandler("/members/**")
                .addResourceLocations("file:///Users/baeyeong-ug/Desktop/image/profiles/");
    }
}
