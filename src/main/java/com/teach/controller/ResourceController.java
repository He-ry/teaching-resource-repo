package com.teach.controller;

import com.teach.domain.pojo.Result;
import com.teach.domain.dto.resource.ResourceSaveDTO;
import com.teach.domain.vo.resource.PageResourceVO;
import com.teach.domain.vo.resource.ResourceVO;
import com.teach.models.entity.ResourceDO;
import com.teach.service.resource.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "资源管理")
@RestController
@RequestMapping("/api/v1/resources")
@Validated
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    @GetMapping()
    @Operation(summary = "获取资源列表")
    public Result<PageResourceVO> getResourceList(@RequestParam(required = false, defaultValue = "1", name = "page") Integer page,
                                                  @RequestParam(required = false, defaultValue = "20", name = "per_page") Integer pageSize,
                                                  @RequestParam(required = false) String type,
                                                  @RequestParam(required = false) String subType,
                                                  @RequestParam(required = false) String subSubType,
                                                  @RequestParam(required = false) String search,
                                                  @RequestParam(required = false, defaultValue = "created_at", name = "sort_by") String sortBy,
                                                  @RequestParam(required = false, defaultValue = "desc", name = "sort_order") String sortOrder) {
        PageResourceVO res = resourceService.getResourceList(page, pageSize, type, subType, subSubType, search, sortBy, sortOrder);
        return Result.success(res);
    }

    @PostMapping()
    @Operation(summary = "创建资源")
    public Result<Long> createResource(@Valid @RequestBody ResourceSaveDTO dto) {
        return Result.success(resourceService.createResource(dto));
    }

    @DeleteMapping("/{resource_id}")
    @Operation(summary = "删除资源")
    @Parameter(name = "id", description = "资源ID", required = true)
    public Result<Boolean> deleteResource(@PathVariable(name = "resource_id") Long id) {
        resourceService.deleteResource(id);
        return Result.success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除资源")
    public Result<Boolean> deleteResourceList(@RequestParam("ids") List<Long> ids) {
        resourceService.deleteResourceListByIds(ids);
        return Result.success(true);
    }

    @GetMapping("/{resource_id}")
    @Operation(summary = "获取资源详情")
    public Result<ResourceVO> getResource(@PathVariable("resource_id") Long id) {
        return Result.success(resourceService.getResource(id));
    }

    @GetMapping("/{resource_id}/view")
    @Operation(summary = "增加浏览量")
    public Result<Integer> viewResource(@PathVariable("resource_id") Long id) {
        Integer res = resourceService.viewResource(id);
        return Result.success(res);
    }

}
