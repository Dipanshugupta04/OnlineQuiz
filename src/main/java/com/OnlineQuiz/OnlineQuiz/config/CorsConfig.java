package com.OnlineQuiz.OnlineQuiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean("globalCorsConfigurer") 
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                  .allowedOrigins("onlinequizwin.netlify.app"
                // ,"http://quizwiz-frontend.s3-website.ap-south-1.amazonaws.com","http://127.0.0.1:5504",
                // "http://localhost:5504"
                )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}