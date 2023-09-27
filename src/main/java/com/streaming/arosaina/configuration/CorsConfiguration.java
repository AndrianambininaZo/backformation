package com.streaming.arosaina.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {
    private static final String GET="GET";
    private static final String POST="POST";
    private static final String PUT="DELETE";
    private static final String DELETE="DELETE";
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods(GET,PUT,POST,DELETE)
                        .allowedHeaders("*")
                        .allowedOriginPatterns("*")
                        .allowedOrigins("http://localhost:4200")
                        .allowCredentials(true);

            }
        };
    }
}