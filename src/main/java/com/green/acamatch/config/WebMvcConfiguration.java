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
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final String uploadPath;
    private final ExcelProperties excelProperties;

    public WebMvcConfiguration(@Value("${file.directory}") String uploadPath, ExcelProperties excelProperties) {
        this.uploadPath = uploadPath;
        this.excelProperties = excelProperties;
    }

//    @Value("${excel.path}") // 엑셀 저장 경로 (환경 변수에서 가져옴)
//    private String excelPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pic/**").addResourceLocations("file:" + uploadPath + "/");
        registry.addResourceHandler("/xlsx/**").addResourceLocations("file:" + uploadPath + "/");
//        // 추가로 C:\Users\Administrator\Downloads\student_grades 경로 허용
//        registry.addResourceHandler("/xlsx/**")
//                .addResourceLocations("file:" + excelPath + "/");


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ✅ 업로드 파일 경로 허용
        registry.addResourceHandler("/pic/**")
                .addResourceLocations("file:" + uploadPath + "/");

        // ✅ 엑셀 저장 경로 허용
        String excelPath = excelProperties.getPath().replace("\\", "/"); // 경로 변환
        registry.addResourceHandler("/xlsx/**")
                .addResourceLocations("file:" + excelPath + "/");

        // ✅ 정적 리소스 허용 (Spring Boot 기본 static 디렉터리)
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