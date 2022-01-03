package com.reddit.redditcloneback.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // header 확장시킴.
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .exposedHeaders("Authorization");
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/userImage/**")
//                .addResourceLocations("file:///Users/juyohan/BookRedFile/User/");
//
//        registry.addResourceHandler("/feedImage/**")
//                .addResourceLocations("file:///Users/juyohan/BookRedFile/Feed/");
//    }
}
