package com.ruoyi.platform.platform.service;

import com.ruoyi.platform.domain.IndexImgUrl;

import java.util.List;

public interface IPlatformIndexReviewService {
    List<IndexImgUrl> getUserIndexImgs();

    int deleteUserIndexImgs(IndexImgUrl indexImgUrl);

    int addIndexImgUrl(IndexImgUrl indexImgUrl);
}
