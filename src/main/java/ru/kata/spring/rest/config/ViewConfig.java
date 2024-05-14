package ru.kata.spring.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ViewConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("auth/login");
        registry.addViewController("/login").setViewName("auth/login");
        registry.addViewController("/user").setViewName("user");
        registry.addViewController("/admin").setViewName("/admin/admin_panel");
    }
}
