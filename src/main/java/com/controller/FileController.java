package com.controller;

import com.service.FileService;
import com.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin(origins = "http://124.221.127.36", maxAge = 3600, allowCredentials="true",allowedHeaders = "*",methods = {RequestMethod.POST,RequestMethod.GET})
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fileService;


    @PostMapping("/upload")
    public Response fileUpload(MultipartFile multipartFile) {
        return fileService.uploadFile(multipartFile);
    }

//    @PostMapping("/upload")
//    public Response fileUpload(MultipartFile[] multipartFiles, String fileName) {
//        return fileService.uploadFile(multipartFiles, fileName);
//    }
}
