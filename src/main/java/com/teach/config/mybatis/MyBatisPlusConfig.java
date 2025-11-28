package com.tracker.framework.config.mybatis;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.tracker.framework.domain.pojo.BaseDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Objects;

@Configuration
@MapperScan(
        basePackages = "com.tracker",   // 扫描根包，递归扫描所有子模块下的 Mapper
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
                if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO baseDO) {

                    LocalDateTime current = LocalDateTime.now();
                    // 创建时间为空，则以当前时间为插入时间
                    if (Objects.isNull(baseDO.getCreateTime())) {
                        baseDO.setCreateTime(current);
                    }
                    // 更新时间为空，则以当前时间为更新时间
                    if (Objects.isNull(baseDO.getUpdateTime())) {
                        baseDO.setUpdateTime(current);
                    }

                    Object userId = StpUtil.isLogin() ? StpUtil.getLoginId() : null;

                    // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
                    if (Objects.nonNull(userId) && Objects.isNull(baseDO.getCreatedBy())) {
                        baseDO.setCreatedBy(userId.toString());
                    } else {
                        baseDO.setCreatedBy("");
                    }
                    // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
                    if (Objects.nonNull(userId) && Objects.isNull(baseDO.getUpdatedBy())) {
                        baseDO.setUpdatedBy(userId.toString());
                    } else {
                        baseDO.setUpdatedBy("");
                    }
                }
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                // 更新时间为空，则以当前时间为更新时间
                Object modifyTime = getFieldValByName("updateTime", metaObject);
                if (Objects.isNull(modifyTime)) {
                    setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
                }

                // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
                Object modifier = getFieldValByName("updated_by", metaObject);
                Object userId = StpUtil.isLogin() ? StpUtil.getLoginId() : null;
                if (Objects.nonNull(userId) && Objects.isNull(modifier)) {
                    setFieldValByName("updated_by", userId.toString(), metaObject);
                }
            }
        };
    }
}