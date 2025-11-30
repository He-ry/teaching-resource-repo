package com.teach.service.file;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.db.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teach.domain.dto.file.FileSaveDTO;
import com.teach.domain.pojo.PageResult;
import com.teach.domain.pojo.SortablePageParam;
import com.teach.domain.vo.file.FileVO;
import com.teach.domain.vo.file.ImageFileUploadVO;
import com.teach.domain.vo.file.VideoFileUploadVO;
import com.teach.exception.ServiceException;
import com.teach.models.entity.FileDO;
import com.teach.models.mapper.FileMapper;
import com.teach.utils.MinioUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Validated
public class FileServiceImpl extends ServiceImpl<FileMapper, FileDO> implements FileService {

    @Resource
    private FileMapper fileMapper;

    @Resource
    private MinioUtil minioUtil;

    @Override
    public ImageFileUploadVO createFile(MultipartFile file) {
        MinioUtil.FileInfo fileInfo = minioUtil.uploadFile(file);
        FileDO build = FileDO.builder()
                .name(fileInfo.getName())
                .size(fileInfo.getSize())
                .duration("0")
                .url(fileInfo.getUrl())
                .suffix(fileInfo.getSuffix())
                .mimeType(fileInfo.getContentType())
                .kind("image")
                .build();
        fileMapper.insert(build);
        return ImageFileUploadVO.builder()
                .alt(FileNameUtil.mainName(fileInfo.getName()))
                .href(fileInfo.getUrl())
                .url(fileInfo.getUrl())
                .build();
    }

    @Override
    public VideoFileUploadVO createVideoFile(MultipartFile multipartFile) {
        MinioUtil.FileInfo fileInfo = minioUtil.uploadFile(multipartFile);
        FileDO build = FileDO.builder()
                .name(fileInfo.getName())
                .size(fileInfo.getSize())
                .duration("0")
                .url(fileInfo.getUrl())
                .suffix(fileInfo.getSuffix())
                .kind("video")
                .build();
        fileMapper.insert(build);
        return VideoFileUploadVO.builder()
                .poster(FileNameUtil.mainName(fileInfo.getName()))
                .url(fileInfo.getUrl())
                .build();
    }

    @Override
    public void updateFile(FileSaveDTO dto) {
        validateExists(dto.getId());
        FileDO updateObj = BeanUtil.toBean(dto, FileDO.class);
        fileMapper.updateById(updateObj);
    }

    @Override
    public void deleteFile(String  id) {
        validateExists(id);
        fileMapper.deleteById(id);
    }

    @Override
    public void deleteFileListByIds(List<Long> ids) {
        fileMapper.deleteByIds(ids);
    }

    private void validateExists(String id) {
        if (fileMapper.selectById(id) == null) {
            throw new ServiceException("文件不存在");
        }
    }

    @Override
    public FileDO getFile(Long id) {
        return fileMapper.selectById(id);
    }

    @Override
    public PageResult<FileVO> images(Integer currentPage, Integer pageSize, String kind) {
        SortablePageParam sortablePageParam = new SortablePageParam();
        sortablePageParam.setPage(currentPage);
        sortablePageParam.setPerPage(pageSize);
        QueryWrapper<FileDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("kind", kind);
        PageResult<FileDO> fileDOPageResult = fileMapper.selectPage(sortablePageParam, queryWrapper);
        if (CollUtil.isEmpty(fileDOPageResult.getList())) {
            return new PageResult<>(List.of(), 0L, currentPage, pageSize);
        }
        List<FileVO> fileVOList = fileDOPageResult.getList().stream().map(fileDO -> {
            FileVO fileVO = new FileVO();
            BeanUtil.copyProperties(fileDO, fileVO);
            fileVO.setFilename(fileDO.getName());
            fileVO.setHref(fileDO.getUrl());
            fileVO.setAltText(FileNameUtil.mainName(fileDO.getName()));
            fileVO.setFilePath(fileDO.getUrl());
            fileVO.setOriginalName(fileDO.getName());
            fileVO.setFileSize(fileDO.getSize());
            return fileVO;
        }).toList();
        return new PageResult<>(fileVOList, fileDOPageResult.getTotal(), currentPage, pageSize);
    }

}
