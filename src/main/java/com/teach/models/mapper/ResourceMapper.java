package com.teach.models.mapper;

import com.teach.config.mybatis.BaseMapperX;
import com.teach.models.entity.ResourceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResourceMapper extends BaseMapperX<ResourceDO> {
}
