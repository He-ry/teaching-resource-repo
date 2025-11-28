package com.teach.config.mybatis;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Objects;

@Configuration
@MapperScan(
        basePackages = "com.teach",   // 扫描根包，递归扫描所有子模块下的 Mapper
        annotationClass = Mapper.class, // 只扫描加了 @Mapper 的接口
        lazyInitialization = "true"
)
public class MyBatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 自动填充字段
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                if (Objects.nonNull(metaObject)) {
                    Object createdAt = getFieldValByName("createdAt", metaObject);
                    if (Objects.isNull(createdAt)) {
                        setFieldValByName("createdAt", LocalDateTime.now(), metaObject);
                    }
                    Object modifier = getFieldValByName("createdBy", metaObject);
                    Object userId = StpUtil.isLogin() ? StpUtil.getLoginId() : null;
                    if (Objects.nonNull(userId) && Objects.isNull(modifier)) {
                        setFieldValByName("createdBy", userId.toString(), metaObject);
                    }
                }
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                // 更新时间为空，则以当前时间为更新时间
                Object modifyTime = getFieldValByName("updatedAt", metaObject);
                if (Objects.isNull(modifyTime)) {
                    setFieldValByName("updatedAt", LocalDateTime.now(), metaObject);
                }

                // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
                Object modifier = getFieldValByName("updatedBy", metaObject);
                Object userId = StpUtil.isLogin() ? StpUtil.getLoginId() : null;
                if (Objects.nonNull(userId) && Objects.isNull(modifier)) {
                    setFieldValByName("updatedBy", userId.toString(), metaObject);
                }
            }
        };
    }
}