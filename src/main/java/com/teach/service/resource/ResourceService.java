package com.teach.service.resource;

import com.baomidou.mybatisplus.extension.service.IService;
import com.teach.domain.dto.resource.ResourceSaveDTO;
import com.teach.domain.vo.resource.PageResourceVO;
import com.teach.domain.vo.resource.ResourceVO;
import com.teach.models.entity.ResourceDO;
import jakarta.validation.Valid;

import java.util.List;

public interface ResourceService extends IService<ResourceDO> {

    Long createResource(@Valid ResourceSaveDTO dto);

    void updateResource(@Valid ResourceSaveDTO dto);

    void deleteResource(Long id);

    void deleteResourceListByIds(List<Long> ids);

    ResourceVO getResource(Long id);

    PageResourceVO getResourceList(Integer page, Integer pageSize, String type, String subType, String subSubType, String search, String sortBy, String sortOrder);

    Integer viewResource(Long id);
}
