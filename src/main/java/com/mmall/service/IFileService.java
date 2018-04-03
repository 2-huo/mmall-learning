package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by geely
 */
public interface IFileService {

//    String readfile(String path);

    String upload(MultipartFile file, String path);
}
