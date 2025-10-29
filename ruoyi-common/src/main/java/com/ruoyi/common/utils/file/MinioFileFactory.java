package com.ruoyi.common.utils.file;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class MinioFileFactory {

    //    @Value("${minio.access-key}")
    private static final String accessKey = "ATRI";

    //    @Value("${minio.secret-key}")
    private static final String secretKey = "AAATTTRRRIII";

    //    @Value("${minio.endpoint}")
    private static final String endpoint = "http://182.254.228.15:9000";

    public static MinioClient minioClient = MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build();

    public String upload(MultipartFile file, String bucketName, Long id)throws Exception{

        if(file == null){
            return "上传文件不能为空";
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName =   id + "/"+ UUID.randomUUID().toString() + suffix;

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(),file.getSize(),-1)
                        .contentType(file.getContentType())
                        .build()
        );
        return endpoint + "/" + bucketName + "/" + fileName;
    }
}
