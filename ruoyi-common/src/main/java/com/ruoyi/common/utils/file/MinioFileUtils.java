package com.ruoyi.common.utils.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class MinioFileUtils {

    MinioFileFactory minioFileFactory = new MinioFileFactory();

    public String upload(MultipartFile file, String bucketName, Long id) throws Exception {
        return minioFileFactory.upload(file, bucketName, id);
    }

    public String upload(byte[] data, String originalFileName, Long id) throws Exception {
        return minioFileFactory.upload(data, originalFileName, id);
    }

    /**
     * 根据对象路径删除文件
     * @param bucketName 存储桶名称
     * @param objectPath 对象路径（如：123/uuid.jpg 或 chat/123/uuid.jpg）
     * @return 删除成功返回true，失败返回false
     */
    public boolean delete(String bucketName, String objectPath) {
        return minioFileFactory.delete(bucketName, objectPath);
    }

    /**
     * 根据完整URL删除文件
     * @param fullUrl 完整URL（如：http://182.254.228.15:9000/merchantgood/123/uuid.jpg）
     * @return 删除成功返回true，失败返回false
     */
    public boolean deleteByUrl(String fullUrl) {
        return minioFileFactory.deleteByUrl(fullUrl);
    }

    /**
     * 安全删除 - 根据URL安全删除文件
     * @param fullUrl 完整URL
     * @return 删除成功返回true，失败返回false
     */
    public boolean safeDeleteByUrl(String fullUrl) {
        try {
            return deleteByUrl(fullUrl);
        } catch (Exception e) {
            // 安全删除，即使出现异常也不抛出
            return false;
        }
    }

    /**
     * 批量删除文件
     * @param bucketName 存储桶名称
     * @param objectPaths 对象路径列表
     * @return 成功删除的文件数量
     */
    public int batchDelete(String bucketName, List<String> objectPaths) {
        return minioFileFactory.batchDelete(bucketName, objectPaths);
    }

    /**
     * 批量删除文件 - 根据URL列表
     * @param urls 完整URL列表
     * @return 成功删除的文件数量
     */
    public int batchDeleteByUrls(List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        for (String url : urls) {
            if (deleteByUrl(url)) {
                successCount++;
            }
        }
        return successCount;
    }
}