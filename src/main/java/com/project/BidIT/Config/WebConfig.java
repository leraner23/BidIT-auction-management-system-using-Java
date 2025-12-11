package com.project.BidIT.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.users}")
    private String uploadUserDir;
    @Value("${file.upload.admins}")
    private String uploadAdminDir;
    @Value("${file.upload.item.images}")
    private String uploadItemDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){

        registry.addResourceHandler("/users/**")
                .addResourceLocations("file:"+ uploadUserDir+ "/");


        registry.addResourceHandler("/admins/**")
                .addResourceLocations("file:"+ uploadAdminDir+ "/");

        registry.addResourceHandler("/items/**")
                .addResourceLocations("file:"+ uploadItemDir+ "/");
    }

}
