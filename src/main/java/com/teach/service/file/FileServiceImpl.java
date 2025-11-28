package com.teach.service.file;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teach.domain.dto.file.FileSaveDTO;
import com.teach.exception.ServiceException;
import com.teach.models.entity.FileDO;
import com.teach.models.mapper.FileMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class FileServiceImpl extends ServiceImpl<FileMapper, FileDO> implements FileService {

    @Resource
    private FileMapper fileMapper;

    @Override
    public Long createFile(FileSaveDTO dto) {
        FileDO obj = BeanUtil.toBean(dto, FileDO.class);
        fileMapper.insert(obj);
        return obj.getId();
    }

    @Override
    public void updateFile(FileSaveDTO dto) {
        validateExists(dto.getId());
        FileDO updateObj = BeanUtil.toBean(dto, FileDO.class);
        fileMapper.updateById(updateObj);
    }

    @Override
    public void deleteFile(Long id) {
        validateExists(id);
        fileMapper.deleteById(id);
    }

    @Override
    public void deleteFileListByIds(List<Long> ids) {
        fileMapper.deleteByIds(ids);
    }

    private void validateExists(Long id) {
        if (fileMapper.selectById(id) == null) {
            throw new ServiceException("文件不存在");
        }
    }

    @Override
    public FileDO getFile(Long id) {
        return fileMapper.selectById(id);
    }

}
