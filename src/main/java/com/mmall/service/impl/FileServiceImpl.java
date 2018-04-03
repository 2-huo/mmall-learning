package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;

import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
/**
 * Created by geely
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

//    public String readfile(String filepath) {
//        File file = new File(filepath);
//        if (!file.isDirectory()) {
//            System.out.println("文件");
//            System.out.println("path=" + file.getPath());
//            System.out.println("absolutepath=" + file.getAbsolutePath());
//            System.out.println("name=" + file.getName());
//
//        } else if (file.isDirectory()) {
//            System.out.println("文件夹");
//            String[] filelist = file.list();
//            for (int i = 0; i < filelist.length; i++) {
//                File readfile = new File(filepath + "\\" + filelist[i]);
//                if (!readfile.isDirectory()) {
//                    System.out.println("path=" + readfile.getPath());
//                    System.out.println("absolutepath="
//                            + readfile.getAbsolutePath());
//                    System.out.println("name=" + readfile.getName());
//
//                } else if (readfile.isDirectory()) {
//                    readfile(filepath + "\\" + filelist[i]);
//                }
//            }
//
//        }
//        return filepath;
//    }

    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        //扩展名
        //abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);


        try {
            file.transferTo(targetFile);
            //文件已经上传成功了


            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传到ftp服务器上

            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        //A:abc.jpg
        //B:abc.jpg
        return targetFile.getName();
    }

}
