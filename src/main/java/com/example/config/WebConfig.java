package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC Configuration
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final PhotoProperties photoProperties;

    public WebConfig(PhotoProperties photoProperties) {
        this.photoProperties = photoProperties;
    }

    /**
     * Configure resource handlers for serving uploaded photos
     * Maps /images/** URLs to the configured upload directory
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = photoProperties.getUploadDir();
        String resourceLocation = ensureTrailingSlash(uploadDir);

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + resourceLocation);
    }

    /**
     * Ensure the path has a trailing slash for proper resource mapping
     */
    private String ensureTrailingSlash(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }
        return path.endsWith("/") ? path : path + "/";
    }
}