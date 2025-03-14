package com.green.acamatch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
class WebMvcConfiguration implements WebMvcConfigurer {
    private final String uploadPath;
    private final String excelPath;

    public WebMvcConfiguration(@Value("${file.directory}") String uploadPath,
                               @Value("${excel.path}") String excelPath) {
        this.uploadPath = uploadPath;
        this.excelPath = excelPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 이미지 및 일반 파일 업로드 경로 설정
        registry.addResourceHandler("/pic/**")
                .addResourceLocations("file:" + uploadPath + "/");

        // 엑셀 파일 전용 경로 설정 (중복 방지)
        registry.addResourceHandler("/xlsx/**")
                .addResourceLocations("file:" + excelPath + "/");

        // 정적 리소스 핸들링 (React, Vue 같은 프론트엔드 파일 지원)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource resource = location.createRelative(resourcePath);
                        if (resource.exists() && resource.isReadable()) {
                            return resource;
                        }
                        return new ClassPathResource("/static/index.html");
                    }
                });
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:5173",
                        "http://localhost:4173",
                        "http://127.0.0.1:5173",  // 추가
                        "http://127.0.0.1:4173",  // 추가
                        "https://dev.acamtach.site", // 추가 (개발 환경)
                        "https://acamtach.site", // 추가 (운영 환경)
                        "https://www.acamatch.site"// 추가 (운영 환경)
                )
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("api", HandlerTypePredicate.forAnnotation(RestController.class));
    }
}