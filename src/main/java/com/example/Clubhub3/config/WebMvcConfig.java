package com.example.Clubhub3.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Add any view controllers if needed
        // registry.addViewController("/uniadmin").setViewName("redirect:/uniadmin/clubs");
    }
}


