package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.po.FileHelper;
import com.utils.Response;
import org.springframework.web.multipart.MultipartFile;

public interface FileService extends IService<FileHelper> {

    Response uploadFile(MultipartFile multipartFiles);

}
