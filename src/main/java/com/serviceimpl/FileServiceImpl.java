package com.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapperservice.FileMapperService;
import com.po.FileHelper;
import com.service.FileService;
import com.utils.OssFileUtils;
import com.utils.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

@Service("fileService")
public class FileServiceImpl extends ServiceImpl<FileMapperService, FileHelper> implements FileService {

    @Override
    public Response uploadFile(MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        String rslt = "error";
        try {
            rslt = OssFileUtils.uploadFile(fileName,multipartFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Response.error();
        }
        save(new FileHelper(rslt));
        return Response.ok().put("data", rslt);
    }

}
