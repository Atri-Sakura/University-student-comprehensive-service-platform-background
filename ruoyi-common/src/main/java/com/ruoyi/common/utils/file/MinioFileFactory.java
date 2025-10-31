package com.ruoyi.common.utils.file;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
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

    /**
     * 头像，图片上传
     * @param file
     * @param bucketName
     * @param id
     * @return
     * @throws Exception
     */
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

    /**
     * 上传字节数组到MinIO的chat存储桶，并按chat/{id}/文件名结构存储
     * @param data  文件字节数组
     * @param id    关联的ID（用于路径分级）
     * @param originalFileName 原始文件名（用于保留后缀）
     * @return 存储的对象路径（如 chat/123/test.jpg），失败返回null
     * @throws Exception 异常抛出给上层处理
     */
    public String upload(byte[] data, String originalFileName,Long id) throws Exception {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        if (id == null) {
            throw new IllegalArgumentException("ID不能为空");
        }
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("原始文件名不能为空");
        }

        // 1. 定义存储桶（固定为chat）
        String bucketName = "chat";

        // 2. 处理文件名：保留原始后缀，避免重名（加UUID前缀）
        String fileExt = originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : ""; // 获取文件后缀（如 .jpg）
        String uniqueFileName = UUID.randomUUID().toString() + fileExt; // 生成唯一文件名

        // 3. 构建存储路径：chat/{id}/唯一文件名
        String objectPath = String.format("chat/%d/%s", id, uniqueFileName);

        try (InputStream is = new ByteArrayInputStream(data)) {
            // 4. 上传到MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectPath) // 完整路径：chat/id/文件名
                            .stream(is, data.length, -1) // -1表示不限制分片大小
                            .build()
            );
            log.info("文件上传成功，路径：{}", objectPath);
            return objectPath; // 返回完整存储路径，方便后续访问
        } catch (Exception e) {
            log.error("文件上传失败，ID：{}，原因：{}", id, e.getMessage(), e);
            throw e; // 抛出异常让上层处理（如事务回滚）
        }
    }
}
