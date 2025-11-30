package com.teach.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.teach.config.swagger.SpringDocAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public GroupedOpenApi systemGroupedOpenApi() {
        return SpringDocAutoConfiguration.buildGroupedOpenApi("教学信息资源库", "com.teach");
    }

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            JavaTimeModule module = new JavaTimeModule();
            module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATETIME_FORMATTER));
            module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATETIME_FORMATTER));
            builder.modules(module);

            // 禁止时间戳
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }

}