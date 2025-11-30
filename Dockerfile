# 使用官方 OpenJDK 21 镜像
FROM eclipse-temurin:21-jdk-jammy

# 设置工作目录
WORKDIR /app

# 复制本地 jar 到镜像中
COPY target/teaching-resource-repo-1.0.jar ./app.jar

# 对外暴露端口（Spring Boot 默认 8080）
EXPOSE 8080

# 启动 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "app.jar"]
