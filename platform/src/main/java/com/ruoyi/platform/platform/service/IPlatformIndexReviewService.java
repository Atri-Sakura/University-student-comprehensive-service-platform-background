package com.ruoyi.platform.platform.service;

import com.ruoyi.platform.domain.IndexImgUrl;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPlatformIndexReviewService {
    List<IndexImgUrl> getUserIndexImgs();

    int deleteUserIndexImgs(IndexImgUrl indexImgUrl);

    int addIndexImgUrl(MultipartFile file);
}
