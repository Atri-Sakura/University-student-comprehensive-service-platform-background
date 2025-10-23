package com.ruoyi.framework.storage.impl;

import com.ruoyi.framework.storage.CloudStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OssStorageService implements CloudStorageService {
    @Override
    public String upload(MultipartFile file, String relativePath) {
        // TODO: 接入阿里云OSS / MinIO SDK
        System.out.println("⚠️ 模拟云端上传：" + relativePath);
        return "https://cdn.example.com/" + relativePath;
    }
}
