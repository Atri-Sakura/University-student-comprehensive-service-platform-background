package com.ruoyi.common.utils.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MinioFileUtils {

    MinioFileFactory minioFileFactory = new MinioFileFactory();




    public  String upload(MultipartFile file, String bucketName, Long id) throws Exception {

        return minioFileFactory.upload(file,bucketName,id);

    }
}
