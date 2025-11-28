package com.teach.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemWebConfig {

    @Bean
    public GroupedOpenApi systemGroupedOpenApi() {
        return SpringDocAutoConfiguration.buildGroupedOpenApi("系统管理", "com.tracker.system");
    }
}