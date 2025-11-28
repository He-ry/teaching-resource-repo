package com.teach;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.teach.*"})
public class TeachingResourceRepoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TeachingResourceRepoApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8080");
        String path = env.getProperty("server.servlet.context-path", "");
        log.info("文档地址：{}", "http://localhost:" + port + path + "/doc.html");
    }

}
