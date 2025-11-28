package com.teach.service.file;

import com.baomidou.mybatisplus.extension.service.IService;
import com.teach.domain.dto.file.FileSaveDTO;
import com.teach.models.entity.FileDO;
import jakarta.validation.Valid;

import java.util.List;

public interface FileService extends IService<FileDO> {

    Long createFile(@Valid FileSaveDTO dto);

    void updateFile(@Valid FileSaveDTO dto);

    void deleteFile(Long id);

    void deleteFileListByIds(List<Long> ids);

    FileDO getFile(Long id);

}
