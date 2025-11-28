package com.teach.models.mapper;

import com.teach.config.mybatis.BaseMapperX;
import com.teach.models.entity.FileDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapperX<FileDO> {
}
