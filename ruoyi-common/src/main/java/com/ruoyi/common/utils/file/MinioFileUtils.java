package com.ruoyi.common.utils.file;

import com.ruoyi.common.utils.uuid.UUID;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.ruoyi.common.utils.file.MinioFileFactory.minioClient;

@Component
public class MinioFileUtils {

    MinioFileFactory minioFileFactory = new MinioFileFactory();




    public  String upload(MultipartFile file, String bucketName,Long id) throws Exception {

        return minioFileFactory.upload(file,bucketName,id);

    }




}
