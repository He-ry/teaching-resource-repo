package com.teach.service.file;

import com.baomidou.mybatisplus.extension.service.IService;
import com.teach.domain.dto.file.FileSaveDTO;
import com.teach.domain.pojo.PageResult;
import com.teach.domain.vo.file.FileVO;
import com.teach.domain.vo.file.ImageFileUploadVO;
import com.teach.domain.vo.file.VideoFileUploadVO;
import com.teach.models.entity.FileDO;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService extends IService<FileDO> {

    ImageFileUploadVO createFile(MultipartFile multipartFile);

    VideoFileUploadVO createVideoFile(MultipartFile multipartFile);

    void updateFile(@Valid FileSaveDTO dto);

    void deleteFile(String id);

    void deleteFileListByIds(List<Long> ids);

    FileDO getFile(Long id);

    PageResult<FileVO> images(Integer page, Integer pageSize,  String kind);
}
