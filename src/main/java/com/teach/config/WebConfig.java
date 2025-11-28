package com.teach.config;

import com.teach.config.swagger.SpringDocAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public GroupedOpenApi systemGroupedOpenApi() {
        return SpringDocAutoConfiguration.buildGroupedOpenApi("教学信息资源库", "com.teach");
    }

}