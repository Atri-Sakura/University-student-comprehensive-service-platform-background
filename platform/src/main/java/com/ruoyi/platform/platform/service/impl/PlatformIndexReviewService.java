package com.ruoyi.platform.platform.service.impl;

import com.ruoyi.common.utils.file.MinioFileUtils;
import com.ruoyi.platform.domain.IndexImgUrl;
import com.ruoyi.platform.platform.mapper.PlatformIndexReviewMapper;
import com.ruoyi.platform.platform.service.IPlatformIndexReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlatformIndexReviewService implements IPlatformIndexReviewService {
    @Autowired
    private PlatformIndexReviewMapper platformIndexReviewMapper;
    @Autowired
    private MinioFileUtils minioFileUtils;
    @Override
    public List<IndexImgUrl> getUserIndexImgs() {
        return platformIndexReviewMapper.getUserIndexImgs();
    }

    @Override
    @Transactional
    public int deleteUserIndexImgs(IndexImgUrl indexImgUrl) {
        String url = indexImgUrl.getIndexImageUrl();
        try {
            int result = platformIndexReviewMapper.deleteUserIndexImgs(indexImgUrl.getIndexImageUrlId());
            if (result > 0) {
                minioFileUtils.deleteByUrl(url);
                return result;
            }
        }catch (Exception e){
            throw new RuntimeException("删除失败");
        }
        return 0;
    }

    @Override
    public int addIndexImgUrl(IndexImgUrl indexImgUrl) {
        return platformIndexReviewMapper.addIndexImgUrl(indexImgUrl.getIndexImageUrlId(),indexImgUrl.getIndexImageUrl());
    }
}
