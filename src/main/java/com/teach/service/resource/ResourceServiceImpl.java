package com.teach.service.resource;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teach.config.mybatis.LambdaQueryWrapperX;
import com.teach.domain.bo.ResourceTypeBo;
import com.teach.domain.dto.resource.ResourceSaveDTO;
import com.teach.domain.vo.resource.PageResourceVO;
import com.teach.domain.vo.resource.ResourceSuggestVO;
import com.teach.domain.vo.resource.ResourceVO;
import com.teach.exception.ServiceException;
import com.teach.models.entity.ResourceDO;
import com.teach.models.enums.ResourceSubSubTypeEnum;
import com.teach.models.enums.ResourceSubTypeEnum;
import com.teach.models.enums.ResourceTypeEnum;
import com.teach.models.mapper.ResourceMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Validated
public class ResourceServiceImpl  extends ServiceImpl<ResourceMapper, ResourceDO> implements ResourceService {

    @Resource
    private ResourceMapper resourceMapper;

    @Override
    public Long createResource(ResourceSaveDTO dto) {
        ResourceDO obj = BeanUtil.toBean(dto, ResourceDO.class);
        obj.setType(dto.getType().getType());
        obj.setSubType(dto.getType().getSubType());
        obj.setSubSubType(dto.getType().getSubSubType());
        if (dto.getId() == null || dto.getId() == 0L) {
            obj.setId(null);
            resourceMapper.insert(obj);
            return obj.getId();
        }
        resourceMapper.insertOrUpdate(obj);
        return obj.getId();
    }

    @Override
    public void updateResource(ResourceSaveDTO dto) {
        validateExists(dto.getId());
        ResourceDO updateObj = BeanUtil.toBean(dto, ResourceDO.class);
        resourceMapper.updateById(updateObj);
    }

    @Override
    public void deleteResource(Long id) {
        validateExists(id);
        resourceMapper.deleteById(id);
    }

    @Override
    public void deleteResourceListByIds(List<Long> ids) {
        resourceMapper.deleteByIds(ids);
    }

    private void validateExists(Long id) {
        if (resourceMapper.selectById(id) == null) {
            throw new ServiceException("资源不存在");
        }
    }

    @Override
    public ResourceVO getResource(Long id) {
        ResourceDO resourceDO = resourceMapper.selectById(id);
        if (resourceDO == null) {
            throw new ServiceException("资源不存在");
        }
        ResourceVO resourceVO = new ResourceVO();
        BeanUtil.copyProperties(resourceDO, resourceVO, "type");
        resourceVO.setType(new ResourceTypeBo(resourceDO.getType(), resourceDO.getSubType(), resourceDO.getSubSubType()));
        return resourceVO;
    }

    @Override
    public PageResourceVO getResourceList(Integer pageNum, Integer pageSize, String type, String subType, String subSubType, String search, String sortBy, String sortOrder) {
        Page<ResourceDO> page = Page.of(pageNum, pageSize);
        LambdaQueryWrapperX<ResourceDO> query = new LambdaQueryWrapperX<>();
        query.eqIfPresent(ResourceDO::getType, type)
                .eqIfPresent(ResourceDO::getSubType, subType)
                .eqIfPresent(ResourceDO::getSubSubType, subSubType)
                .and(StrUtil.isNotBlank(search),  item -> item.like(ResourceDO::getTitle, search)
                        .or()
                        .like(ResourceDO::getDescription, search)
                );
        // 排序
        if (StrUtil.isNotBlank(sortBy)) {
        }
        Page<ResourceDO> resourceDOPage = resourceMapper.selectPage(page, query);
        PageResourceVO.Pagination pagination = new PageResourceVO.Pagination(
                String.valueOf(pageNum),
                String.valueOf(pageSize),
                String.valueOf(resourceDOPage.getTotal())
        );

        // 一级 type 分组
        Map<String, List<ResourceDO>> typeMap = resourceDOPage.getRecords()
                .stream()
                .collect(Collectors.groupingBy(ResourceDO::getType));
        List<PageResourceVO.DataResources> resourcesList = new ArrayList<>();
        for (Map.Entry<String, List<ResourceDO>> typeEntry : typeMap.entrySet()) {
            String t = typeEntry.getKey();
            List<ResourceDO> typeRecords = typeEntry.getValue();
            PageResourceVO.DataResources dataResources = new PageResourceVO.DataResources();
            dataResources.setTitle(ResourceTypeEnum.fromCode(t) != null ?
                    ResourceTypeEnum.fromCode(t).getLabel() : t);
            dataResources.setValue(t);
            dataResources.setIcon(getIconByType(t));
            dataResources.setCount(typeRecords.size());
            dataResources.setDescription(dataResources.getTitle() + "库");

            // 二级 subType 分组
            Map<String, List<ResourceDO>> subTypeMap = typeRecords.stream()
                    .collect(Collectors.groupingBy(ResourceDO::getSubType));
            List<PageResourceVO.Tabs> tabsList = new ArrayList<>();
            for (Map.Entry<String, List<ResourceDO>> subEntry : subTypeMap.entrySet()) {
                String st = subEntry.getKey();
                List<ResourceDO> subRecords = subEntry.getValue();
                PageResourceVO.Tabs tab = new PageResourceVO.Tabs();
                tab.setKey(st);
                tab.setTitle(ResourceSubTypeEnum.fromCode(st) != null ? ResourceSubTypeEnum.fromCode(st).getLabel() : st);

                // 三级 subSubType 分组
                Map<String, List<ResourceDO>> subSubTypeMap = subRecords.stream().collect(Collectors.groupingBy(ResourceDO::getSubSubType));
                List<PageResourceVO.Children> childrenList = new ArrayList<>();
                for (Map.Entry<String, List<ResourceDO>> subSubEntry : subSubTypeMap.entrySet()) {
                    String sst = subSubEntry.getKey();
                    List<ResourceDO> sstRecords = subSubEntry.getValue();

                    PageResourceVO.Children children = new PageResourceVO.Children();
                    children.setKey(sst);
                    children.setTitle(ResourceSubSubTypeEnum.fromCode(sst) != null ? ResourceSubSubTypeEnum.fromCode(sst).getLabel() : sst);
                    List<PageResourceVO.ChildrenResources> childrenResourcesList = sstRecords.stream().map(r -> {
                        PageResourceVO.ChildrenResources cr = new PageResourceVO.ChildrenResources();
                        BeanUtil.copyProperties(r, cr);
                        return cr;
                    }).collect(Collectors.toList());
                    children.setResources(childrenResourcesList);
                    childrenList.add(children);
                }
                tab.setChildren(childrenList);
                tabsList.add(tab);
            }
            dataResources.setTabs(tabsList);
            resourcesList.add(dataResources);
        }
        PageResourceVO vo = new PageResourceVO();
        vo.setPagination(pagination);
        vo.setResources(resourcesList);
        return vo;
    }

    @Override
    @Transactional
    public Integer viewResource(Long id) {
        if (id == null) {
            throw new ServiceException("资源不存在");
        }
        ResourceDO resource = baseMapper.selectById(id);
        if (resource == null) {
            throw new ServiceException("资源不存在");
        }
        baseMapper.update(null, new LambdaUpdateWrapper<ResourceDO>()
                .eq(ResourceDO::getId, id)
                .setSql("view_count = view_count + 1"));
        resource = baseMapper.selectById(id);
        return resource.getViewCount();
    }

    @Override
    public ResourceSuggestVO resourceSuggest(String q, String limit) {
        ResourceSuggestVO vo = new ResourceSuggestVO();
        LambdaQueryWrapperX<ResourceDO> query = new LambdaQueryWrapperX<>();
        query.select(ResourceDO::getId, ResourceDO::getTitle, ResourceDO::getDescription, ResourceDO::getViewCount);
        query.and(StrUtil.isNotBlank(q),  item -> item.like(ResourceDO::getTitle, q)
                        .or()
                        .like(ResourceDO::getDescription, q)
                );
        query.last("limit " + (StrUtil.isBlank(limit) ? 10 : limit));
        List<ResourceDO> resourceDOS = baseMapper.selectList(query);
        List<ResourceSuggestVO.Resources> resources = BeanUtil.copyToList(resourceDOS, ResourceSuggestVO.Resources.class);
        vo.setResources(resources);
        vo.setCount(resourceDOS.size());
        return vo;
    }
    private String getIconByType(String type) {
        return switch (type) {
            case "institutional_norm" -> "mdi--file-document";
            case "research_results" -> "mdi--lightbulb-on";
            case "ideological" -> "mdi--heart-multiple";
            case "practical_method" -> "mdi--tools";
            default -> "";
        };
    }

}
