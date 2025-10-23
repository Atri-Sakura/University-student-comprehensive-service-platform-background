package com.ruoyi.framework.storage;


import org.springframework.web.multipart.MultipartFile;

/**
 * 云存储(支持七牛、阿里、腾讯)
 *待完成，当前只在本地存储
 */
public interface CloudStorageService {
    String upload(MultipartFile file, String relativePath);
}
